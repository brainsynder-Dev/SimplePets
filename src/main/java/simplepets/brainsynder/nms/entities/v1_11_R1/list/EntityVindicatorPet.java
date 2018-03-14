package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.World;
import org.bukkit.entity.LivingEntity;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityVindicatorPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.branch.EntityIllagerAbstractPet;
import simplepets.brainsynder.pet.IPet;

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
