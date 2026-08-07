package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bsdevelopment.pluginutils.version.VersionLimit;
import simplepets.brainsynder.api.entity.passive.IEntityHappyGhastPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.ColorWrapper;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.RESET_COLOR;

/**
 * NMS: {@link net.minecraft.world.entity.animal.HappyGhast}
 */
@VersionLimit(min = {1, 21, 6})
public class EntityHappyGhastPet extends EntityAgeablePet implements IEntityHappyGhastPet {
    private static final EntityDataAccessor<Boolean> IS_LEASH_HOLDER = SynchedEntityData.defineId(EntityHappyGhastPet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> STAYS_STILL = SynchedEntityData.defineId(EntityHappyGhastPet.class, EntityDataSerializers.BOOLEAN);
    private ColorWrapper color = ColorWrapper.NONE;

    public EntityHappyGhastPet(PetType type, PetUser user) {
        super(EntityType.HAPPY_GHAST, type, user);
        this.moveControl = new FlyingMoveControl(this, 20, false);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        return navigation;
    }

    @Override
    public void travel(Vec3 vec3) {
        if (isOwnerRiding()) {
            super.travel(vec3);
            calculateEntityAnimation(false);
            return;
        }
        if (isInWater()) {
            moveRelative(0.02F, vec3);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.800000011920929D));
        } else if (isInLava()) {
            moveRelative(0.02F, vec3);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.5D));
        } else {
            if (this.onGround) {
                setDeltaMovement(getDeltaMovement().x, 0.15, getDeltaMovement().z);
            } else {
                double phase = (getId() % 20) * (Math.PI / 10.0);
                double bob = Math.sin(level().getGameTime() * 0.1 + phase) * 0.03;
                setDeltaMovement(getDeltaMovement().x, bob, getDeltaMovement().z);
            }
            moveRelative(getSpeed(), vec3);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.8100000262260437D));
        }
        calculateEntityAnimation(false);
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
        compound.setEnum(RESET_COLOR.namespace(), getColorWrapper());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(RESET_COLOR.namespace())) setColorWrapper(object.getEnum(RESET_COLOR.namespace(), ColorWrapper.class, ColorWrapper.NONE));
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
