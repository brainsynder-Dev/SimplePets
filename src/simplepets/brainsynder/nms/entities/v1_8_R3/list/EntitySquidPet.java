package simplepets.brainsynder.nms.entities.v1_8_R3.list;

import net.minecraft.server.v1_8_R3.World;
import simplepets.brainsynder.api.entity.passive.IEntitySquidPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_8_R3.EntityPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntitySquidPet extends EntityPet implements IEntitySquidPet {
    public EntitySquidPet(World world, IPet pet) {
        super(world, pet);
    }

    public EntitySquidPet(World world) {
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
