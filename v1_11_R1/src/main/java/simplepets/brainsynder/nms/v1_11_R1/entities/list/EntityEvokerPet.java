package simplepets.brainsynder.nms.v1_11_R1.entities.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.api.entity.hostile.IEntityEvokerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_11_R1.entities.branch.EntityIllagerWizardPet;

public class EntityEvokerPet extends EntityIllagerWizardPet implements IEntityEvokerPet {

    public EntityEvokerPet(World world) {
        super(world);
    }

    public EntityEvokerPet(World world, IPet pet) {
        super(world, pet);
    }

}
