package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityEvokerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.branch.EntityIllagerWizardPet;

@Size(width = 0.6F, length = 1.95F)
public class EntityEvokerPet extends EntityIllagerWizardPet implements IEntityEvokerPet {
    public EntityEvokerPet(World world) {
        super(world);
    }
    public EntityEvokerPet(World world, IPet pet) {
        super(world, pet);
    }
}
