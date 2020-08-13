package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityCreature;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityZombieHorsePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.branch.EntityHorseChestedAbstractPet;
import simplepets.brainsynder.wrapper.EntityWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityHorseZombie}
 */
@Size(width = 1.4F, length = 1.6F)
public class EntityZombieHorsePet extends EntityHorseChestedAbstractPet implements IEntityZombieHorsePet {
    public EntityZombieHorsePet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
    public EntityZombieHorsePet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    public EntityWrapper getPetEntityType() {
        return EntityWrapper.ZOMBIE_HORSE;
    }
}
