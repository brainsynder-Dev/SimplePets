package simplepets.brainsynder.nms.entities.v1_13_R1.list;

import net.minecraft.server.v1_13_R1.World;
import org.bukkit.entity.LivingEntity;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityVindicatorPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_13_R1.branch.EntityIllagerAbstractPet;
import simplepets.brainsynder.nms.registry.v1_13_R1.Types;

@Size(width = 0.6F, length = 1.95F)
public class EntityVindicatorPet extends EntityIllagerAbstractPet implements IEntityVindicatorPet {
    private boolean johnny = false;

    public EntityVindicatorPet(World world) {
        super(Types.VINDICATOR, world);
    }
    public EntityVindicatorPet(World world, IPet pet) {
        super(Types.VINDICATOR, world, pet);
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
        a(1, var);
        if (var) {
            ((LivingEntity) getEntity()).getEquipment().setItemInMainHand(new org.bukkit.inventory.ItemStack(org.bukkit.Material.IRON_AXE));
        } else {
            ((LivingEntity) getEntity()).getEquipment().setItemInMainHand(new org.bukkit.inventory.ItemStack(org.bukkit.Material.AIR));
        }
    }
}
