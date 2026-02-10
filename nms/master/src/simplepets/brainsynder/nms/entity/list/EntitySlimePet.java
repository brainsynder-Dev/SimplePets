package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.sounds.SoundMaker;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import simplepets.brainsynder.api.entity.hostile.IEntitySlimePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.entity.controller.ControllerSlime;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Slime}
 */
public class EntitySlimePet extends EntityPetOverride implements IEntitySlimePet {
    private static final EntityDataAccessor<Integer> SIZE = SynchedEntityData.defineId(EntitySlimePet.class, EntityDataSerializers.INT);

    public EntitySlimePet(PetType type, PetUser user) {
        this(EntityType.SLIME, type, user);
    }

    public EntitySlimePet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
        this.moveControl = new ControllerSlime(this);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        data.add("size", getSize());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(SIZE, 2);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("size", getSize());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("size")) setSize(object.getInteger("size"));
        super.applyCompound(object);
    }

    public int getSize() {
        return this.entityData.get(SIZE);
    }

    public void setSize(int i) {
        this.entityData.set(SIZE, i);
        VersionTranslator.setAttributes(this, 0.2 + 0.1 * i, -1);
    }

    public void playJumpSound() {
        if (isPetSilent()) return;
        SimplePets.getPetConfigManager().getPetConfig(getPetType()).ifPresent(config -> {
            SoundMaker sound = config.getSound();
            if (sound != null) sound.playSound(getEntity());
        });
    }
}
