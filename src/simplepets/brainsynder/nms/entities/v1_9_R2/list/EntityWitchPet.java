package simplepets.brainsynder.nms.entities.v1_9_R2.list;

import net.minecraft.server.v1_9_R2.World;
import simplepets.brainsynder.nms.entities.type.IEntityWitchPet;
import simplepets.brainsynder.nms.entities.v1_9_R2.EntityPet;
import simplepets.brainsynder.pet.IPet;

@Deprecated
public class EntityWitchPet extends EntityPet implements IEntityWitchPet {
    public EntityWitchPet(World world, IPet pet) {
        super(world, pet);
    }

}
