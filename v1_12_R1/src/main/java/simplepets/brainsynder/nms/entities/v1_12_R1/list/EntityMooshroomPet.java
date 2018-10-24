package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityMooshroomPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.AgeableEntityPet;

@Size(width = 0.9F, length = 1.3F)
public class EntityMooshroomPet extends AgeableEntityPet implements IEntityMooshroomPet {
    public EntityMooshroomPet(World world, IPet pet) {
        super(world, pet);
    }
    public EntityMooshroomPet(World world) {
        super(world);
    }
}
