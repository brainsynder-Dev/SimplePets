package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.sounds.SoundMaker;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import simplepets.brainsynder.api.entity.hostile.IEntitySlimePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityPet;
import simplepets.brainsynder.versions.v1_17_R1.entity.controller.ControllerSlime;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntitySlime}
 */
public class EntitySlimePet extends EntityPet implements IEntitySlimePet {
    private static final EntityDataAccessor<Integer> SIZE;

    public EntitySlimePet(PetType type, PetUser user) {
        this(EntityType.SLIME, type, user);
    }

    public EntitySlimePet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
        this.moveControl = new ControllerSlime(this);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(SIZE, 2);
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
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2 + 0.1 * i);
    }

    static {
        SIZE = SynchedEntityData.defineId(EntitySlimePet.class, EntityDataSerializers.INT);
    }

    public void playJumpSound() {
        if (isPetSilent()) return;
        SimplePets.getPetConfigManager().getPetConfig(getPetType()).ifPresent(config -> {
            SoundMaker sound = config.getSound();
            if (sound != null) sound.playSound(getEntity());
        });
    }
}
