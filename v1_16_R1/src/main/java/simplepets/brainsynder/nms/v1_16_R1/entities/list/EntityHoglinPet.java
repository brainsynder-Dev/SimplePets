package simplepets.brainsynder.nms.v1_16_R1.entities.list;

import net.minecraft.server.v1_16_R1.EntityCreature;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityHoglinPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R1.entities.AgeableEntityPet;


/**
 * NMS: {@link net.minecraft.server.v1_16_R1.EntityHoglin}
 */
@Size(width = 1.3964844F, length = 1.4F)
public class EntityHoglinPet extends AgeableEntityPet implements IEntityHoglinPet {
    public EntityHoglinPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityHoglinPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
}
