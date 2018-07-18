package simplepets.brainsynder.nms.entities.v1_13_R1.list;

import net.minecraft.server.v1_13_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityIronGolemPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_13_R1.EntityPet;
import simplepets.brainsynder.nms.registry.v1_13_R1.Types;

@Size(width = 1.4F, length = 2.9F)
public class EntityIronGolemPet extends EntityPet implements IEntityIronGolemPet {
    public EntityIronGolemPet(World world, IPet pet) {
        super(Types.IRON_GOLEM, world, pet);
    }
    public EntityIronGolemPet(World world) {
        super(Types.IRON_GOLEM, world);
    }
}
