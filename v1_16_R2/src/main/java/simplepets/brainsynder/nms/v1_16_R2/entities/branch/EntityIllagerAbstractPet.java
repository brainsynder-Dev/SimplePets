package simplepets.brainsynder.nms.v1_16_R2.entities.branch;

import net.minecraft.server.v1_16_R2.EntityInsentient;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.pet.IPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityIllagerAbstract}
 */
public abstract class EntityIllagerAbstractPet extends EntityRaiderPet {
    public EntityIllagerAbstractPet(EntityTypes<? extends EntityInsentient> type, World world) {
        super(type, world);
    }

    public EntityIllagerAbstractPet(EntityTypes<? extends EntityInsentient> type, World world, IPet pet) {
        super(type, world, pet);
    }
}
