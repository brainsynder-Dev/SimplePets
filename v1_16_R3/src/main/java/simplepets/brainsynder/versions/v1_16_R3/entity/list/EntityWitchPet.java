package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.math.MathUtils;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import simplepets.brainsynder.api.entity.hostile.IEntityWitchPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.branch.EntityRaiderPet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityWitch}
 */
public class EntityWitchPet extends EntityRaiderPet implements IEntityWitchPet {
    private static final DataWatcherObject<Boolean> IS_DRINKING;

    public EntityWitchPet(PetType type, PetUser user) {
        super(EntityTypes.WITCH, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(IS_DRINKING, Boolean.FALSE);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("potion", isDrinkingPotion());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("potion"))
            setDrinkingPotion(object.getBoolean("potion"));
        super.applyCompound(object);
    }

    @Override
    public void setDrinkingPotion(boolean flag) {
        datawatcher.set(IS_DRINKING, flag);
        if (flag) {
            ItemStack item = new ItemStack(Material.POTION);
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            meta.setColor(Color.fromRGB(MathUtils.random(0,255), MathUtils.random(0,255), MathUtils.random(0,255)));
            item.setItemMeta(meta);
            setSlot(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(item));
        } else {
            setSlot(EnumItemSlot.MAINHAND, Items.AIR.createItemStack());
        }
    }

    @Override
    public boolean isDrinkingPotion() {
        return datawatcher.get(IS_DRINKING);
    }

    static {
        IS_DRINKING = DataWatcher.a(EntityWitchPet.class, DataWatcherWrapper.BOOLEAN);
    }
}
