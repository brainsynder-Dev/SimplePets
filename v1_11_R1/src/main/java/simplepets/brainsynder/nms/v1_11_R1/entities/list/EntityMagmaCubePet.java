package simplepets.brainsynder.nms.v1_11_R1.entities.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.api.entity.hostile.IEntityMagmaCubePet;
import simplepets.brainsynder.api.pet.IPet;

public class EntityMagmaCubePet extends EntitySlimePet implements IEntityMagmaCubePet {
    public EntityMagmaCubePet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityMagmaCubePet(World world) {
        super(world);
    }
}
