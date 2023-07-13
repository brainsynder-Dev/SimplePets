package simplepets.brainsynder.menu.inventory;

import com.google.common.collect.Lists;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.utils.Colorize;
import lib.brainsynder.utils.ListPager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.addon.AddonCloudData;
import simplepets.brainsynder.addon.PetModule;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.inventory.handler.InventoryType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.managers.AddonManager;
import simplepets.brainsynder.managers.ItemManager;
import simplepets.brainsynder.menu.inventory.holders.AddonHolder;
import simplepets.brainsynder.menu.items.list.Air;
import simplepets.brainsynder.utils.Keys;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public class AddonMenu extends CustomInventory {
    private Map<String, List<AddonCloudData>> addonCache;
    private Map<String, ListPager<ItemBuilder>> pagerMap;
    private Map<String, Boolean> installerMap;

    public AddonMenu(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        addonCache = new HashMap<>();
        pagerMap = new HashMap<>();
        installerMap = new HashMap<>();

        setDefault("_COMMENT_", "This menu is only viewed by people who have permission to it (Not recommended for regular users to have access to)");
        setDefault("size", 54);
        setDefault("title_comment", "The title of the GUI can support regular color codes '&c' and HEX color codes '&#FFFFFF'");
        setDefault("title", "&#de9790[] &#b35349SimplePets Addon Modules");


        Map<Integer, String> object = new HashMap<>();
        Arrays.asList(
                11, 12, 13, 14, 15, 16, 17,
                20, 21, 22, 23, 24, 25, 26,
                29, 30, 31, 32, 33, 34, 35,
                38, 39, 40, 41, 42, 43, 44
        ).forEach(slot -> object.put(slot, "air"));
        object.put(46, "previouspage");
        object.put(50, "installer");
        object.put(54, "nextpage");

        // Makes sure the slot numbers are sorted from low to high
        Set<Map.Entry<Integer, String>> set = object.entrySet();
        List<Map.Entry<Integer, String>> list = new ArrayList<>(set);
        list.sort(Map.Entry.comparingByKey());

        JsonArray array = new JsonArray();
        for (Map.Entry<Integer, String> entry : list) {
            JsonObject json = new JsonObject();
            json.add("slot", entry.getKey());
            json.add("item", entry.getValue());
            array.add(json);
        }

        setDefault("slots", array);
    }

    @Override
    public void onClick(int slot, ItemStack item, Player player) {

    }

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.ADDON_GUI;
    }

    public boolean isInstallerGUI (PetUser user) {
        return installerMap.getOrDefault(user.getPlayer().getName(), false);
    }

    public void open(PetUser user, int page, boolean installer) {
        Player player = user.getPlayer();
        installerMap.put(player.getName(), installer);
        pageSave.put(player.getName(), page);
        Inventory inv = Bukkit.createInventory(new AddonHolder(), getInteger("size", 54), Colorize.translateBungeeHex(getString("title", "&#de9790[] &#b35349SimplePets Addon Modules")));

        int placeHolder = inv.getSize();
        int maxPets = 0;
        while (placeHolder > 0) {
            int slot = (placeHolder - 1);
            if (getSlots().containsKey(slot)) {
                Item item = getSlots().get(slot);
                if (item instanceof Air) {
                    maxPets++;
                } else {
                    inv.setItem(placeHolder - 1, ItemManager.PLACEHOLDER.getItemBuilder().build());
                }
            } else {
                inv.setItem(placeHolder - 1, ItemManager.PLACEHOLDER.getItemBuilder().build());
            }
            placeHolder--;
        }

        getSlots().forEach((slot, item) -> {
            if (item.isEnabled() && item.addItemToInv(user, this))
                inv.setItem(slot, item.getItemBuilder().build());
        });
        ItemBuilder master = new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/ce1f3cc63c73a6a1dde72fe09c6ac5569376d7b61231bb740764368788cbf1fa");

        int finalMaxPets = maxPets;

        if (!installer) {
            List<ItemBuilder> items = Lists.newArrayList();

            AddonManager manager = PetCore.getInstance().getAddonManager();

            for (PetModule module : manager.getLoadedAddons()) {
                String name = module.getNamespace().namespace();
                ItemBuilder builder = ItemBuilder.fromItem(module.getAddonIcon());
                builder.addLore("&r ", "&7Enabled: " + (module.isEnabled() ? "&atrue" : "&cfalse"));
                builder.handleMeta(ItemMeta.class, itemMeta -> {
                    PersistentDataContainer container = itemMeta.getPersistentDataContainer();
                    container.set(Keys.MODULE_NAME, PersistentDataType.STRING, name);
                    container.set(Keys.ADDON_NAME, PersistentDataType.STRING, module.getLocalData().getName());
                    return itemMeta;
                });
                items.add(builder);
            }

            ListPager<ItemBuilder> pages = new ListPager<>(finalMaxPets, items);
            pagerMap.put(player.getName(), pages);

            if (!pages.isEmpty()) {
                if (pages.exists(page)) {
                    pages.getPage(page).forEach(builder1 -> inv.addItem(builder1.build()));
                } else {
                    SimplePets.getDebugLogger().debug(DebugLevel.WARNING, "Page does not exist (Page " + page + " / " + pages.totalPages() + ")");
                }

            }

            if (ConfigOption.INSTANCE.MISC_TOGGLES_CLEAR_ALL_PLACEHOLDERS.getValue())
                inv.remove(ItemManager.PLACEHOLDER.getItemBuilder().build());

            player.openInventory(inv);
            return;
        }
        handleFetch(player.getName(), addons -> {
            List<ItemBuilder> items = Lists.newArrayList();

            AddonManager manager = PetCore.getInstance().getAddonManager();

            for (AddonCloudData data : addons) {
                String name = data.getName();
                if (!manager.fetchAddon(name).isPresent())  {
                    ItemBuilder builder = master.clone();
                    builder.withName(Colorize.fetchColor("e1eb5b") + name);
                    List<String> description = Lists.newArrayList();
                    if (!data.getDescription().isEmpty())
                        data.getDescription().forEach(s -> description.add(ChatColor.GRAY + s));
                    description.add("&r ");
                    description.add("&7Modrinth ID: &e" + data.getId());
                    description.add("&7Author: &e" + data.getAuthor());
                    description.add("&7Version: &e" + data.getVersion());
                    description.add("&7Last Update: &e" + data.getLastUpdated());
                    description.add("&7Total Downloads: &e" + data.getDownloadCount());

                    builder.withLore(description).addLore("&r ", "&7Click here to install the", "&7" + name + " addon to your server");
                    builder.handleMeta(ItemMeta.class, itemMeta -> {
                        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
                        container.set(Keys.ADDON_URL, PersistentDataType.STRING, data.getDownloadURL());
                        container.set(Keys.ADDON_NAME, PersistentDataType.STRING, name);
                        return itemMeta;
                    });
                    items.add(builder);
                }
            }

            ListPager<ItemBuilder> pages = new ListPager<>(finalMaxPets, items);
            pagerMap.put(player.getName(), pages);

            if (!pages.isEmpty()) {
                if (pages.exists(page)) {
                    pages.getPage(page).forEach(builder1 -> inv.addItem(builder1.build()));
                } else {
                    SimplePets.getDebugLogger().debug(DebugLevel.WARNING, "Page does not exist (Page " + page + " / " + pages.totalPages() + ")");
                }

            }

            if (ConfigOption.INSTANCE.MISC_TOGGLES_CLEAR_ALL_PLACEHOLDERS.getValue())
                inv.remove(ItemManager.PLACEHOLDER.getItemBuilder().build());

            player.openInventory(inv);

        });
    }

    @Override
    public void open(PetUser user, int page) {
        open(user, page, false);
    }

    public void handleFetch(String name, Consumer<List<AddonCloudData>> consumer) {
        if (addonCache.containsKey(name)) {
            consumer.accept(addonCache.get(name));
            return;
        }

        PetCore.getInstance().getAddonManager().fetchAddons(jsonValues -> {
            addonCache.put(name, jsonValues);
            consumer.accept(jsonValues);
        });
    }

    public ListPager<ItemBuilder> getPages(PetUser user) {
        OfflinePlayer player = user.getPlayer();
        if (pagerMap.containsKey(player.getName()))
            return pagerMap.get(player.getName());
        return null;
    }

    @Override
    public void reset(PetUser user) {
        super.reset(user);
        String name = user.getOwnerName();
        installerMap.remove(name);
        addonCache.remove(name);
        pagerMap.remove(name);
    }
}
