package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.entity.passive.IEntityCodPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.EntityFishPet;


/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityCod}
 */
public class EntityCodPet extends EntityFishPet implements IEntityCodPet {
    public EntityCodPet(World world, IPet pet) {
        super(EntityTypes.COD, world, pet);
    }

    public EntityCodPet(World world) {
        super(EntityTypes.COD, world);
    }
}
