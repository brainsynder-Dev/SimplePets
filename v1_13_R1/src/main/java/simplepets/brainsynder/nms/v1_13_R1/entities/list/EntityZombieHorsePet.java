package simplepets.brainsynder.nms.v1_13_R1.entities.list;

import net.minecraft.server.v1_13_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityZombieHorsePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R1.entities.branch.EntityHorseChestedAbstractPet;
import simplepets.brainsynder.nms.v1_13_R1.registry.Types;
import simplepets.brainsynder.wrapper.EntityWrapper;

@Size(width = 1.4F, length = 1.6F)
public class EntityZombieHorsePet extends EntityHorseChestedAbstractPet implements IEntityZombieHorsePet {
    public EntityZombieHorsePet(World world) {
        super(Types.ZOMBIE_HORSE, world);
    }
    public EntityZombieHorsePet(World world, IPet pet) {
        super(Types.ZOMBIE_HORSE, world, pet);
    }

    @Override
    public EntityWrapper getEntityType() {
        return EntityWrapper.ZOMBIE_HORSE;
    }
}
