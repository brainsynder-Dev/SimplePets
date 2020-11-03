package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.entity.hostile.IEntityRavagerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityRavager}
 *
 *   I still have some stuff I want to add to the Ravager
 */
public class EntityRavagerPet extends EntityPet implements IEntityRavagerPet {
    public EntityRavagerPet(World world) {
        super(EntityTypes.RAVAGER, world);
    }
    public EntityRavagerPet(World world, IPet pet) {
        super(EntityTypes.RAVAGER, world, pet);
    }
}
