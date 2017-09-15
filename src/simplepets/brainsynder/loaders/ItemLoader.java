package simplepets.brainsynder.loaders;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.api.SkullMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.files.JSONFile;

import java.io.File;

public abstract class ItemLoader extends JSONFile {
    public ItemLoader(File file) {
        super(file);
    }

    public int getSlot() {
        int i = getInteger("Slot-Number");
        return (i - 1);
    }

    public String getValueName() {
        return ChatColor.stripColor(getString("DisplayName", true));
    }

    public ItemStack getItem() {
        if (getBoolean(getObject("CustomSkull"), "Enabled")) {
            SkullMaker maker = new SkullMaker();
            maker.setAmount(getInteger("MaterialAmount"));
            maker.setName(getString("DisplayName", true));
            maker.setSkullOwner(getString(getObject("CustomSkull"), "SkullOwner"));
            maker.setOwner(getString(getObject("CustomSkull"), "TextureURL"));
            if (getBoolean("DisplayLoreEnabled"))
                for (Object s : getArray("DisplayLore")) {
                    maker.addLoreLine(String.valueOf(s));
                }
            return maker.create();
        } else {
            String mat = getString("MaterialName", true);
            if (mat.equalsIgnoreCase("air")) {
                String name = getClass().getSimpleName();
                name = name.replace("Loader", "Item.json");
                PetCore.get().debug(2, "SimplePets: MaterialName can now be air for the item " + name);
                mat = Material.STONE.name();
                set("MaterialName", defaults.get("MaterialName"));
                save();
            }
            ItemMaker maker = new ItemMaker(Material.valueOf(mat), (byte) getInteger("MaterialData"));
            maker.setAmount(getInteger("MaterialAmount"));
            maker.setName(getString("DisplayName", true));
            if (getBoolean("DisplayLoreEnabled"))
                for (Object s : getArray("DisplayLore")) {
                    maker.addLoreLine(String.valueOf(s));
                }
            if (getBoolean("FakeEnchanted"))
                maker.enchant();
            maker.setFlags(
                    ItemFlag.HIDE_ATTRIBUTES,
                    ItemFlag.HIDE_DESTROYS,
                    ItemFlag.HIDE_ENCHANTS,
                    ItemFlag.HIDE_PLACED_ON,
                    ItemFlag.HIDE_POTION_EFFECTS,
                    ItemFlag.HIDE_UNBREAKABLE
            );
            return maker.create();
        }
    }
}
