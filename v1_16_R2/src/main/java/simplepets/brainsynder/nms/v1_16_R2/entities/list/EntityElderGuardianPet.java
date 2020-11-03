package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityElderGuardianPet;
import simplepets.brainsynder.api.pet.IPet;


/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityGuardianElder}
 */
@Size(width = 0.85F * 2.35F, length = 0.85F * 2.35F)
public class EntityElderGuardianPet extends EntityGuardianPet implements IEntityElderGuardianPet {
    public EntityElderGuardianPet(World world) {
        super(EntityTypes.ELDER_GUARDIAN, world);
    }
    public EntityElderGuardianPet(World world, IPet pet) {
        super(EntityTypes.ELDER_GUARDIAN, world, pet);
    }
}
