package simplepets.brainsynder.nms.entity;

import lib.brainsynder.reflection.Reflection;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.CitizensFixer;
import simplepets.brainsynder.nms.VersionFields;
import simplepets.brainsynder.nms.VersionTranslator;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;

public class EntityBase extends Mob {
    protected final EntityType<? extends Mob> entityType;
    protected final EntityType<? extends Mob> originalEntityType;
    private PetUser user;
    private PetType petType;

    protected EntityBase(EntityType<? extends Mob> entitytypes, Level world) {
        super(entitytypes, world);
        entityType = getEntityType(entitytypes);
        originalEntityType = entitytypes;
        getBukkitEntity().remove();
    }

    public EntityBase(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, VersionTranslator.getWorldHandle(user.getPlayer().getLocation().getWorld()));
        this.user = user;
        this.petType = type;
        entityType = getEntityType(entitytypes);
        originalEntityType = entitytypes;
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
            Field frozen = Reflection.getField(registry.getClass().getSuperclass(), VersionFields.v1_20_2.getRegistryFrozenField());
            if (frozen != null) frozen.set(registry, false);

            // Clears the intrusive holder field to an empty map
            Field intrusiveField = Reflection.getField(registry.getClass().getSuperclass(), VersionFields.v1_20_2.getRegistryIntrusiveField());
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
        Field field = Reflection.getField(EntityType.class, VersionFields.v1_20_2.getEntityFactoryField());

        EntityType.Builder<? extends Mob> builder = EntityType.Builder.of(
                (EntityType.EntityFactory<? extends Mob>) field.get(originalType),
                MobCategory.AMBIENT
        );
        builder.sized(0.1f, 0.1f);

        return builder.build(petType.name().toLowerCase());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return VersionTranslator.getAddEntityPacket(this, originalEntityType, VersionTranslator.getPosition(this));
    }
}
