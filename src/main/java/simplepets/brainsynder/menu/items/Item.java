package simplepets.brainsynder.menu.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.api.SkullMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.files.base.JSONFile;

import java.io.File;

public class Item extends JSONFile {
    private boolean isCustom = false;
    public static final String DISPLAY_NAME = "DisplayName",
            CUSTOM_SKULL = "CustomSkull",
            AMOUNT = "AMOUNT",
            DATA = "Data",
            LORE = "DisplayLore",
            MATERIAL = "Material",
            LORE_ENABLED = "DisplayLoreEnabled",
            SKULL_OWNER = "SkullOwner",
            TEXTURE_URL = "TextureURL",
            ENABLED = "Enabled",
            COMMANDS = "CommandsOnClick"
    ;

    public Item(File file) {
        super(file);
    }

    public static File getLocation (PetCore core, Class<? extends Item> clazz) {
        File folder = new File(core.getDataFolder().toString() + "/Items/");
        return new File(folder, clazz.getSimpleName() + ".json");
    }

    public void onClick(PetOwner owner, CustomInventory inventory) {}

    public String getValueName() {
        return ChatColor.stripColor(getString(DISPLAY_NAME, true));
    }

    public ItemStack getItem() {
        if (getBoolean(getObject(CUSTOM_SKULL), ENABLED)) {
            SkullMaker maker = new SkullMaker();
            maker.setAmount(getInteger(AMOUNT));
            maker.setName(getString(DISPLAY_NAME, true));
            maker.setSkullOwner(getString(getObject(CUSTOM_SKULL), SKULL_OWNER));
            maker.setOwner(getString(getObject(CUSTOM_SKULL), TEXTURE_URL));
            if (getBoolean(LORE_ENABLED))
                for (Object s : getArray(LORE)) {
                    maker.addLoreLine(String.valueOf(s));
                }
            return maker.create();
        } else {
            ItemMaker maker = new ItemMaker(Material.getMaterial(getString(MATERIAL, true)), (byte) getInteger(DATA));
            maker.setAmount(getInteger(AMOUNT));
            maker.setName(getString(DISPLAY_NAME, true));
            if (getBoolean(LORE_ENABLED))
                for (Object s : getArray(LORE)) {
                    maker.addLoreLine(String.valueOf(s));
                }
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

    public String namespace () {
        if (hasKey("namespace")) return getString("namespace", false);
        return getClass().getSimpleName().toLowerCase();
    }

    public boolean isEnabled () {
        if (hasKey(ENABLED)) return getBoolean(ENABLED);
        return true;
    }
}
