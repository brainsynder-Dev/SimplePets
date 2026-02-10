package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import simplepets.brainsynder.api.entity.passive.IEntityParrotPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.ParrotVariant;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.EntityTameablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.Parrot}
 */
public class EntityParrotPet extends EntityTameablePet implements IEntityParrotPet {
    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(EntityParrotPet.class, EntityDataSerializers.INT);
    private boolean rainbow = false;
    private int toggle = 0;

    public EntityParrotPet(PetType type, PetUser user) {
        super(EntityType.PARROT, type, user);
        this.moveControl = new FlyingMoveControl(this, 10, false);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("variant", getVariant().name());
        data.add("rainbow", isRainbow());
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        VersionTranslator.setupFlyingNavigation(this, level, navigation);
        return navigation;
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(TYPE, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (rainbow) {
            if (toggle == 4) {
                setVariant(ParrotVariant.getNext(getVariant()));
                getPetUser().updateDataMenu();
                toggle = 0;
            }
            toggle++;
        }
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("variant")) setVariant(ParrotVariant.getByName(object.getString("variant")));
        if (object.hasKey("rainbow")) rainbow = object.getBoolean("rainbow");
        super.applyCompound(object);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        if (!rainbow) object.setString("variant", getVariant().name());
        object.setBoolean("rainbow", rainbow);
        return object;
    }

    @Override
    public ParrotVariant getVariant() {
        return ParrotVariant.getById(entityData.get(TYPE));
    }

    @Override
    public void setVariant(ParrotVariant variant) {
        this.entityData.set(TYPE, variant.ordinal());
    }

    @Override
    public boolean isRainbow() {
        return rainbow;
    }

    @Override
    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    @Override
    public void travel(Vec3 vec3) {
        if (isOwnerRiding()) {
            super.travel(vec3);
            VersionTranslator.calculateEntityAnimation(this, false);
            return;
        }
        if (this.isInWater()) {
            this.moveRelative(0.02F, vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.800000011920929D));
        } else if (this.isInLava()) {
            this.moveRelative(0.02F, vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
        } else {
            this.moveRelative(this.getSpeed(), vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8100000262260437D));
        }

        VersionTranslator.calculateEntityAnimation(this, false);
    }
}
