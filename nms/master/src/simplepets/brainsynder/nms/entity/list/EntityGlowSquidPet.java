package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityGlowSquidPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.GlowSquid}
 */
public class EntityGlowSquidPet extends EntitySquidPet implements IEntityGlowSquidPet {
    private static final EntityDataAccessor<Integer> DATA_DARK_TICKS_REMAINING = SynchedEntityData.defineId(EntityGlowSquidPet.class, EntityDataSerializers.INT);

    public EntityGlowSquidPet(PetType type, PetUser user) {
        super(EntityType.GLOW_SQUID, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        data.add("glowing", isSquidGlowing());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(DATA_DARK_TICKS_REMAINING, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setBoolean("glowing", isSquidGlowing());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("glowing")) setSquidGlowing(object.getBoolean("glowing"));
        super.applyCompound(object);
    }

    @Override
    public boolean isSquidGlowing() {
        return entityData.get(DATA_DARK_TICKS_REMAINING) == 0;
    }

    @Override
    public void setSquidGlowing(boolean glowing) { // Does this even make the GlowSquid darker?!
        entityData.set(DATA_DARK_TICKS_REMAINING, glowing ? 0 : 100);
    }
}
