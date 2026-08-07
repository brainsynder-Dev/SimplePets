package simplepets.brainsynder.nms.entity.list;

import net.minecraft.sounds.SoundEvents;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.hostile.IEntityRavagerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.branch.EntityRaiderPet;

import static simplepets.brainsynder.api.pet.PetDataRegistry.Ravager.CHOMP;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Ravager}
 */
public class EntityRavagerPet extends EntityRaiderPet implements IEntityRavagerPet {
    private int attackTick = 10;
    private boolean chomping = false;

    public EntityRavagerPet(PetType type, PetUser user) {
        super(EntitySelector.RAVAGER, type, user);
        doIndirectAttach = true;
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("chomping", isChomping());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean(CHOMP.namespace(), chomping);
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(CHOMP.namespace())) setChomping(object.getBoolean(CHOMP.namespace(), false));
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
            level().broadcastEntityEvent(this, (byte)4);
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
