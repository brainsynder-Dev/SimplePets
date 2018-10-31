package simplepets.brainsynder.nms.v1_12_R1.entities.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntitySpiderPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_12_R1.entities.EntityPet;

@Size(width = 1.4F, length = 0.9F)
public class EntitySpiderPet extends EntityPet implements IEntitySpiderPet {
    public EntitySpiderPet(World world, IPet pet) {
        super(world, pet);
    }
    public EntitySpiderPet(World world) {
        super(world);
    }
}
