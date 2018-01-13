package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.api.entity.hostile.IEntityElderGuardianPet;
import simplepets.brainsynder.api.pet.IPet;
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
