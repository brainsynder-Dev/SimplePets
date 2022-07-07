package simplepets.brainsynder.nms.entity;

import net.minecraft.core.Registry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftLivingEntity;
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

    EntityType<? extends Mob> getEntityType(EntityType<? extends Mob> originalType, boolean checkFields) {
        try {
            Field field = EntityType.class.getDeclaredField(VersionTranslator.ENTITY_FACTORY_FIELD);
            field.setAccessible(true);
            EntityType.Builder<? extends Mob> builder =
                    EntityType.Builder.of((EntityType.EntityFactory<? extends Mob>) field.get(originalType),
                            MobCategory.AMBIENT);
            builder.sized(0.1f, 0.1f);
            Registry<EntityType<?>> registry = Registry.ENTITY_TYPE;
            // frozen field
            Field frozen = null;
            if (checkFields) {
                frozen = registry.getClass().getSuperclass().getDeclaredField("ca");
                frozen.setAccessible(true);
                frozen.set(registry, false);
            }
            // map field
            if (checkFields) {
                Field map = registry.getClass().getSuperclass().getDeclaredField("cc");
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
            Registry.ENTITY_TYPE.getClass().getSuperclass().getDeclaredField("ca");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
