package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import simplepets.brainsynder.api.entity.passive.IEntityStriderPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Strider}
 */
public class EntityStriderPet extends EntityAgeablePet implements IEntityStriderPet {
    private static final EntityDataAccessor<Integer> BOOST_TIME = SynchedEntityData.defineId(EntityStriderPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> COLD = SynchedEntityData.defineId(EntityStriderPet.class, EntityDataSerializers.BOOLEAN);

    public EntityStriderPet(PetType type, PetUser user) {
        super(EntityType.STRIDER, type, user);
        doIndirectAttach = true;
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("cold", isCold());
        data.add("saddled", isPetSaddled());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(BOOST_TIME, 0);
        dataAccess.define(COLD, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("cold", isCold());
        object.setBoolean("saddled", isPetSaddled());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("cold")) setCold(object.getBoolean("cold"));
        if (object.hasKey("saddled")) setPetSaddled(object.getBoolean("saddled"));
        super.applyCompound(object);
    }

    @Override
    public boolean isPetSaddled() {
        return !getItemBySlot(EquipmentSlot.SADDLE).isEmpty();
    }

    @Override
    public void setPetSaddled(boolean saddled) {
        setItemSlot(EquipmentSlot.SADDLE, saddled ? new ItemStack(Items.SADDLE) : ItemStack.EMPTY);
    }

    @Override
    public boolean isCold() {
        return entityData.get(COLD);
    }

    @Override
    public void setCold(boolean cold) {
        entityData.set(COLD, cold);
    }
}
