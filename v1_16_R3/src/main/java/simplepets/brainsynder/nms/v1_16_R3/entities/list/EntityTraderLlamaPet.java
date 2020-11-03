package simplepets.brainsynder.nms.v1_16_R3.entities.list;

import net.minecraft.server.v1_16_R3.EntityCreature;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityTraderLlamaPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.wrapper.EntityWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityLlamaTrader}
 */
@Size(width = 0.9F, length = 1.87F)
public class EntityTraderLlamaPet extends EntityLlamaPet implements IEntityTraderLlamaPet {
    public EntityTraderLlamaPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
    public EntityTraderLlamaPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    public EntityWrapper getPetEntityType() {
        return EntityWrapper.TRADER_LLAMA;
    }
}
