package simplepets.brainsynder.nms.entities.v1_9_R2.list;

import net.minecraft.server.v1_9_R2.World;
import simplepets.brainsynder.nms.entities.type.IEntityMooshroomPet;
import simplepets.brainsynder.nms.entities.v1_9_R2.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;

@Deprecated
public class EntityMooshroomPet extends AgeableEntityPet implements IEntityMooshroomPet {
    public EntityMooshroomPet(World world, IPet pet) {
        super(world, pet);
    }
}
