package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityNautilusPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.branch.EntityNautilusAbstractPet;

/**
 * NMS: {@link net.minecraft.world.entity.animal.nautilus.Nautilus }
 */
@SupportedVersion(version = ServerVersion.v1_21_11)
public class EntityNautilusPet extends EntityNautilusAbstractPet implements IEntityNautilusPet {
    public EntityNautilusPet(PetType type, PetUser user) {
        super(EntityType.NAUTILUS, type, user);
    }
}
