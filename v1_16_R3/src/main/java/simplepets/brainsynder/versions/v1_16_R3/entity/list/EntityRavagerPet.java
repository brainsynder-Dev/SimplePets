package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.SoundEffects;
import simplepets.brainsynder.api.entity.hostile.IEntityRavagerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.branch.EntityRaiderPet;

public class EntityRavagerPet extends EntityRaiderPet implements IEntityRavagerPet {
    private int attackTick = 10;
    private boolean chomping = false;

    public EntityRavagerPet(PetType type, PetUser user) {
        super(EntityTypes.RAVAGER, type, user);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("chomping", chomping);
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("chomping")) setChomping(object.getBoolean("chomping", false));
        super.applyCompound(object);
    }

    @Override
    public void tick() {
        super.tick();
        if (!chomping) return;

        if (this.attackTick > 0) {
            --this.attackTick;
        }

        if (this.attackTick <= 0) {
            this.attackTick = 20;
            this.playSound(SoundEffects.ENTITY_RAVAGER_ATTACK, 1.0F, 1.0F);
            this.world.broadcastEntityEffect(this, (byte)4);
        }
    }

    @Override
    public boolean isChomping() {
        return chomping;
    }

    @Override
    public void setChomping(boolean chomping) {
        this.chomping = chomping;

        if (chomping) this.attackTick = 10;
    }
}
