package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.api.entity.passive.IEntityMulePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.branch.EntityHorseChestedAbstractPet;

public class EntityMulePet extends EntityHorseChestedAbstractPet implements IEntityMulePet {
    public EntityMulePet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityMulePet(World world) {
        super(world);
    }
}
