package simplepets.brainsynder.nms.entity;

import lib.brainsynder.reflection.Reflection;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftLivingEntity;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.CitizensFixer;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.list.EntityRabbitPet;
import simplepets.brainsynder.nms.entity.list.EntitySlimePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;
import simplepets.brainsynder.utils.VersionFields;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;

public class EntityBase extends Mob {
    protected final EntityType<? extends Mob> entityType;
    protected final EntityType<? extends Mob> originalEntityType;
    private PetUser user;
    private PetType petType;
    private volatile CraftEntity bukkitEntity;

    protected EntityBase(EntityType<? extends Mob> entitytypes, Level world) {
        super(entitytypes, world);
        entityType = getEntityType(entitytypes);
        originalEntityType = entitytypes;
        VersionTranslator.getBukkitEntity(this).remove();
    }

    public EntityBase(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, VersionTranslator.getWorldHandle(user.getPlayer().getLocation().getWorld()));
        this.user = user;
        this.petType = type;
        entityType = getEntityType(entitytypes);
        originalEntityType = entitytypes;
    }

    @Override
    public void jumpFromGround() {
        if (this instanceof EntitySlimePet) {
            Vec3 vec3d = this.getDeltaMovement();
            this.setDeltaMovement(vec3d.x, this.getJumpPower(), vec3d.z);
            this.hasImpulse = true;
            return;
        }

        super.jumpFromGround();

        if (this instanceof EntityRabbitPet) {
            double speed = this.moveControl.getSpeedModifier();
            if (speed > 0.0D) {
                double length = getDeltaMovement().horizontalDistanceSqr();
                if (length < 0.01D) {
                    this.moveRelative(0.1F, new Vec3(0.0D, 0.0D, 1.0D));
                }
            }

            if (!VersionTranslator.getEntityLevel(this).isClientSide()) VersionTranslator.getEntityLevel(this).broadcastEntityEvent(this, (byte)1);
        }
    }

    @Override
    protected void handlePortal() {
        // fuck around and find out
    }

    public void populateDataAccess(PetDataAccess dataAccess) {}

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder datawatcher) {
        super.defineSynchedData(datawatcher);

        PetDataAccess dataAccess = new PetDataAccess();
        populateDataAccess(dataAccess);
        dataAccess.getAccessorDefinitions().forEach(datawatcher::define);
    }

    // 1.20.1+   Replaces boolean rideableUnderWater()
    @Override
    public boolean dismountsUnderwater() {
        return false;
    }

    public PetType getPetType() {
        return petType;
    }

    public PetUser getUser() {
        return user;
    }

    EntityType<? extends Mob> getEntityType(EntityType<? extends Mob> originalType) {
        try {
            DefaultedRegistry<EntityType<?>> registry = CitizensFixer.getVanillaRegistry(BuiltInRegistries.ENTITY_TYPE);

            // Resets the Entity Registry to the vanilla one...
            CitizensFixer.overrideRegistry(registry);

            // Melts the frozen status, so we can register the mob...
            Field frozen = Reflection.getField(registry.getClass().getSuperclass(), VersionFields.current().getRegistryFrozenField());
            if (frozen != null) frozen.set(registry, false);

            // Clears the intrusive holder field to an empty map
            Field intrusiveField = Reflection.getField(registry.getClass().getSuperclass(), VersionFields.current().getRegistryIntrusiveField());
            if (intrusiveField != null) intrusiveField.set(registry, new IdentityHashMap<>());

            // Fetch the entity type instance before we resume
            EntityType<? extends Mob> mob = handleMobBuilder(originalType);

            // Puts the registry back in the freezer... (Buuurrrrrr)
            if (frozen != null) frozen.set(registry, true);

            // If a custom registry was found before, reset it back so that plugin can continue to work...
            // COUGH COUGH... Citizens... COUGH COUGH...
            CitizensFixer.resetCustomRegistry();
            return mob;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return originalType;
        }
    }

    private EntityType<? extends Mob> handleMobBuilder(EntityType<? extends Mob> originalType) throws NoSuchFieldException, IllegalAccessException {
        Field field = Reflection.getField(EntityType.class, VersionFields.current().getEntityFactoryField());

        EntityType.Builder<? extends Mob> builder = EntityType.Builder.of(
                (EntityType.EntityFactory<? extends Mob>) field.get(originalType),
                MobCategory.AMBIENT
        );
        builder.sized(0.1f, 0.1f);

        return builder.build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace(petType.name().toLowerCase())));
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (this.bukkitEntity == null) {
            synchronized (this) {
                if (this.bukkitEntity == null) {
                    return this.bukkitEntity = new CraftLivingEntity(this.level().getCraftServer(), this) {};
                }
            }
        }
        return this.bukkitEntity;
    }

    /**
     * Overrides the Paper method
     */
    public CraftLivingEntity getBukkitLivingEntity() {
        return (CraftLivingEntity) this.getBukkitEntity();
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entitytrackerentry) {
        return VersionTranslator.getAddEntityPacket(this, entitytrackerentry, originalEntityType, VersionTranslator.getPosition(this));
    }


    /**
     * These methods prevent pets from being saved in the worlds
     */
    @Override
    public boolean saveAsPassenger(CompoundTag nbttagcompound) {// Calls e
        return false;
    }

    @Override
    public boolean save(CompoundTag nbttagcompound) {// Calls e
        return false;
    }

    @Override
    public void load(CompoundTag nbttagcompound) {
    }
}
