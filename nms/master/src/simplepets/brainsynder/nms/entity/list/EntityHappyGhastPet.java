package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import simplepets.brainsynder.api.entity.passive.IEntityHappyGhastPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.ColorWrapper;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.HappyGhast}
 */
@SupportedVersion(version = ServerVersion.v1_21_6)
public class EntityHappyGhastPet extends EntityAgeablePet implements IEntityHappyGhastPet {
    private static final EntityDataAccessor<Boolean> IS_LEASH_HOLDER = SynchedEntityData.defineId(EntityHappyGhastPet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> STAYS_STILL = SynchedEntityData.defineId(EntityHappyGhastPet.class, EntityDataSerializers.BOOLEAN);
    private ColorWrapper color = ColorWrapper.NONE;

    public EntityHappyGhastPet(PetType type, PetUser user) {
        super(EntityType.HAPPY_GHAST, type, user);
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);

        dataAccess.define(IS_LEASH_HOLDER, false);
        dataAccess.define(STAYS_STILL, false);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("color", getColorWrapper().name());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setEnum("color", getColorWrapper());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("color")) setColorWrapper(object.getEnum("color", ColorWrapper.class, ColorWrapper.NONE));
        super.applyCompound(object);
    }

    @Override
    public ColorWrapper getColorWrapper() {
        return this.color;
    }

    @Override
    public void setColorWrapper(ColorWrapper color) {
        this.color = color;

        switch (color) {
            case NONE -> setItemSlot(EquipmentSlot.BODY, Items.AIR.getDefaultInstance());
            case WHITE -> setItemSlot(EquipmentSlot.BODY, Items.WHITE_HARNESS.getDefaultInstance());
            case ORANGE -> setItemSlot(EquipmentSlot.BODY, Items.ORANGE_HARNESS.getDefaultInstance());
            case MAGENTA -> setItemSlot(EquipmentSlot.BODY, Items.MAGENTA_HARNESS.getDefaultInstance());
            case LIGHT_BLUE -> setItemSlot(EquipmentSlot.BODY, Items.LIGHT_BLUE_HARNESS.getDefaultInstance());
            case YELLOW -> setItemSlot(EquipmentSlot.BODY, Items.YELLOW_HARNESS.getDefaultInstance());
            case LIME -> setItemSlot(EquipmentSlot.BODY, Items.LIME_HARNESS.getDefaultInstance());
            case PINK -> setItemSlot(EquipmentSlot.BODY, Items.PINK_HARNESS.getDefaultInstance());
            case GRAY -> setItemSlot(EquipmentSlot.BODY, Items.GRAY_HARNESS.getDefaultInstance());
            case LIGHT_GRAY -> setItemSlot(EquipmentSlot.BODY, Items.LIGHT_GRAY_HARNESS.getDefaultInstance());
            case CYAN -> setItemSlot(EquipmentSlot.BODY, Items.CYAN_HARNESS.getDefaultInstance());
            case PURPLE -> setItemSlot(EquipmentSlot.BODY, Items.PURPLE_HARNESS.getDefaultInstance());
            case BLUE -> setItemSlot(EquipmentSlot.BODY, Items.BLUE_HARNESS.getDefaultInstance());
            case BROWN -> setItemSlot(EquipmentSlot.BODY, Items.BROWN_HARNESS.getDefaultInstance());
            case GREEN -> setItemSlot(EquipmentSlot.BODY, Items.GREEN_HARNESS.getDefaultInstance());
            case RED -> setItemSlot(EquipmentSlot.BODY, Items.RED_HARNESS.getDefaultInstance());
            case BLACK -> setItemSlot(EquipmentSlot.BODY, Items.BLACK_HARNESS.getDefaultInstance());
        }
    }
}
