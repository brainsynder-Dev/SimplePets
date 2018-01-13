package simplepets.brainsynder.nms.entities.v1_8_R3.list;

import net.minecraft.server.v1_8_R3.World;
import simplepets.brainsynder.api.entity.IFlyablePet;
import simplepets.brainsynder.api.entity.hostile.IEntityGhastPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_8_R3.EntityPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityGhastPet extends EntityPet implements IEntityGhastPet,
        IFlyablePet {
    public EntityGhastPet(World world, IPet pet) {
        super(world, pet);
    }


    public EntityGhastPet(World world) {
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
