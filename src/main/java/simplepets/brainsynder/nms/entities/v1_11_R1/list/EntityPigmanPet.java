package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.api.entity.hostile.IEntityPigmanPet;
import simplepets.brainsynder.api.pet.IPet;

public class EntityPigmanPet extends EntityZombiePet implements IEntityPigmanPet {
    public EntityPigmanPet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityPigmanPet(World world) {
        super(world);
    }
}
