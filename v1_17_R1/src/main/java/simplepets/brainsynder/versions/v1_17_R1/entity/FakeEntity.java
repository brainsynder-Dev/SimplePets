package simplepets.brainsynder.versions.v1_17_R1.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

// This is only used by the EntityUtils class
public class FakeEntity extends LivingEntity {
    public FakeEntity() {
        super(EntityType.SILVERFISH, null);
    }

    // Required methods for the LivingEntity class
    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return null;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return null;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }
}