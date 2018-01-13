package simplepets.brainsynder.nms.entities.v1_8_R3.list;

import net.minecraft.server.v1_8_R3.World;
import simplepets.brainsynder.api.entity.passive.IEntityIronGolemPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_8_R3.EntityPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityIronGolemPet extends EntityPet implements IEntityIronGolemPet {
    public EntityIronGolemPet(World world, IPet pet) {
        super(world, pet);
    }


    public EntityIronGolemPet(World world) {
        super(world);
    }

    @Override
    protected String getIdleSound() {
        return null;
    }

    @Override
    protected String getDeathSound() {
        return null;
    }
}
