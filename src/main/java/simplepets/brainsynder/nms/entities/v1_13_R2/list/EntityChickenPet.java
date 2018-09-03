package simplepets.brainsynder.nms.entities.v1_13_R2.list;

import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityChickenPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_13_R2.AgeableEntityPet;
import simplepets.brainsynder.nms.registry.v1_13_R2.Types;

@Size(width = 0.3F, length = 0.7F)
public class EntityChickenPet extends AgeableEntityPet implements IEntityChickenPet {
    public EntityChickenPet(World world, IPet pet) {
        super(Types.CHICKEN, world, pet);
    }
    public EntityChickenPet(World world) {
        super(Types.CHICKEN, world);
    }
}
