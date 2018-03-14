package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntityElderGuardianPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.utils.Size;

@Size(width = 0.85F * 2.35F, length = 0.85F * 2.35F)
public class EntityElderGuardianPet extends EntityGuardianPet implements IEntityElderGuardianPet {
    public EntityElderGuardianPet(World world) {
        super(world);
    }
    public EntityElderGuardianPet(World world, IPet pet) {
        super(world, pet);
    }
}
