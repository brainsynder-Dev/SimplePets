package simplepets.brainsynder.nms.entities.v1_9_R2.list;

import net.minecraft.server.v1_9_R2.World;
import simplepets.brainsynder.nms.entities.type.IEntitySilverfishPet;
import simplepets.brainsynder.nms.entities.v1_9_R2.EntityPet;
import simplepets.brainsynder.pet.IPet;

@Deprecated
public class EntitySilverfishPet extends EntityPet implements IEntitySilverfishPet {
    public EntitySilverfishPet(World world, IPet pet) {
        super(world, pet);
    }

}
