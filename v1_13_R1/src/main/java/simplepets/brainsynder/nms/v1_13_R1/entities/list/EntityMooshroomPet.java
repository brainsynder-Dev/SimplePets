package simplepets.brainsynder.nms.v1_13_R1.entities.list;

import net.minecraft.server.v1_13_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityMooshroomPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R1.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_13_R1.registry.Types;

@Size(width = 0.9F, length = 1.3F)
public class EntityMooshroomPet extends AgeableEntityPet implements IEntityMooshroomPet {
    public EntityMooshroomPet(World world, IPet pet) {
        super(Types.MOOSHROOM, world, pet);
    }
    public EntityMooshroomPet(World world) {
        super(Types.MOOSHROOM, world);
    }
}
