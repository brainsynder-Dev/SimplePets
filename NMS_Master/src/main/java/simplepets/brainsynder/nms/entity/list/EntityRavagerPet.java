package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityRavagerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.branch.EntityRaiderPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityRavager}
 */
public class EntityRavagerPet extends EntityRaiderPet implements IEntityRavagerPet {
    private int attackTick = 10;
    private boolean chomping = false;

    public EntityRavagerPet(PetType type, PetUser user) {
        super(EntityType.RAVAGER, type, user);
        doIndirectAttach = true;
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
            this.playSound(SoundEvents.RAVAGER_ATTACK, 1.0F, 1.0F);
            VersionTranslator.getEntityLevel(this).broadcastEntityEvent(this, (byte)4);
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
