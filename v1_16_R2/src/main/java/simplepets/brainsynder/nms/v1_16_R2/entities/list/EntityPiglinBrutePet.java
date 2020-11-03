package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.entity.hostile.IEntityPiglinBrutePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.branch.EntityPiglinAbstractPet;


/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityPiglinBrute}
 */
public class EntityPiglinBrutePet extends EntityPiglinAbstractPet implements IEntityPiglinBrutePet {
    public EntityPiglinBrutePet(World world, IPet pet) {
        super(EntityTypes.PIGLIN_BRUTE, world, pet);
    }
    public EntityPiglinBrutePet(World world) {
        super(EntityTypes.PIGLIN_BRUTE, world);
    }
}
