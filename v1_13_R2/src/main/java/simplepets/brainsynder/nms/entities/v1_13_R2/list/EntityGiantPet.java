package simplepets.brainsynder.nms.entities.v1_13_R2.list;

import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityGiantPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_13_R2.EntityPet;
import simplepets.brainsynder.nms.registry.v1_13_R2.Types;

@Size(width = 5.5F, length = 5.5F)
public class EntityGiantPet extends EntityPet implements IEntityGiantPet {
    public EntityGiantPet(World world, IPet pet) {
        super(Types.GIANT, world, pet);
    }
    public EntityGiantPet(World world) {
        super(Types.GIANT, world);
    }
}
