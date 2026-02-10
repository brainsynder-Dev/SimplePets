package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.utils.DyeColorWrapper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.WolfVariant;
import org.bukkit.craftbukkit.v1_21_R3.CraftRegistry;
import org.bukkit.craftbukkit.v1_21_R3.util.CraftNamespacedKey;
import org.bukkit.entity.Wolf;
import simplepets.brainsynder.api.entity.passive.IEntityWolfPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.WolfType;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.EntityTameablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.Wolf}
 */
public class EntityWolfPet extends EntityTameablePet implements IEntityWolfPet {
    private static final EntityDataAccessor<Boolean> BEGGING = SynchedEntityData.defineId(EntityWolfPet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> COLLAR_COLOR = SynchedEntityData.defineId(EntityWolfPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANGER_TIME = SynchedEntityData.defineId(EntityWolfPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Holder<WolfVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(EntityWolfPet.class, EntityDataSerializers.WOLF_VARIANT);

    private boolean angry = false;
    private boolean furWet = false;
    private int ticks = -1;
    private WolfType wolfType = WolfType.PALE;

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
        dataAccess.define(DATA_VARIANT_ID, VersionTranslator.getRegistryValue(CraftRegistry.getMinecraftRegistry(Registries.WOLF_VARIANT), Wolf.Variant.PALE.getKey()));
        dataAccess.define(BEGGING, false);
        dataAccess.define(COLLAR_COLOR, DyeColorWrapper.WHITE.getWoolData());
        dataAccess.define(ANGER_TIME, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (furWet) {
            if (ticks == -1) {
                VersionTranslator.getEntityLevel(this).broadcastEntityEvent(this, (byte)8); // Wolf shaking
                ticks = 0;
            }

            ticks++;
            if (ticks >= 27) {
                ticks = 0;
                VersionTranslator.getEntityLevel(this).broadcastEntityEvent(this, (byte)8); // Wolf shaking
            }
        }
        if (this.angry && (entityData.get(ANGER_TIME) < 50)) entityData.set(ANGER_TIME, 500);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setEnum("type", getWolfType());
        compound.setString("color", getColor().name().toLowerCase());
        compound.setBoolean("angry", isAngry());
        compound.setBoolean("tilted", isHeadTilted());
        compound.setBoolean("shaking", furWet);
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("type")) setWolfType(object.getEnum("type", WolfType.class));
        if (object.hasKey("color")) setColor(DyeColorWrapper.getByName(object.getString("color")));
        if (object.hasKey("angry")) setAngry(object.getBoolean("angry", false));
        if (object.hasKey("tilted")) setHeadTilted(object.getBoolean("tilted", false));
        if (object.hasKey("shaking")) setShaking(object.getBoolean("shaking", false));
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
    public WolfType getWolfType() {
        return wolfType;
    }

    @Override
    public void setWolfType(WolfType type) {
        this.wolfType = type;

        WolfVariant variant = CraftRegistry.getMinecraftRegistry(Registries.WOLF_VARIANT)
                .getOptional(CraftNamespacedKey.toMinecraft(type.getKey())).orElseThrow();
        Registry<WolfVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.WOLF_VARIANT);
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

        if (!shaking) VersionTranslator.getEntityLevel(this).broadcastEntityEvent(this, (byte)56);
        this.ticks = -1;
    }
}
