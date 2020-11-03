package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityMulePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.branch.EntityHorseChestedAbstractPet;
import simplepets.brainsynder.wrapper.EntityWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityHorseMule}
 */
@Size(width = 1.4F, length = 1.6F)
public class EntityMulePet extends EntityHorseChestedAbstractPet implements IEntityMulePet {
    public EntityMulePet(World world, IPet pet) {
        super(EntityTypes.MULE, world, pet);
    }
    public EntityMulePet(World world) {
        super(EntityTypes.MULE, world);
    }

    @Override
    public EntityWrapper getPetEntityType() {
        return EntityWrapper.MULE;
    }
}
