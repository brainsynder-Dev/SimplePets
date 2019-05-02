package simplepets.brainsynder.nms.v1_14_R1.entities.list;

import net.minecraft.server.v1_14_R1.EntityCreature;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntitySquidPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_14_R1.entities.EntityPet;


/**
 * NMS: {@link net.minecraft.server.v1_14_R1.EntitySquid}
 */
@Size(width = 0.95F, length = 0.95F)
public class EntitySquidPet extends EntityPet implements IEntitySquidPet {
    public EntitySquidPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntitySquidPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
}
