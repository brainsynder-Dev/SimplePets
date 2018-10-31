package simplepets.brainsynder.nms.v1_13_R1.entities.list;

import net.minecraft.server.v1_13_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntitySilverfishPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R1.entities.EntityPet;
import simplepets.brainsynder.nms.v1_13_R1.registry.Types;

@Size(width = 0.3F, length = 0.7F)
public class EntitySilverfishPet extends EntityPet implements IEntitySilverfishPet {
    public EntitySilverfishPet(World world) {
        super(Types.SILVERFISH, world);
    }
    public EntitySilverfishPet(World world, IPet pet) {
        super(Types.SILVERFISH, world, pet);
    }
}
