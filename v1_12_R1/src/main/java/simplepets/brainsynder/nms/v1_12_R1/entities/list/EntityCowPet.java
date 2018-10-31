package simplepets.brainsynder.nms.v1_12_R1.entities.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityCowPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_12_R1.entities.AgeableEntityPet;

@Size(width = 0.9F, length = 1.3F)
public class EntityCowPet extends AgeableEntityPet implements IEntityCowPet {
    public EntityCowPet(World world, IPet pet) {
        super(world, pet);
    }
    public EntityCowPet(World world) {
        super(world);
    }
}
