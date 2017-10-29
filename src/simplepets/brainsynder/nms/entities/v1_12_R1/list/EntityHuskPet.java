package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntityHuskPet;
import simplepets.brainsynder.pet.IPet;

public class EntityHuskPet extends EntityZombiePet implements IEntityHuskPet {
    public EntityHuskPet(World world, IPet pet) {
        super(world, pet);
    }
    public EntityHuskPet(World world) {
        super(world);
    }
}
