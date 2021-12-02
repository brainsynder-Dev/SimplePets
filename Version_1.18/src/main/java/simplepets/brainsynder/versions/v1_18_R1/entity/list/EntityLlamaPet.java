package simplepets.brainsynder.versions.v1_18_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.DyeColor;
import simplepets.brainsynder.api.entity.passive.IEntityLlamaPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.ColorWrapper;
import simplepets.brainsynder.api.wrappers.LlamaColor;
import simplepets.brainsynder.versions.v1_18_R1.entity.branch.EntityDonkeyAbstractPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityLlama}
 */
public class EntityLlamaPet extends EntityDonkeyAbstractPet implements IEntityLlamaPet {
    private static final EntityDataAccessor<Integer> STRENGTH;
    private static final EntityDataAccessor<Integer> COLOR;
    private static final EntityDataAccessor<Integer> VARIANT;

    public EntityLlamaPet(PetType type, PetUser user) {
        this(EntityType.LLAMA, type, user);
    }

    public EntityLlamaPet(EntityType<? extends Mob> llama, PetType type, PetUser user) {
        super(llama, type, user);
        doIndirectAttach = false;
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(STRENGTH, 0);
        this.entityData.define(COLOR, -1);
        this.entityData.define(VARIANT, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setString("skin", getSkinColor().name());
        object.setString("color", getColor().name());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("skin")) setSkinColor(LlamaColor.getByName(object.getString("skin")));
        if (object.hasKey("color"))
            setColor(ColorWrapper.getByName(object.getString("color")));
        super.applyCompound(object);
    }

    @Override
    public void setSkinColor(LlamaColor skinColor) {
        this.entityData.set(VARIANT, skinColor.ordinal());
    }

    @Override
    public LlamaColor getSkinColor() {
        return LlamaColor.getByID(entityData.get(VARIANT));
    }

    @Override
    public ColorWrapper getColor() {
        ColorWrapper color = ColorWrapper.getByWoolData((byte) ((int) entityData.get(COLOR)));
        if (color == null) color = ColorWrapper.getByDyeData((byte) ((int) entityData.get(COLOR)));
        return color;
    }

    @Override
    public void setColor(ColorWrapper color) {
        if (color == ColorWrapper.NONE) {
            entityData.set(COLOR, -1);
            return;
        }
        entityData.set(COLOR, DyeColor.byId(color.getWoolData()).getId());
    }

    static {
        STRENGTH = SynchedEntityData.defineId(EntityLlamaPet.class, EntityDataSerializers.INT);
        COLOR = SynchedEntityData.defineId(EntityLlamaPet.class, EntityDataSerializers.INT);
        VARIANT = SynchedEntityData.defineId(EntityLlamaPet.class, EntityDataSerializers.INT);
    }
}
