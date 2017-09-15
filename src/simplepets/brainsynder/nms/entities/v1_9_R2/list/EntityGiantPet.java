package simplepets.brainsynder.nms.entities.v1_9_R2.list;

import net.minecraft.server.v1_9_R2.World;
import simplepets.brainsynder.nms.entities.type.IEntityGiantPet;
import simplepets.brainsynder.nms.entities.v1_9_R2.EntityPet;
import simplepets.brainsynder.pet.IPet;

@Deprecated
public class EntityGiantPet extends EntityPet implements IEntityGiantPet {
    public EntityGiantPet(World world, IPet pet) {
        super(world, pet);
    }

}
