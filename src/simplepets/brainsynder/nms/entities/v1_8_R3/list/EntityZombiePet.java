package simplepets.brainsynder.nms.entities.v1_8_R3.list;

import net.minecraft.server.v1_8_R3.World;
import simplepets.brainsynder.nms.entities.type.IEntityZombiePet;
import simplepets.brainsynder.nms.entities.v1_8_R3.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityZombiePet extends AgeableEntityPet implements IEntityZombiePet {
    public EntityZombiePet(World world, IPet pet) {
        super(world, pet);
        this.datawatcher.a(13, (byte) 0);
    }

    public EntityZombiePet(World world) {
        super(world);
    }

    public void setVillager(boolean flag) {
        this.datawatcher.watch(13, (byte) (flag ? 1 : 0));
    }
}
