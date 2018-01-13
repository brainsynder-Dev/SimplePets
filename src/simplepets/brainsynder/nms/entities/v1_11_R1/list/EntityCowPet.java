package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.api.entity.passive.IEntityCowPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.AgeableEntityPet;

public class EntityCowPet extends AgeableEntityPet implements IEntityCowPet {
    public EntityCowPet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityCowPet(World world) {
        super(world);
    }
}
