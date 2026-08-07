package simplepets.brainsynder.nms.entity.branch;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.misc.IHorseAbstract;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.Horse.EATING;
import static simplepets.brainsynder.api.pet.PetDataRegistry.SADDLE;

/**
 * NMS: {@link net.minecraft.world.entity.animal.horse.AbstractHorse}
 */
public class EntityHorseAbstractPet extends EntityAgeablePet implements IHorseAbstract {
    private static final EntityDataAccessor<Byte> STATUS = SynchedEntityData.defineId(EntityHorseAbstractPet.class, EntityDataSerializers.BYTE);
    protected boolean isJumping;
    private boolean isSaddled = false;

    public EntityHorseAbstractPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
        doIndirectAttach = true;
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("saddled", this.isPetSaddled());
        data.add("eating", isEating());
        data.add("angry", isAngry());
        data.add("rearing", isRearing());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(STATUS, (byte) 0);
    }

    @Override
    public boolean isPetSaddled() {
        return this.isSaddled;
    }

    @Override
    public void setPetSaddled(boolean saddled) {
        this.isSaddled = saddled;
        setItemSlot(EquipmentSlot.SADDLE, saddled ? Items.SADDLE.getDefaultInstance() : Items.AIR.getDefaultInstance());
    }

    public boolean isJumping() {
        return this.isJumping;
    }

    public void setIsJumping(boolean flag) {
        this.isJumping = flag;
    }

    public double getCustomJump() {
        return this.getAttributeValue(Attributes.JUMP_STRENGTH);
    }


    @Override
    public void setSpecialFlag(int flag, boolean value) {
        byte b0 = this.entityData.get(STATUS);
        if(value){
            this.entityData.set(STATUS, (byte) (b0 | flag));
        }else{
            this.entityData.set(STATUS, (byte) (b0 & (~flag)));
        }
    }

    @Override
    public boolean getSpecialFlag(int flag) {
        return (this.entityData.get(STATUS) & flag) != 0;
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean(SADDLE.namespace(), this.isPetSaddled());
        object.setBoolean(EATING.namespace(), isEating());
        object.setBoolean("angry", isAngry());
        object.setBoolean("rearing", isRearing());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(SADDLE.namespace())) this.setPetSaddled(object.getBoolean(SADDLE.namespace()));
        if (object.hasKey(EATING.namespace())) setEating(object.getBoolean(EATING.namespace()));
        if (object.hasKey("angry")) setAngry(object.getBoolean("angry"));
        if (object.hasKey("rearing")) setRearing(object.getBoolean("rearing"));
        super.applyCompound(object);
    }
}
