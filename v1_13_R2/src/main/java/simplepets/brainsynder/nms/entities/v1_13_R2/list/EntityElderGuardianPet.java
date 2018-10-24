package simplepets.brainsynder.nms.entities.v1_13_R2.list;

import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityElderGuardianPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.registry.v1_13_R2.Types;

@Size(width = 0.85F * 2.35F, length = 0.85F * 2.35F)
public class EntityElderGuardianPet extends EntityGuardianPet implements IEntityElderGuardianPet {
    public EntityElderGuardianPet(World world) {
        super(Types.ELDER_GUARDIAN, world);
    }
    public EntityElderGuardianPet(World world, IPet pet) {
        super(Types.ELDER_GUARDIAN, world, pet);
    }
}
