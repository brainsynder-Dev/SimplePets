package simplepets.brainsynder.nms.v1_11_R1.entities.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.api.entity.hostile.IEntityElderGuardianPet;
import simplepets.brainsynder.api.pet.IPet;

public class EntityElderGuardianPet extends EntityGuardianPet implements IEntityElderGuardianPet {


    public EntityElderGuardianPet(World world) {
        super(world);
    }

    public EntityElderGuardianPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public boolean isElder() {
        return true;
    }

    @Override
    public void setElder(boolean var1) {

    }
}
