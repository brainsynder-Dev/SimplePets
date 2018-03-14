package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntityChickenPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;

public class EntityChickenPet extends AgeableEntityPet implements IEntityChickenPet {
    public EntityChickenPet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityChickenPet(World world) {
        super(world);
    }
}
