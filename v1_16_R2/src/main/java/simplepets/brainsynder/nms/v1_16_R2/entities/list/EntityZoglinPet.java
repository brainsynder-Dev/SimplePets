package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityZoglinPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.AgeableEntityPet;


/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityZoglin}
 */
@Size(width = 1.3964844F, length = 1.4F)
public class EntityZoglinPet extends AgeableEntityPet implements IEntityZoglinPet {
    public EntityZoglinPet(World world, IPet pet) {
        super(EntityTypes.ZOGLIN, world, pet);
    }
    public EntityZoglinPet(World world) {
        super(EntityTypes.ZOGLIN, world);
    }
}
