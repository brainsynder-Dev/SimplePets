package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.passive.IEntitySheepPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.DyeColorWrapper;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.*;

/**
 * NMS: {@link net.minecraft.world.entity.animal.Sheep}
 */
public class EntitySheepPet extends EntityAgeablePet implements IEntitySheepPet {
    private static final EntityDataAccessor<Byte> DYE_COLOR = SynchedEntityData.defineId(EntitySheepPet.class, EntityDataSerializers.BYTE);
    private DyeColorWrapper color = DyeColorWrapper.WHITE;
    private boolean rainbow = false;
    private int toggle = 0;

    public EntitySheepPet(PetType type, PetUser user) {
        super(EntitySelector.SHEEP, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        if (!isRainbow()) data.add("color", getColor().name());
        data.add("sheared", isSheared());
        data.add("rainbow", isRainbow());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(DYE_COLOR, (byte) 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        if (!rainbow) object.setString(COLOR.namespace(), getColor().name());
        object.setBoolean(SHEAR.namespace(), isSheared());
        object.setBoolean(RAINBOW.namespace(), rainbow);
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(RAINBOW.namespace())) rainbow = object.getBoolean(RAINBOW.namespace(), false);
        if (object.hasKey(COLOR.namespace())) setColor(DyeColorWrapper.getByName(object.getString(COLOR.namespace())));
        if (object.hasKey(SHEAR.namespace())) setSheared(object.getBoolean(SHEAR.namespace(), false));
        super.applyCompound(object);
    }

    @Override
    public DyeColorWrapper getColor() {
        return color;
    }

    @Override
    public void setColor(DyeColorWrapper color) {
        this.color = color;
        if (!isSheared()) entityData.set(DYE_COLOR, (byte)color.getWoolData());
    }

    @Override
    public boolean isSheared() {
        byte data = this.entityData.get(DYE_COLOR);
        return (data & 0xF0) != 0;
    }

    public void setSheared(boolean flag) {
        byte data = this.entityData.get(DYE_COLOR);
        if (flag) {
            this.entityData.set(DYE_COLOR, (byte) (data | 0x10));
        } else {
            this.entityData.set(DYE_COLOR, (byte) (data & 0xFFFFFFEF));
            setColor(color);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (rainbow) {
            if (toggle == 4) {
                setColor(DyeColorWrapper.getNext(getColor()));
                getPetUser().updateDataMenu();
                toggle = 0;
            }
            toggle++;
        }
    }

    @Override
    public boolean isRainbow() {
        return rainbow;
    }

    @Override
    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }
}
