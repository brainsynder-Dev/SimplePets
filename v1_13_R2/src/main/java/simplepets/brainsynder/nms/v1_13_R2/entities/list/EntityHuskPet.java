package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityHuskPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.registry.Types;

@Size(width = 0.6F, length = 1.8F)
public class EntityHuskPet extends EntityZombiePet implements IEntityHuskPet {
    public EntityHuskPet(World world, IPet pet) {
        super(Types.HUSK, world, pet);
    }
    public EntityHuskPet(World world) {
        super(Types.HUSK, world);
    }
}