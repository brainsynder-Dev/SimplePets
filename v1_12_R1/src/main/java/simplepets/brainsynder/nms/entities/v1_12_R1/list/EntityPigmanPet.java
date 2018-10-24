package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityPigmanPet;
import simplepets.brainsynder.api.pet.IPet;

@Size(width = 0.6F, length = 1.8F)
public class EntityPigmanPet extends EntityZombiePet implements IEntityPigmanPet {
    public EntityPigmanPet(World world, IPet pet) {
        super(world, pet);
    }
    public EntityPigmanPet(World world) {
        super(world);
    }
}
