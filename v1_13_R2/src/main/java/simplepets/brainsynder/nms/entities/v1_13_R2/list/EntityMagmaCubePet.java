package simplepets.brainsynder.nms.entities.v1_13_R2.list;

import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityMagmaCubePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.registry.v1_13_R2.Types;

@Size(width = 0.6F, length = 0.6F)
public class EntityMagmaCubePet extends EntitySlimePet implements IEntityMagmaCubePet {
    public EntityMagmaCubePet(World world, IPet pet) {
        super(Types.MAGMA_CUBE, world, pet);
    }
    public EntityMagmaCubePet(World world) {
        super(Types.MAGMA_CUBE, world);
    }
}
