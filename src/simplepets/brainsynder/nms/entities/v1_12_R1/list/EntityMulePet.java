package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.api.entity.passive.IEntityMulePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.branch.EntityHorseChestedAbstractPet;
import simplepets.brainsynder.utils.Size;
import simplepets.brainsynder.wrapper.EntityWrapper;

@Size(width = 1.4F, length = 1.6F)
public class EntityMulePet extends EntityHorseChestedAbstractPet implements IEntityMulePet {
    public EntityMulePet(World world, IPet pet) {
        super(world, pet);
    }
    public EntityMulePet(World world) {
        super(world);
    }

    @Override
    public EntityWrapper getEntityType() {
        return EntityWrapper.MULE;
    }
}
