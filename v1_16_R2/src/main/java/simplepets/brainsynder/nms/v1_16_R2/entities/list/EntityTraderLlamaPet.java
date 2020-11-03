package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityTraderLlamaPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.wrapper.EntityWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityLlamaTrader}
 */
@Size(width = 0.9F, length = 1.87F)
public class EntityTraderLlamaPet extends EntityLlamaPet implements IEntityTraderLlamaPet {
    public EntityTraderLlamaPet(World world) {
        super(EntityTypes.TRADER_LLAMA, world);
    }
    public EntityTraderLlamaPet(World world, IPet pet) {
        super(EntityTypes.TRADER_LLAMA, world, pet);
    }

    @Override
    public EntityWrapper getPetEntityType() {
        return EntityWrapper.TRADER_LLAMA;
    }
}
