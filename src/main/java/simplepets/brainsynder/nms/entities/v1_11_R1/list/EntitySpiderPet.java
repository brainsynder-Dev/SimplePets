package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntitySpiderPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.EntityPet;
import simplepets.brainsynder.pet.IPet;

public class EntitySpiderPet extends EntityPet implements IEntitySpiderPet {
    public EntitySpiderPet(World world, IPet pet) {
        super(world, pet);
    }


    public EntitySpiderPet(World world) {
        super(world);
    }
}
