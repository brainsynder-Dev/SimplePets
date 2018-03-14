package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntityEndermitePet;
import simplepets.brainsynder.nms.entities.v1_11_R1.EntityPet;
import simplepets.brainsynder.pet.IPet;

public class EntityEndermitePet extends EntityPet implements IEntityEndermitePet {
    public EntityEndermitePet(World world, IPet pet) {
        super(world, pet);
    }


    public EntityEndermitePet(World world) {
        super(world);
    }
}
