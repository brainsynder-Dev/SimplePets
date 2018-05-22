package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import simple.brainsynder.math.MathUtils;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityWitchPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.EntityPet;

@Size(width = 0.6F, length = 1.9F)
public class EntityWitchPet extends EntityPet implements IEntityWitchPet {
    private static final DataWatcherObject<Boolean> IS_DRINKING;

    static {
        IS_DRINKING = DataWatcher.a(EntityPolarBearPet.class, DataWatcherRegistry.h);
    }

    public EntityWitchPet(World world) {
        super(world);
    }

    public EntityWitchPet(World world, IPet pet) {
        super(world, pet);
    }

    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(IS_DRINKING, Boolean.FALSE);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("drinking", isDrinkingPotion());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("drinking"))
            setDrinkingPotion(object.getBoolean("drinking"));
        super.applyCompound(object);
    }

    public void setDrinkingPotion(boolean drinkingPotion) {
        datawatcher.set(IS_DRINKING, drinkingPotion);
        if (drinkingPotion) {
            ItemStack item = new ItemStack(Material.POTION);
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            meta.setColor(Color.fromRGB(MathUtils.random(0,255), MathUtils.random(0,255), MathUtils.random(0,255)));
            item.setItemMeta(meta);
            ((LivingEntity) getEntity()).getEquipment().setItemInMainHand(item);
        } else {
            ((LivingEntity) getEntity()).getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
        }
    }

    public boolean isDrinkingPotion() {
        return datawatcher.get(IS_DRINKING);
    }
}
