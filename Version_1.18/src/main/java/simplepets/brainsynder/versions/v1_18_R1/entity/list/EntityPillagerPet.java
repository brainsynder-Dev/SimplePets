package simplepets.brainsynder.versions.v1_18_R1.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityPillagerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_18_R1.entity.branch.EntityIllagerAbstractPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityPillager}
 */
public class EntityPillagerPet extends EntityIllagerAbstractPet implements IEntityPillagerPet {
    public EntityPillagerPet(PetType type, PetUser user) {
        super(EntityType.PILLAGER, type, user);
    }
}
