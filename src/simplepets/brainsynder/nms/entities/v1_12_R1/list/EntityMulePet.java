package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntityMulePet;
import simplepets.brainsynder.nms.entities.v1_12_R1.branch.EntityHorseChestedAbstractPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.wrapper.EntityWrapper;

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
