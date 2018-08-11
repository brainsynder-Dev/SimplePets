package simplepets.brainsynder.nms.entities.v1_13_R1.list;

import net.minecraft.server.v1_13_R1.*;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftCreature;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import simple.brainsynder.math.MathUtils;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityWitchPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_13_R1.EntityPet;
import simplepets.brainsynder.nms.registry.v1_13_R1.Types;

@Size(width = 0.6F, length = 1.9F)
public class EntityWitchPet extends EntityPet implements IEntityWitchPet {
    private static final DataWatcherObject<Boolean> IS_DRINKING;

    static {
        IS_DRINKING = DataWatcher.a(EntityPolarBearPet.class, DataWatcherRegistry.i);
    }

    public EntityWitchPet(World world) {
        super(Types.WITCH, world);
    }

    public EntityWitchPet(World world, IPet pet) {
        super(Types.WITCH, world, pet);
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

    @Override
    public void setDrinkingPotion(boolean drinkingPotion) {
        if (drinkingPotion) {
            ItemStack item = new ItemStack(Material.POTION);
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            meta.setColor(Color.fromRGB(MathUtils.random(0,255), MathUtils.random(0,255), MathUtils.random(0,255)));
            item.setItemMeta(meta);
            ((CraftCreature) getBukkitEntity()).getEquipment().setItemInMainHand(item);
        } else {
            ((CraftCreature) getBukkitEntity()).getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
        }
        datawatcher.set(IS_DRINKING, drinkingPotion);
    }

    @Override
    public boolean isDrinkingPotion() {
        return datawatcher.get(IS_DRINKING);
    }
}
