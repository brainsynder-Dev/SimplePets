package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.api.entity.hostile.IEntityHuskPet;
import simplepets.brainsynder.api.pet.IPet;

public class EntityHuskPet extends EntityZombiePet implements IEntityHuskPet {
    public EntityHuskPet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityHuskPet(World world) {
        super(world);
    }
}
