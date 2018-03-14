package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntityGiantPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.EntityPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.utils.Size;

@Size(width = 5.5F, length = 5.5F)
public class EntityGiantPet extends EntityPet implements IEntityGiantPet {
    public EntityGiantPet(World world, IPet pet) {
        super(world, pet);
    }
    public EntityGiantPet(World world) {
        super(world);
    }
}
