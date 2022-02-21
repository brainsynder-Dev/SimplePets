package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import simplepets.brainsynder.api.entity.hostile.IEntityVindicatorPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.branch.EntityIllagerAbstractPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityVindicator}
 */
public class EntityVindicatorPet extends EntityIllagerAbstractPet implements IEntityVindicatorPet {
    private boolean johnny = false;

    public EntityVindicatorPet(PetType type, PetUser user) {
        super(EntityType.VINDICATOR, type, user);
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
            setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
        } else {
            setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.AIR));
        }
    }
}
