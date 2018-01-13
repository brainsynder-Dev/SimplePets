package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.api.entity.hostile.IEntitySilverfishPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.EntityPet;

public class EntitySilverfishPet extends EntityPet implements IEntitySilverfishPet {

    public EntitySilverfishPet(World world) {
        super(world);
    }

    public EntitySilverfishPet(World world, IPet pet) {
        super(world, pet);
    }

}
