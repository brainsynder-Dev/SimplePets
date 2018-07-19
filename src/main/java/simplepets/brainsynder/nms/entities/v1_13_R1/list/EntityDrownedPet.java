package simplepets.brainsynder.nms.entities.v1_13_R1.list;

import net.minecraft.server.v1_13_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityHuskPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.registry.v1_13_R1.Types;

@Size(width = 0.6F, length = 1.8F)
public class EntityDrownedPet extends EntityZombiePet implements IEntityHuskPet {
    public EntityDrownedPet(World world, IPet pet) {
        super(Types.DROWNED, world, pet);
    }
    public EntityDrownedPet(World world) {
        super(Types.DROWNED, world);
    }
}
