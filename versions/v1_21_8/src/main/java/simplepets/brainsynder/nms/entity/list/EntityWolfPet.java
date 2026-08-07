package simplepets.brainsynder.nms.entity.list;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.wolf.WolfVariants;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import simplepets.brainsynder.api.entity.passive.IEntityWolfPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.DyeColorWrapper;
import simplepets.brainsynder.api.wrappers.WolfVariant;
import simplepets.brainsynder.nms.entity.EntityTameablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;
import simplepets.brainsynder.nms.utils.VariantUtils;

import static simplepets.brainsynder.api.pet.PetDataRegistry.COLOR;
import static simplepets.brainsynder.api.pet.PetDataRegistry.SHAKE;
import static simplepets.brainsynder.api.pet.PetDataRegistry.Wolf.*;

/**
 * NMS: {@link net.minecraft.world.entity.animal.wolf.Wolf}
 */
public class EntityWolfPet extends EntityTameablePet implements IEntityWolfPet {
    private static final EntityDataAccessor<Boolean> BEGGING = SynchedEntityData.defineId(EntityWolfPet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> COLLAR_COLOR = SynchedEntityData.defineId(EntityWolfPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANGER_TIME = SynchedEntityData.defineId(EntityWolfPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Holder<net.minecraft.world.entity.animal.wolf.WolfVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(EntityWolfPet.class, EntityDataSerializers.WOLF_VARIANT);

    private boolean angry = false;
    private boolean furWet = false;
    private int ticks = -1;
    private WolfVariant wolfVariant = WolfVariant.PALE;

    public EntityWolfPet(PetType type, PetUser user) {
        super(EntityType.WOLF, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("anger", isAngry());
        data.add("shaking", isShaking());
        data.add("head-tilted", isHeadTilted());
        data.add("collar-color", getColor().name());
        data.add("type", getWolfType().name());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(registryAccess(), WolfVariants.PALE));
        dataAccess.define(BEGGING, false);
        dataAccess.define(COLLAR_COLOR, DyeColorWrapper.WHITE.getWoolData());
        dataAccess.define(ANGER_TIME, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (furWet) {
            if (ticks == -1) {
                level().broadcastEntityEvent(this, (byte)8); // Wolf shaking
                ticks = 0;
            }

            ticks++;
            if (ticks >= 27) {
                ticks = 0;
                level().broadcastEntityEvent(this, (byte)8); // Wolf shaking
            }
        }
        if (this.angry && (entityData.get(ANGER_TIME) < 50)) entityData.set(ANGER_TIME, 500);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setEnum(VARIANT.namespace(), getWolfType());
        compound.setString(COLOR.namespace(), getColor().name().toLowerCase());
        compound.setBoolean(ANGRY.namespace(), isAngry());
        compound.setBoolean(TILT.namespace(), isHeadTilted());
        compound.setBoolean(SHAKE.namespace(), furWet);
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(VARIANT.namespace())) setWolfType(object.getEnum(VARIANT.namespace(), WolfVariant.class));
        if (object.hasKey(COLOR.namespace())) setColor(DyeColorWrapper.getByName(object.getString(COLOR.namespace())));
        if (object.hasKey(ANGRY.namespace())) setAngry(object.getBoolean(ANGRY.namespace(), false));
        if (object.hasKey(TILT.namespace())) setHeadTilted(object.getBoolean(TILT.namespace(), false));
        if (object.hasKey(SHAKE.namespace())) setShaking(object.getBoolean(SHAKE.namespace(), false));
        super.applyCompound(object);
    }

    @Override
    public boolean isHeadTilted() {
        return entityData.get(BEGGING);
    }

    @Override
    public void setHeadTilted(boolean headTilted) {
        entityData.set(BEGGING, headTilted);
    }

    @Override
    public boolean isAngry() {
        return entityData.get(ANGER_TIME) > 0;
    }

    @Override
    public void setAngry(boolean angry) {
        this.angry = angry;
        entityData.set(ANGER_TIME, angry ? 500 : 0);
    }

    @Override
    public WolfVariant getWolfType() {
        return wolfVariant;
    }

    @Override
    public void setWolfType(WolfVariant type) {
        this.wolfVariant = type;

        net.minecraft.world.entity.animal.wolf.WolfVariant variant = CraftRegistry.getMinecraftRegistry(Registries.WOLF_VARIANT)
                .getOptional(CraftNamespacedKey.toMinecraft(type.getKey())).orElseThrow();
        Registry<net.minecraft.world.entity.animal.wolf.WolfVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.WOLF_VARIANT);
        entityData.set(DATA_VARIANT_ID, registry.wrapAsHolder(variant));
    }

    @Override
    public DyeColorWrapper getColor() {
        return DyeColorWrapper.getByWoolData((byte) ((int) entityData.get(COLLAR_COLOR)));
    }

    @Override
    public void setColor(DyeColorWrapper color) {
        this.entityData.set(COLLAR_COLOR, color.getWoolData());
    }

    @Override
    public boolean isShaking() {
        return furWet;
    }

    @Override
    public void setShaking(boolean shaking) {
        this.furWet = shaking;

        if (!shaking) level().broadcastEntityEvent(this, (byte)56);
        this.ticks = -1;
    }
}
