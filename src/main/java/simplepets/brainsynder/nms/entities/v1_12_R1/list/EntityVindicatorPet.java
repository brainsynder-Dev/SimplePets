package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import org.bukkit.entity.LivingEntity;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityVindicatorPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.branch.EntityIllagerAbstractPet;

@Size(width = 0.6F, length = 1.95F)
public class EntityVindicatorPet extends EntityIllagerAbstractPet implements IEntityVindicatorPet {
    boolean johhny = false;

    public EntityVindicatorPet(World world) {
        super(world);
    }
    public EntityVindicatorPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("johnny", johhny);
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("johnny")) setJohnny(object.getBoolean("johnny"));
        super.applyCompound(object);
    }

    @Override
    public boolean isJohnny() {
        return johhny;
    }

    @Override
    public void setJohnny(boolean var) {
        johhny = var;
        a(1, var);
        if (var) {
            ((LivingEntity) getEntity()).getEquipment().setItemInMainHand(new org.bukkit.inventory.ItemStack(org.bukkit.Material.IRON_AXE));
        } else {
            ((LivingEntity) getEntity()).getEquipment().setItemInMainHand(new org.bukkit.inventory.ItemStack(org.bukkit.Material.AIR));
        }
    }
}
