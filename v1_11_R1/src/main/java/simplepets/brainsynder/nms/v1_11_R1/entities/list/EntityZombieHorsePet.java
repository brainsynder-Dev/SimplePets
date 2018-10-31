package simplepets.brainsynder.nms.v1_11_R1.entities.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.api.entity.passive.IEntityZombieHorsePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_11_R1.entities.branch.EntityHorseChestedAbstractPet;

public class EntityZombieHorsePet extends EntityHorseChestedAbstractPet implements IEntityZombieHorsePet {

    public EntityZombieHorsePet(World world) {
        super(world);
    }

    public EntityZombieHorsePet(World world, IPet pet) {
        super(world, pet);
    }
}
