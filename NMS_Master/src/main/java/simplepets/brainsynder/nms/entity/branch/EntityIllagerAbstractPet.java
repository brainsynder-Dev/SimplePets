package simplepets.brainsynder.nms.entity.branch;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

public abstract class EntityIllagerAbstractPet extends EntityRaiderPet {
    public EntityIllagerAbstractPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }
}