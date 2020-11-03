package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityChickenPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.AgeableEntityPet;


/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityChicken}
 */
@Size(width = 0.3F, length = 0.7F)
public class EntityChickenPet extends AgeableEntityPet implements IEntityChickenPet {
    public EntityChickenPet(World world, IPet pet) {
        super(EntityTypes.CHICKEN, world, pet);
    }
    public EntityChickenPet(World world) {
        super(EntityTypes.CHICKEN, world);
    }
}
