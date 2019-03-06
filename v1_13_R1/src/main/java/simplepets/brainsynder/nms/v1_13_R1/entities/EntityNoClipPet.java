package simplepets.brainsynder.nms.v1_13_R1.entities;

import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.World;
import simplepets.brainsynder.api.entity.misc.IEntityNoClipPet;
import simplepets.brainsynder.api.pet.IPet;

public abstract class EntityNoClipPet extends EntityPet implements IEntityNoClipPet {
    public EntityNoClipPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }

    public EntityNoClipPet(EntityTypes<?> type, World world) {
        super(type, world);
    }

    public void noClip(boolean flag) {
        this.noclip = flag;
    }
}