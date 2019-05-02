package simplepets.brainsynder.nms.v1_12_R1.entities.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityDonkeyPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_12_R1.entities.branch.EntityHorseChestedAbstractPet;
import simplepets.brainsynder.wrapper.EntityWrapper;

@Size(width = 1.4F, length = 1.6F)
public class EntityDonkeyPet extends EntityHorseChestedAbstractPet implements IEntityDonkeyPet {
    public EntityDonkeyPet(World world, IPet pet) {
        super(world, pet);
    }
    public EntityDonkeyPet(World world) {
        super(world);
    }

    @Override
    public EntityWrapper getPetEntityType() {
        return EntityWrapper.DONKEY;
    }
}
