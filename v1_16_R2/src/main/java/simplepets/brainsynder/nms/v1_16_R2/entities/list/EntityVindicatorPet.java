package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R2.*;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityVindicatorPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.branch.EntityIllagerAbstractPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityVindicator}
 */
@Size(width = 0.6F, length = 1.95F)
public class EntityVindicatorPet extends EntityIllagerAbstractPet implements IEntityVindicatorPet {
    private boolean johnny = false;

    public EntityVindicatorPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
    public EntityVindicatorPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("johnny", johnny);
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("johnny")) setJohnny(object.getBoolean("johnny"));
        super.applyCompound(object);
    }

    @Override
    public boolean isJohnny() {
        return johnny;
    }

    @Override
    public void setJohnny(boolean var) {
        johnny = var;
        if (var) {
            setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
        } else {
            setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.AIR));
        }
    }
}
