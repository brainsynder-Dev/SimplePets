package simplepets.brainsynder.nms.v1_15_R1.entities.list;

import net.minecraft.server.v1_15_R1.EntityCreature;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.World;
import simplepets.brainsynder.api.entity.passive.IEntityCodPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_15_R1.entities.EntityFishPet;

/**
 * NMS: {@link net.minecraft.server.v1_15_R1.EntitySalmon}
 */
public class EntitySalmonPet extends EntityFishPet implements IEntityCodPet {
    public EntitySalmonPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    public EntitySalmonPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
}
