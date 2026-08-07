package simplepets.brainsynder.nms.entity.branch;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.misc.IChestedAbstractPet;
import simplepets.brainsynder.api.pet.PetDataRegistry;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.utils.PetDataAccess;

public class EntityDonkeyAbstractPet extends EntityHorseAbstractPet implements IChestedAbstractPet {
    private static final EntityDataAccessor<Boolean> CHEST = SynchedEntityData.defineId(EntityDonkeyAbstractPet.class, EntityDataSerializers.BOOLEAN);

    public EntityDonkeyAbstractPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("chest", isChested());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(CHEST, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean(PetDataRegistry.Horse.CHEST.namespace(), isChested());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(PetDataRegistry.Horse.CHEST.namespace())) setChested(object.getBoolean(PetDataRegistry.Horse.CHEST.namespace(), false));
        super.applyCompound(object);
    }

    @Override
    public boolean isChested() {
        return entityData.get(CHEST);
    }

    @Override
    public void setChested(boolean flag) {
        this.entityData.set(CHEST, flag);
    }
}