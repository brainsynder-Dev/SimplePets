package simplepets.brainsynder.nms.entities.v1_9_R2.list;

import net.minecraft.server.v1_9_R2.World;
import simplepets.brainsynder.nms.entities.type.IEntityEndermitePet;
import simplepets.brainsynder.nms.entities.v1_9_R2.EntityPet;
import simplepets.brainsynder.pet.IPet;

@Deprecated
public class EntityEndermitePet extends EntityPet implements IEntityEndermitePet {
    public EntityEndermitePet(World world, IPet pet) {
        super(world, pet);
    }

}
