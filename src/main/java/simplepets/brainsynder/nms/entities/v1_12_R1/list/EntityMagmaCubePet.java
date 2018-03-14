package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntityMagmaCubePet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.utils.Size;


@Size(width = 0.6F, length = 0.6F)
public class EntityMagmaCubePet extends EntitySlimePet implements IEntityMagmaCubePet {
    public EntityMagmaCubePet(World world, IPet pet) {
        super(world, pet);
    }
    public EntityMagmaCubePet(World world) {
        super(world);
    }
}
