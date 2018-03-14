package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntityMooshroomPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;

public class EntityMooshroomPet extends AgeableEntityPet implements IEntityMooshroomPet {
    public EntityMooshroomPet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityMooshroomPet(World world) {
        super(world);
    }
}
