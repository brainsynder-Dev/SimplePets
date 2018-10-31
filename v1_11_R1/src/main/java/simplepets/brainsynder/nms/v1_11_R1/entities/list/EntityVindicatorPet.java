package simplepets.brainsynder.nms.v1_11_R1.entities.list;

import net.minecraft.server.v1_11_R1.World;
import org.bukkit.entity.LivingEntity;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.entity.hostile.IEntityVindicatorPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_11_R1.entities.branch.EntityIllagerAbstractPet;

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
        object.setBoolean("Johnny", johhny);
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Johnny")) setJohnny(object.getBoolean("Johnny"));
        super.applyCompound(object);
    }

    @Override
    public boolean isJohnny() {
        return johhny;
    }

    @Override
    public void setJohnny(boolean var) {
        johhny = var;
        if (var) {
            a(1, var);
            ((LivingEntity) getEntity()).getEquipment().setItemInMainHand(new org.bukkit.inventory.ItemStack(org.bukkit.Material.IRON_AXE));
        } else {
            a(1, var);
            ((LivingEntity) getEntity()).getEquipment().setItemInMainHand(new org.bukkit.inventory.ItemStack(org.bukkit.Material.AIR));
        }
    }
}
