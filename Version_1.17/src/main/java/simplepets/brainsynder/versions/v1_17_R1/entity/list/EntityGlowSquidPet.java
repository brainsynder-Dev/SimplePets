package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityGlowSquidPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

/**
 * NMS: {@link net.minecraft.world.entity.GlowSquid}
 */
public class EntityGlowSquidPet extends EntitySquidPet implements IEntityGlowSquidPet {
    private static final EntityDataAccessor<Integer> DATA_DARK_TICKS_REMAINING;

    public EntityGlowSquidPet(PetType type, PetUser user) {
        super(EntityType.GLOW_SQUID, type, user);
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
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        entityData.define(DATA_DARK_TICKS_REMAINING, 0);
    }

    @Override
    public boolean isSquidGlowing() {
        return entityData.get(DATA_DARK_TICKS_REMAINING) == 0;
    }

    @Override
    public void setSquidGlowing(boolean glowing) { // Does this even make the GlowSquid darker?!
        entityData.set(DATA_DARK_TICKS_REMAINING, glowing ? 0 : 100);
    }

    static {
        DATA_DARK_TICKS_REMAINING = SynchedEntityData.defineId(EntityGlowSquidPet.class, EntityDataSerializers.INT);
    }
}
