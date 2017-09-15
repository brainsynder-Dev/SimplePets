package simplepets.brainsynder.nms.entities.v1_10_R1.list;

import net.minecraft.server.v1_10_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntityCowPet;
import simplepets.brainsynder.nms.entities.v1_10_R1.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;

@Deprecated
public class EntityCowPet extends AgeableEntityPet implements IEntityCowPet {
    public EntityCowPet(World world, IPet pet) {
        super(world, pet);
    }

}
