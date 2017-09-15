package simplepets.brainsynder.nms.entities.v1_9_R2.list;

import net.minecraft.server.v1_9_R2.World;
import simplepets.brainsynder.nms.entities.type.IEntityChickenPet;
import simplepets.brainsynder.nms.entities.v1_9_R2.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;

@Deprecated
public class EntityChickenPet extends AgeableEntityPet implements IEntityChickenPet {
    public EntityChickenPet(World world, IPet pet) {
        super(world, pet);
    }
}
