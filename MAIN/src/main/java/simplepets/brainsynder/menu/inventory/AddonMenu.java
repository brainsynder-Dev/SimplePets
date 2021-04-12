package simplepets.brainsynder.menu.inventory;

import com.google.common.collect.Lists;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.reflection.FieldAccessor;
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
import simplepets.brainsynder.addon.AddonData;
import simplepets.brainsynder.addon.PetAddon;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.managers.AddonManager;
import simplepets.brainsynder.managers.ItemManager;
import simplepets.brainsynder.menu.inventory.holders.AddonHolder;
import simplepets.brainsynder.menu.items.list.Air;
import simplepets.brainsynder.utils.Keys;
import simplepets.brainsynder.utils.debug.Debug;
import simplepets.brainsynder.utils.debug.DebugLevel;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public class AddonMenu extends CustomInventory {
    private Map<String, List<AddonData>> addonCache;
    private Map<String, ListPager<ItemBuilder>> pagerMap;

    public AddonMenu(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        addonCache = new HashMap<>();
        pagerMap = new HashMap<>();

        setDefault("size", 54);
        setDefault("title_comment", "The title of the GUI can support regular color codes '&c' and HEX color codes '&#FFFFFF'");
        setDefault("title", "&#de9790[] &#b35349SimplePets Addons");


        Map<Integer, String> object = new HashMap<>();
        Arrays.asList(
                11, 12, 13, 14, 15, 16, 17,
                20, 21, 22, 23, 24, 25, 26,
                29, 30, 31, 32, 33, 34, 35,
                38, 39, 40, 41, 42, 43, 44
        ).forEach(slot -> object.put(slot, "air"));
        object.put(46, "previouspage");
        object.put(54, "nextpage");

        // Makes sure the slot numbers are sorted from low to high
        Set<Map.Entry<Integer, String>> set = object.entrySet();
        List<Map.Entry<Integer, String>> list = new ArrayList<>(set);
        Collections.sort(list, Comparator.comparing(o -> (o.getKey())));

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
    public void open(PetUser user, int page) {
        if (!isEnabled()) return;
        Player player = (Player) user.getPlayer();
        pageSave.put(player.getName(), page);
        Inventory inv = Bukkit.createInventory(new AddonHolder(), getInteger("size", 54), Colorize.translateBungeeHex(getString("title", "&#de9790[] &#b35349SimplePets Addons")));

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

        int finalMaxPets = maxPets;
        handleFetch(player.getName(), jsonValues -> {
            List<ItemBuilder> items = Lists.newArrayList();

            AddonManager manager = PetCore.getInstance().getAddonManager();

            ItemBuilder builder = new ItemBuilder(Material.PLAYER_HEAD)
                    .setTexture("http://textures.minecraft.net/texture/ce1f3cc63c73a6a1dde72fe09c6ac5569376d7b61231bb740764368788cbf1fa");
            for (AddonData data : jsonValues) {
                String name = data.getName();
                Optional<PetAddon> optional = manager.fetchAddon(name);
                if (optional.isPresent()) {
                    PetAddon addon = optional.get();
                    builder = ItemBuilder.fromItem(addon.getAddonIcon());
                    builder.addLore("&r ", "&7Enabled: "+(addon.isEnabled() ? "&atrue" : "&cfalse"));
                    if (addon.hasUpdate() || (data.getVersion() > addon.getVersion())) {
                        if (!addon.hasUpdate()) FieldAccessor.getField(PetAddon.class, "update", Boolean.TYPE).set(addon, true);
                        builder.handleMeta(ItemMeta.class, itemMeta -> {
                            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
                            container.set(Keys.ADDON_UPDATE, PersistentDataType.STRING, data.getUrl());
                            return itemMeta;
                        });
                        builder.addLore("&r ", "&7Update available! You are on "+addon.getVersion()+" new version "+data.getVersion(), "&c(Shift Click To Update)");
                    }
                }else{
                    builder.withName(Colorize.fetchColor("e1eb5b")+name);
                    List<String> description = Lists.newArrayList();
                    if (!data.getDescription().isEmpty()) data.getDescription().forEach(s -> description.add(ChatColor.GRAY+s));
                    description.add("&r ");
                    description.add("&7Author: &e"+data.getAuthor());
                    description.add("&7Version: &e"+data.getVersion());
                    if (!data.getSupportedVersion().isEmpty()) description.add("&7Supported Version: &e"+data.getSupportedVersion()+" [And up]");
                    builder.withLore(description).addLore("&r ","&7Click here to install the", "&7"+name+" addon to your server");
                }
                builder.handleMeta(ItemMeta.class, itemMeta -> {
                    PersistentDataContainer container = itemMeta.getPersistentDataContainer();
                    if (!optional.isPresent()) container.set(Keys.ADDON_URL, PersistentDataType.STRING, data.getUrl());
                    container.set(Keys.ADDON_NAME, PersistentDataType.STRING, name);
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
                    Debug.debug(DebugLevel.MODERATE, "Page does not exist (Page " + page + " / " + pages.totalPages() + ")");
                }

            }
            player.openInventory(inv);

        });
    }

    public void handleFetch (String name, Consumer<List<AddonData>> consumer) {
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
        addonCache.remove(user.getPlayer().getName());
        pagerMap.remove(user.getPlayer().getName());
    }
}
