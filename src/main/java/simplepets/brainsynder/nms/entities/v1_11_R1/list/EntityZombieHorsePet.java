package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntityZombieHorsePet;
import simplepets.brainsynder.nms.entities.v1_11_R1.branch.EntityHorseChestedAbstractPet;
import simplepets.brainsynder.pet.IPet;

public class EntityZombieHorsePet extends EntityHorseChestedAbstractPet implements IEntityZombieHorsePet {

    public EntityZombieHorsePet(World world) {
        super(world);
    }

    public EntityZombieHorsePet(World world, IPet pet) {
        super(world, pet);
    }
}
