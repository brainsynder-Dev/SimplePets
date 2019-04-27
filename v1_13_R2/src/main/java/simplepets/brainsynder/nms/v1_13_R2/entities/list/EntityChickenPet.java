package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityChickenPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.AgeableEntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_13_R2.EntityChicken}
 */
@Size(width = 0.3F, length = 0.7F)
public class EntityChickenPet extends AgeableEntityPet implements IEntityChickenPet {
    public EntityChickenPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityChickenPet(EntityTypes<?> type, World world) {
        super(type, world);
    }
}
