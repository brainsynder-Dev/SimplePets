package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityParchedPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPetOverride;

/**
 * NMS: {@link net.minecraft.world.entity.monster.skeleton.Parched}
 */
@SupportedVersion(version = ServerVersion.v1_21_11)
public class EntityParchedPet extends EntityPetOverride implements IEntityParchedPet {
    public EntityParchedPet(PetType type, PetUser user) {
        super(EntityType.PARCHED, type, user);
    }
}
