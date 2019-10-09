package simplepets.brainsynder.nms.v1_14_R1.entities.branch;

import net.minecraft.server.v1_14_R1.EntityCreature;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.World;
import simplepets.brainsynder.api.pet.IPet;

/**
 * NMS: {@link net.minecraft.server.v1_14_R1.EntityIllagerAbstract}
 */
public abstract class EntityIllagerAbstractPet extends EntityRaiderPet {
    public EntityIllagerAbstractPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    public EntityIllagerAbstractPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
}
