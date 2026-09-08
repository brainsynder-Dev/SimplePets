package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.passive.IEntityStriderPet;
import simplepets.brainsynder.api.pet.PetDataRegistry;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.SADDLE;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Strider}
 */
public class EntityStriderPet extends EntityAgeablePet implements IEntityStriderPet {
    private static final EntityDataAccessor<Integer> BOOST_TIME = SynchedEntityData.defineId(EntityStriderPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> COLD = SynchedEntityData.defineId(EntityStriderPet.class, EntityDataSerializers.BOOLEAN);

    public EntityStriderPet(PetType type, PetUser user) {
        super(EntitySelector.STRIDER, type, user);
        doIndirectAttach = true;
        setNoAi(true);
    }

    @Override
    public boolean isEffectiveAi() {
        return true;
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
        object.setBoolean(PetDataRegistry.Strider.COLD.namespace(), isCold());
        object.setBoolean(SADDLE.namespace(), isPetSaddled());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(PetDataRegistry.Strider.COLD.namespace())) setCold(object.getBoolean(PetDataRegistry.Strider.COLD.namespace()));
        if (object.hasKey(SADDLE.namespace())) setPetSaddled(object.getBoolean(SADDLE.namespace()));
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
