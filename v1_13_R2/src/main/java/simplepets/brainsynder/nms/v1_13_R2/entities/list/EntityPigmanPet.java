package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityPigmanPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.registry.Types;

@Size(width = 0.6F, length = 1.8F)
public class EntityPigmanPet extends EntityZombiePet implements IEntityPigmanPet {
    public EntityPigmanPet(World world, IPet pet) {
        super(Types.PIGMAN, world, pet);
    }
    public EntityPigmanPet(World world) {
        super(Types.PIGMAN, world);
    }
}
