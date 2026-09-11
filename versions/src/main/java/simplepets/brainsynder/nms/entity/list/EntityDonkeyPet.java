package simplepets.brainsynder.nms.entity.list;

import simplepets.brainsynder.api.entity.passive.IEntityDonkeyPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.branch.EntityAbstractChestedPet;

/**
 * NMS: {@link net.minecraft.world.entity.animal.horse.Donkey}
 */
public class EntityDonkeyPet extends EntityAbstractChestedPet implements IEntityDonkeyPet {
    public EntityDonkeyPet(PetType type, PetUser user) {
        super(EntitySelector.DONKEY, type, user);
    }
}
