package simplepets.brainsynder.nms.entity;

import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftLivingEntity;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
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
        entityType = getEntityType(entitytypes, containsFields());
        originalEntityType = entitytypes;
        getBukkitEntity().remove();
    }

    public EntityBase(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, VersionTranslator.getWorldHandle(user.getPlayer().getLocation().getWorld()));
        this.user = user;
        this.petType = type;
        entityType = getEntityType(entitytypes, containsFields());
        originalEntityType = entitytypes;
    }

    // 1.20+   Replaces boolean rideableUnderWater()
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

    /**
     * This literally fixed the shit with p2 and i'm so fucking mad
     */
    public CraftEntity getBukkitEntity() {
        return new CraftLivingEntity(level.getCraftServer(), this) {
            @Override
            public org.bukkit.entity.EntityType getType() {
                return petType.getEntityType();
            }
        };
    }

    // TODO: THIS METHOD NEEDS TO BE LOOKED AT CAUSES SOME ISSUES ON 1.19.3
    EntityType<? extends Mob> getEntityType(EntityType<? extends Mob> originalType, boolean checkFields) {
        try {
            Field field = EntityType.class.getDeclaredField(VersionTranslator.ENTITY_FACTORY_FIELD);
            field.setAccessible(true);
            EntityType.Builder<? extends Mob> builder =
                    EntityType.Builder.of((EntityType.EntityFactory<? extends Mob>) field.get(originalType),
                            MobCategory.AMBIENT);
            builder.sized(0.1f, 0.1f);
            DefaultedRegistry<EntityType<?>> registry = BuiltInRegistries.ENTITY_TYPE;
            // frozen field
            Field frozen = null;
            if (checkFields) {
                frozen = registry.getClass().getSuperclass().getDeclaredField(VersionTranslator.REGISTRY_FROZEN_FIELD);
                frozen.setAccessible(true);
                frozen.set(registry, false);
            }
            // map field
            if (checkFields) {
                Field map = registry.getClass().getSuperclass().getDeclaredField(VersionTranslator.REGISTRY_ENTRY_MAP_FIELD);
                map.setAccessible(true);
                map.set(registry, new IdentityHashMap<>());
            }
            // screw you mojang, my power is unlimited
            EntityType<? extends Mob> mob = builder.build(petType.name().toLowerCase());
            if (checkFields && (frozen != null)) frozen.set(registry, true);
            return mob;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return originalType;
        }
    }

    private boolean containsFields() {
        try {
            BuiltInRegistries.ENTITY_TYPE.getClass().getSuperclass().getDeclaredField(VersionTranslator.REGISTRY_FROZEN_FIELD);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return VersionTranslator.getAddEntityPacket(this, originalEntityType, VersionTranslator.getPosition(this));
    }
}
