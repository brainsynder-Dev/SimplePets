package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityEndermitePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.EntityPet;
import simplepets.brainsynder.nms.v1_13_R2.registry.Types;

@Size(width = 0.4F, length = 0.3F)
public class EntityEndermitePet extends EntityPet implements IEntityEndermitePet {
    public EntityEndermitePet(World world, IPet pet) {
        super(Types.ENDERMITE, world, pet);
    }
    public EntityEndermitePet(World world) {
        super(Types.ENDERMITE, world);
    }
}
