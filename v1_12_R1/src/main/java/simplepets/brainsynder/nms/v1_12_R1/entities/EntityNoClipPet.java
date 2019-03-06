package simplepets.brainsynder.nms.v1_12_R1.entities;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.api.entity.misc.IEntityNoClipPet;
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