package simplepets.brainsynder.nms.entities.v1_13_R2.list;

import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityEvokerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_13_R2.branch.EntityIllagerWizardPet;
import simplepets.brainsynder.nms.registry.v1_13_R2.Types;

@Size(width = 0.6F, length = 1.95F)
public class EntityEvokerPet extends EntityIllagerWizardPet implements IEntityEvokerPet {
    public EntityEvokerPet(World world) {
        super(Types.EVOKER, world);
    }
    public EntityEvokerPet(World world, IPet pet) {
        super(Types.EVOKER, world, pet);
    }
}
