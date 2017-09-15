package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntitySquidPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.EntityPet;
import simplepets.brainsynder.pet.IPet;

public class EntitySquidPet extends EntityPet implements IEntitySquidPet {
    public EntitySquidPet(World world, IPet pet) {
        super(world, pet);
    }

    public EntitySquidPet(World world) {
        super(world);
    }

}
