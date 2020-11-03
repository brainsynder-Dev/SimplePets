package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntitySquidPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.EntityPet;


/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntitySquid}
 */
@Size(width = 0.95F, length = 0.95F)
public class EntitySquidPet extends EntityPet implements IEntitySquidPet {
    public EntitySquidPet(World world, IPet pet) {
        super(EntityTypes.SQUID, world, pet);
    }
    public EntitySquidPet(World world) {
        super(EntityTypes.SQUID, world);
    }
}
