package simplepets.brainsynder.nms.v1_16_R3.entities.list;

import net.minecraft.server.v1_16_R3.EntityCreature;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;
import simplepets.brainsynder.api.entity.passive.IEntityCodPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R3.entities.EntityFishPet;


/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityCod}
 */
public class EntityCodPet extends EntityFishPet implements IEntityCodPet {
    public EntityCodPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    public EntityCodPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
}
