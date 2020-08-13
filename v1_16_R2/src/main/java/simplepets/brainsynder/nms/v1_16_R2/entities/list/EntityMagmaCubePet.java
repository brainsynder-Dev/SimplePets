package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityCreature;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityMagmaCubePet;
import simplepets.brainsynder.api.pet.IPet;


/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityMagmaCube}
 */
@Size(width = 0.6F, length = 0.6F)
public class EntityMagmaCubePet extends EntitySlimePet implements IEntityMagmaCubePet {
    public EntityMagmaCubePet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityMagmaCubePet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
}
