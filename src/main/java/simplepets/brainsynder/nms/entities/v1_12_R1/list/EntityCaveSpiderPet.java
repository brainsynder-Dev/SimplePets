package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntityCaveSpiderPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.utils.Size;

@Size(width = 0.7F, length = 0.5F)
public class EntityCaveSpiderPet extends EntitySpiderPet implements IEntityCaveSpiderPet {
    public EntityCaveSpiderPet(World world, IPet pet) {
        super(world, pet);
    }
    public EntityCaveSpiderPet(World world) {
        super(world);
    }
}
