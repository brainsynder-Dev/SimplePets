package simplepets.brainsynder.nms.entities.v1_13_R1.list;

import net.minecraft.server.v1_13_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityCaveSpiderPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.registry.v1_13_R1.Types;

@Size(width = 0.7F, length = 0.5F)
public class EntityCaveSpiderPet extends EntitySpiderPet implements IEntityCaveSpiderPet {
    public EntityCaveSpiderPet(World world, IPet pet) {
        super(Types.CAVE_SPIDER, world, pet);
    }
    public EntityCaveSpiderPet(World world) {
        super(Types.CAVE_SPIDER, world);
    }
}
