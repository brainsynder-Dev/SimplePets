package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityDonkeyPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.branch.EntityHorseChestedAbstractPet;
import simplepets.brainsynder.nms.v1_13_R2.registry.Types;
import simplepets.brainsynder.wrapper.EntityWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_13_R2.EntityDonkey}
 */
@Size(width = 1.4F, length = 1.6F)
public class EntityDonkeyPet extends EntityHorseChestedAbstractPet implements IEntityDonkeyPet {
    public EntityDonkeyPet(World world, IPet pet) {
        super(Types.DONKEY, world, pet);
    }
    public EntityDonkeyPet(World world) {
        super(Types.DONKEY, world);
    }

    @Override
    public EntityWrapper getEntityType() {
        return EntityWrapper.DONKEY;
    }
}
