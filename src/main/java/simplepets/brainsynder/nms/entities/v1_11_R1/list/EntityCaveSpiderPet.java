package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.api.entity.hostile.IEntityCaveSpiderPet;
import simplepets.brainsynder.api.pet.IPet;

public class EntityCaveSpiderPet extends EntitySpiderPet implements IEntityCaveSpiderPet {
    public EntityCaveSpiderPet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityCaveSpiderPet(World world) {
        super(world);
    }

}
