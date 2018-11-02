package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.entity.passive.IEntityCodPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.EntityFishPet;
import simplepets.brainsynder.nms.v1_13_R2.registry.Types;

/**
 * NMS: {@link net.minecraft.server.v1_13_R2.EntitySalmon}
 */
public class EntitySalmonPet extends EntityFishPet implements IEntityCodPet {
    public EntitySalmonPet(World world, IPet pet) {
        super(Types.SALMON, world, pet);
    }

    public EntitySalmonPet(World world) {
        super(Types.SALMON, world);
    }
}
