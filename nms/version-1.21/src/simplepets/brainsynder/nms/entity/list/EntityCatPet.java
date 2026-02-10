package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.utils.DyeColorWrapper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.CatVariant;
import org.bukkit.craftbukkit.v1_21_R1.CraftRegistry;
import simplepets.brainsynder.api.entity.passive.IEntityCatPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.CatType;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.EntityTameablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.Cat}
 */
public class EntityCatPet extends EntityTameablePet implements IEntityCatPet {
    private static final EntityDataAccessor<Holder<CatVariant>> TYPE = SynchedEntityData.defineId(EntityCatPet.class, EntityDataSerializers.CAT_VARIANT);
    private static final EntityDataAccessor<Boolean> SLEEPING_WITH_OWNER = SynchedEntityData.defineId(EntityCatPet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HEAD_UP = SynchedEntityData.defineId(EntityCatPet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> COLLAR_COLOR = SynchedEntityData.defineId(EntityCatPet.class, EntityDataSerializers.INT);
    private CatType type = CatType.TABBY;

    public EntityCatPet(PetType type, PetUser user) {
        super(EntityType.CAT, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("type", getCatType().name());
        data.add("collar", getCollarColor().name());
        data.add("sleeping", isPetSleeping());
        data.add("head-up", isHeadUp());
        data.add("tamed", isTamed());
        data.add("sitting", isSitting());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(TYPE, BuiltInRegistries.CAT_VARIANT.getHolderOrThrow(CatVariant.TABBY));
        dataAccess.define(SLEEPING_WITH_OWNER, false);
        dataAccess.define(HEAD_UP, false);
        dataAccess.define(COLLAR_COLOR, DyeColorWrapper.WHITE.getWoolData());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setEnum("type", getCatType());
        compound.setEnum("collar", getCollarColor());
        compound.setBoolean("sleeping", isPetSleeping());
        compound.setBoolean("head_up", isHeadUp());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("type")) setCatType(object.getEnum("type", CatType.class, CatType.TABBY));
        if (object.hasKey("collar"))
            setCollarColor(object.getEnum("collar", DyeColorWrapper.class, DyeColorWrapper.WHITE));
        if (object.hasKey("sleeping")) setPetSleeping(object.getBoolean("sleeping", false));
        if (object.hasKey("head_up")) setHeadUp(object.getBoolean("head_up", false));
        super.applyCompound(object);
    }

    @Override
    public CatType getCatType() {
        return type;
    }

    @Override
    public void setCatType(CatType type) {
        this.type = type;

        Registry<CatVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.CAT_VARIANT);
        entityData.set(TYPE, registry.wrapAsHolder(VersionTranslator.getRegistryValue(registry, type.getKey())));
    }

    @Override
    public DyeColorWrapper getCollarColor() {
        return DyeColorWrapper.getByWoolData((byte) ((int) entityData.get(COLLAR_COLOR)));
    }

    @Override
    public void setCollarColor(DyeColorWrapper color) {
        entityData.set(COLLAR_COLOR, color.ordinal());
    }

    @Override
    public boolean isHeadUp() {
        return entityData.get(HEAD_UP);
    }

    @Override
    public void setHeadUp(boolean value) {
        entityData.set(HEAD_UP, value);
    }

    @Override
    public boolean isPetSleeping() {
        return entityData.get(SLEEPING_WITH_OWNER);
    }

    @Override
    public void setPetSleeping(boolean sleeping) {
        entityData.set(SLEEPING_WITH_OWNER, sleeping);
    }
}
