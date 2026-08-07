package simplepets.brainsynder.nms.entity.list;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.feline.CatVariants;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bukkit.craftbukkit.CraftRegistry;
import simplepets.brainsynder.api.entity.passive.IEntityCatPet;
import simplepets.brainsynder.api.pet.PetDataRegistry;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.CatVariant;
import simplepets.brainsynder.api.wrappers.DyeColorWrapper;
import simplepets.brainsynder.nms.entity.EntityTameablePet;
import simplepets.brainsynder.nms.helper.VersionHelper;
import simplepets.brainsynder.nms.utils.PetDataAccess;
import simplepets.brainsynder.nms.utils.VariantUtils;

import static simplepets.brainsynder.api.pet.PetDataRegistry.COLOR;
import static simplepets.brainsynder.api.pet.PetDataRegistry.Cat.LOOK_UP;
import static simplepets.brainsynder.api.pet.PetDataRegistry.SLEEP;

/**
 * NMS: {@link net.minecraft.world.entity.animal.feline.Cat}
 */
public class EntityCatPet extends EntityTameablePet implements IEntityCatPet {
    private static final EntityDataAccessor<Holder<net.minecraft.world.entity.animal.feline.CatVariant>> TYPE = SynchedEntityData.defineId(EntityCatPet.class, EntityDataSerializers.CAT_VARIANT);
    private static final EntityDataAccessor<Boolean> SLEEPING_WITH_OWNER = SynchedEntityData.defineId(EntityCatPet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HEAD_UP = SynchedEntityData.defineId(EntityCatPet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> COLLAR_COLOR = SynchedEntityData.defineId(EntityCatPet.class, EntityDataSerializers.INT);
    private CatVariant type = CatVariant.TABBY;

    public EntityCatPet(PetType type, PetUser user) {
        super(EntityType.CAT, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("type", getCatType().name());
        data.add("collar", getColor().name());
        data.add("sleeping", isPetSleeping());
        data.add("head-up", isHeadUp());
        data.add("tamed", isTamed());
        data.add("sitting", isSitting());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(TYPE, VariantUtils.getDefaultOrAny(this.registryAccess(), CatVariants.TABBY));
        dataAccess.define(SLEEPING_WITH_OWNER, false);
        dataAccess.define(HEAD_UP, false);
        dataAccess.define(COLLAR_COLOR, DyeColorWrapper.WHITE.getWoolData());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setEnum(PetDataRegistry.Cat.TYPE.namespace(), getCatType());
        compound.setEnum(COLOR.namespace(), getColor());
        compound.setBoolean(SLEEP.namespace(), isPetSleeping());
        compound.setBoolean(LOOK_UP.namespace(), isHeadUp());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(PetDataRegistry.Cat.TYPE.namespace())) setCatType(object.getEnum(PetDataRegistry.Cat.TYPE.namespace(), CatVariant.class, CatVariant.TABBY));
        if (object.hasKey(COLOR.namespace())) setColor(object.getEnum(COLOR.namespace(), DyeColorWrapper.class, DyeColorWrapper.WHITE));
        if (object.hasKey("collar")) setColor(object.getEnum("collar", DyeColorWrapper.class, DyeColorWrapper.WHITE));
        if (object.hasKey("sleeping")) setPetSleeping(object.getBoolean("sleeping", false));
        if (object.hasKey(SLEEP.namespace())) setPetSleeping(object.getBoolean(SLEEP.namespace(), false));
        if (object.hasKey(LOOK_UP.namespace())) setHeadUp(object.getBoolean(LOOK_UP.namespace(), false));
        super.applyCompound(object);
    }

    @Override
    public CatVariant getCatType() {
        return type;
    }

    @Override
    public void setCatType(CatVariant type) {
        this.type = type;

        Registry<net.minecraft.world.entity.animal.feline.CatVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.CAT_VARIANT);
        entityData.set(TYPE, registry.wrapAsHolder(VersionHelper.VERSION_TRANSLATOR.getRegistryValue(registry, type.getKey())));
    }

    @Override
    public DyeColorWrapper getColor() {
        return DyeColorWrapper.getByWoolData((byte) ((int) entityData.get(COLLAR_COLOR)));
    }

    @Override
    public void setColor(DyeColorWrapper color) {
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
