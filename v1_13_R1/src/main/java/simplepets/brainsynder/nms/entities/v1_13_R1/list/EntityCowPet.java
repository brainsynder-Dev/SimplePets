package simplepets.brainsynder.nms.entities.v1_13_R1.list;

import net.minecraft.server.v1_13_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityCowPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_13_R1.AgeableEntityPet;
import simplepets.brainsynder.nms.registry.v1_13_R1.Types;

@Size(width = 0.9F, length = 1.3F)
public class EntityCowPet extends AgeableEntityPet implements IEntityCowPet {
    public EntityCowPet(World world, IPet pet) {
        super(Types.COW, world, pet);
    }
    public EntityCowPet(World world) {
        super(Types.COW, world);
    }
}
