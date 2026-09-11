package simplepets.brainsynder.nms.entity.list;

import simplepets.brainsynder.api.entity.passive.IEntityMulePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.branch.EntityAbstractChestedPet;

/**
 * NMS: {@link net.minecraft.world.entity.animal.horse.Mule}
 */
public class EntityMulePet extends EntityAbstractChestedPet implements IEntityMulePet {
    public EntityMulePet(PetType type, PetUser user) {
        super(EntitySelector.MULE, type, user);
    }
}
