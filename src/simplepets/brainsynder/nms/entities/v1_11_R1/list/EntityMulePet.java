package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntityMulePet;
import simplepets.brainsynder.nms.entities.v1_11_R1.branch.EntityHorseChestedAbstractPet;
import simplepets.brainsynder.pet.IPet;

public class EntityMulePet extends EntityHorseChestedAbstractPet implements IEntityMulePet {
    public EntityMulePet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityMulePet(World world) {
        super(world);
    }
}
