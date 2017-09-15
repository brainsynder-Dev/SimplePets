package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntityElderGuardianPet;
import simplepets.brainsynder.pet.IPet;

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
