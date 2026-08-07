package simplepets.brainsynder.nms.entity.branch;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bsdevelopment.pluginutils.sound.SafeSound;
import simplepets.brainsynder.api.pet.PetDataRegistry;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.entity.controller.ControllerCubeEntity;
import simplepets.brainsynder.nms.helper.VersionHelper;
import simplepets.brainsynder.nms.utils.PetDataAccess;

public class EntityCubeAbstractPet extends EntityPetOverride {
    private static final EntityDataAccessor<Integer> SIZE = SynchedEntityData.defineId(EntityCubeAbstractPet.class, EntityDataSerializers.INT);

    public EntityCubeAbstractPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
        this.moveControl = new ControllerCubeEntity(this);
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
        object.setInteger(PetDataRegistry.SIZE.namespace(), getSize());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(PetDataRegistry.SIZE.namespace())) setSize(object.getInteger(PetDataRegistry.SIZE.namespace()));
        super.applyCompound(object);
    }

    public int getSize() {
        return this.entityData.get(SIZE);
    }

    public void setSize(int i) {
        this.entityData.set(SIZE, i);
        VersionHelper.setAttributes(this, 0.2 + 0.1 * i, -1);
    }

    public void playJumpSound() {
        if (isPetSilent()) return;
        SimplePets.getPetConfigManager().getPetConfig(getPetType()).ifPresent(config -> {
            SafeSound sound = config.getSound();
            if (sound != null) sound.playAt(getEntity().getLocation(), 1f, 1f);
        });
    }
}
