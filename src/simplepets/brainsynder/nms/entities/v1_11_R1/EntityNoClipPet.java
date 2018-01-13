package simplepets.brainsynder.nms.entities.v1_11_R1;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.api.entity.IEntityNoClipPet;
import simplepets.brainsynder.api.pet.IPet;

public abstract class EntityNoClipPet extends EntityPet implements IEntityNoClipPet {
    public EntityNoClipPet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityNoClipPet(World world) {
        super(world);
    }

    public void noClip(boolean flag) {
        this.noclip = flag;
    }
}