package simplepets.brainsynder.nms.v1_16_R1.entities.list;

import net.minecraft.server.v1_16_R1.EntityCreature;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.World;
import simplepets.brainsynder.api.entity.hostile.IEntityRavagerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R1.entities.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R1.EntityRavager}
 *
 *   I still have some stuff I want to add to the Ravager
 */
public class EntityRavagerPet extends EntityPet implements IEntityRavagerPet {
    public EntityRavagerPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
    public EntityRavagerPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
}
