package simplepets.brainsynder.nms.v1_16_R3.entities.branch;

import net.minecraft.server.v1_16_R3.EntityCreature;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;
import simplepets.brainsynder.api.pet.IPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityIllagerAbstract}
 */
public abstract class EntityIllagerAbstractPet extends EntityRaiderPet {
    public EntityIllagerAbstractPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    public EntityIllagerAbstractPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
}
