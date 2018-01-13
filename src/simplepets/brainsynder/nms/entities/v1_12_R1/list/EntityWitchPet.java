package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntityWitchPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.EntityPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.utils.Size;

@Size(width = 0.6F, length = 1.9F)
public class EntityWitchPet extends EntityPet implements IEntityWitchPet {
    public EntityWitchPet(World world) {
        super(world);
    }
    public EntityWitchPet(World world, IPet pet) {
        super(world, pet);
    }
}
