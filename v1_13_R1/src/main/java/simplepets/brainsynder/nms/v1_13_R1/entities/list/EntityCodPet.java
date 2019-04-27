package simplepets.brainsynder.nms.v1_13_R1.entities.list;

import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.World;
import simplepets.brainsynder.api.entity.passive.IEntityCodPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R1.entities.EntityFishPet;

public class EntityCodPet extends EntityFishPet implements IEntityCodPet {
    public EntityCodPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }

    public EntityCodPet(EntityTypes<?> type, World world) {
        super(type, world);
    }
}
