package simplepets.brainsynder.nms.entities.v1_13_R1.list;

import net.minecraft.server.v1_13_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntitySquidPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_13_R1.EntityPet;
import simplepets.brainsynder.nms.registry.v1_13_R1.Types;

@Size(width = 0.95F, length = 0.95F)
public class EntitySquidPet extends EntityPet implements IEntitySquidPet {
    public EntitySquidPet(World world, IPet pet) {
        super(Types.SQUID, world, pet);
    }
    public EntitySquidPet(World world) {
        super(Types.SQUID, world);
    }
}
