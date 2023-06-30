package simplepets.brainsynder.menu.inventory;

import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.storage.IStorage;
import lib.brainsynder.storage.StorageList;
import lib.brainsynder.utils.Colorize;
import lib.brainsynder.utils.ListPager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.api.event.inventory.PetInventoryAddPetItemEvent;
import simplepets.brainsynder.api.event.inventory.PetTypeStorage;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.inventory.handler.InventoryType;
import simplepets.brainsynder.api.pet.IPetConfig;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.managers.ItemManager;
import simplepets.brainsynder.menu.inventory.holders.SelectionHolder;
import simplepets.brainsynder.menu.items.list.Air;
import simplepets.brainsynder.utils.Utilities;

import java.io.File;
import java.util.*;

public class SelectionMenu extends CustomInventory {
    private List<PetType> availableTypes;
    private Map<String, ListPager<PetTypeStorage>> pagerMap;
    private Map<String, IStorage<PetTypeStorage>> petMap;

    public SelectionMenu(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        availableTypes = new ArrayList<>();
        pagerMap = new HashMap<>();
        petMap = new HashMap<>();

        setDefault("size", 54);
        setDefault("title_comment", "The title of the GUI can support regular color codes '&c' and HEX color codes '&#FFFFFF'");
        setDefault("title", "&#de9790[] &#b35349Pets");


        Map<Integer, String> object = new HashMap<>();
        Arrays.asList(
                11, 12, 13, 14, 15, 16, 17,
                20, 21, 22, 23, 24, 25, 26,
                29, 30, 31, 32, 33, 34, 35,
                38, 39, 40, 41, 42, 43, 44
        ).forEach(slot -> object.put(slot, "air"));
        object.put(4, "saves");
        object.put(6, "name");
        object.put(9, "data");
        object.put(46, "previouspage");
        object.put(49, "ride");
        object.put(50, "remove");
        object.put(51, "hat");
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

        for (PetType type : PetType.values()) {
            if (!type.isSupported()) continue;
            if (type.isInDevelopment()
                    && (!ConfigOption.INSTANCE.PET_TOGGLES_DEV_MOBS.getValue()))
                continue;
            Optional<IPetConfig> optional = SimplePets.getPetConfigManager().getPetConfig(type);
            if (!optional.isPresent()) continue;
            IPetConfig config = optional.get();
            if (!config.isEnabled()) continue;
            if (!SimplePets.getSpawnUtil().isRegistered(type)) continue;
            availableTypes.add(type);
        }
    }

    @Override
    public void onClick(int slot, ItemStack item, Player player) {

    }

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.SUMMON_GUI;
    }

    public void reloadAvailableTypes() {
        availableTypes.clear();

        for (PetType type : PetType.values()) {
            if (!type.isSupported()) continue;
            Optional<IPetConfig> optional = SimplePets.getPetConfigManager().getPetConfig(type);
            if (!optional.isPresent()) continue;
            IPetConfig config = optional.get();
            if (!config.isEnabled()) continue;
            if (!SimplePets.getSpawnUtil().isRegistered(type)) continue;
            availableTypes.add(type);
        }
    }

    @Override
    public void open(PetUser user, int page) {
        if (!isEnabled()) return;
        Player player = user.getPlayer();
        pageSave.put(player.getName(), page);
        Inventory inv = Bukkit.createInventory(new SelectionHolder(), getInteger("size", 54), Colorize.translateBungeeHex(getString("title", "&#de9790[] &#b35349Pets")));
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

        boolean removeNoPerms = ConfigOption.INSTANCE.PERMISSIONS_PLAYER_ACCESS.getValue();
        IStorage<PetTypeStorage> petTypes = new StorageList<>();
        for (PetType type : availableTypes) {
            PetTypeStorage storage = new PetTypeStorage(type);
            PetInventoryAddPetItemEvent event = new PetInventoryAddPetItemEvent(this, user, storage.getType(), storage.getItem());

            if (Utilities.hasPermission(player, type.getPermission())
                    || (user.getOwnedPets().contains(type) && ConfigOption.INSTANCE.UTILIZE_PURCHASED_PETS.getValue())) {
                Bukkit.getPluginManager().callEvent(event);
            } else {
                if (!removeNoPerms) {
                    Bukkit.getPluginManager().callEvent(event);
                } else {
                    continue;
                }
            }
            if (!event.isCancelled()) {
                petTypes.add(storage.setItem(event.getItem()));
            }
        }
        if ((petTypes.getSize() == 0) && (ConfigOption.INSTANCE.PERMISSIONS_OPEN_GUI.getValue())) {
            player.sendMessage(MessageFile.getTranslation(MessageOption.NO_PETS_UNLOCKED));
            return;
        }

        ListPager<PetTypeStorage> pages = new ListPager<>(maxPets, petTypes.toArrayList());
        pagerMap.put(player.getName(), pages);

        getSlots().forEach((slot, item) -> {
            if (item.isEnabled() && item.addItemToInv(user, this))
                inv.setItem(slot, item.getItemBuilder().build());
        });

        if (!pages.isEmpty()) {
            if (pages.exists(page)) {
                for (PetTypeStorage storage : pages.getPage(page))
                    inv.addItem(storage.getItem());
                petMap.put(player.getName(), new StorageList<>(pages.getPage(page)));
            } else {
                SimplePets.getDebugLogger().debug(DebugLevel.WARNING, "Page does not exist (Page " + page + " / " + pages.totalPages() + ")");
            }
        }

        if (ConfigOption.INSTANCE.MISC_TOGGLES_CLEAR_ALL_PLACEHOLDERS.getValue())
            inv.remove(ItemManager.PLACEHOLDER.getItemBuilder().build());

        player.openInventory(inv);
    }

    @Override
    public void update(PetUser user) {
        if (!isEnabled()) return;
        if (user == null) return;
        Player player = user.getPlayer();
        if (!pageSave.containsKey(player.getName())) return;
        int page = pageSave.getOrDefault(player.getName(), 1);

        Inventory inv = player.getOpenInventory().getTopInventory();
        if (inv.getHolder() == null) return;
        if (!(inv.getHolder() instanceof SelectionHolder)) return;

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

        boolean removeNoPerms = ConfigOption.INSTANCE.PERMISSIONS_PLAYER_ACCESS.getValue();
        IStorage<PetTypeStorage> petTypes = new StorageList<>();
        for (PetType type : availableTypes) {
            PetTypeStorage storage = new PetTypeStorage(type);
            PetInventoryAddPetItemEvent event = new PetInventoryAddPetItemEvent(this, user, storage.getType(), storage.getItem());

            if (Utilities.hasPermission(player, type.getPermission())
                    || (user.getOwnedPets().contains(type) && ConfigOption.INSTANCE.UTILIZE_PURCHASED_PETS.getValue())) {
                Bukkit.getPluginManager().callEvent(event);
            } else {
                if (!removeNoPerms) {
                    Bukkit.getPluginManager().callEvent(event);
                } else {
                    continue;
                }
            }
            if (!event.isCancelled()) {
                petTypes.add(storage.setItem(event.getItem()));
            }
        }
        if ((petTypes.getSize() == 0) && (ConfigOption.INSTANCE.PERMISSIONS_OPEN_GUI.getValue())) {
            player.sendMessage(MessageFile.getTranslation(MessageOption.NO_PETS_UNLOCKED));
            return;
        }

        ListPager<PetTypeStorage> pages = new ListPager<>(maxPets, petTypes.toArrayList());
        pagerMap.put(player.getName(), pages);

        getSlots().forEach((slot, item) -> {
            if (item.isEnabled() && item.addItemToInv(user, this))
                inv.setItem(slot, item.getItemBuilder().build());
        });

        if (!pages.isEmpty()) {
            if (pages.exists(page)) {
                for (PetTypeStorage storage : pages.getPage(page))
                    inv.addItem(storage.getItem());
                petMap.put(player.getName(), new StorageList<>(pages.getPage(page)));
            } else {
                SimplePets.getDebugLogger().debug(DebugLevel.WARNING, "Page does not exist (Page " + page + " / " + pages.totalPages() + ")");
            }
        }
    }

    public Map<String, IStorage<PetTypeStorage>> getPetMap() {
        return petMap;
    }

    public ListPager<PetTypeStorage> getPages(PetUser user) {
        OfflinePlayer player = user.getPlayer();
        if (pagerMap.containsKey(player.getName()))
            return pagerMap.get(player.getName());
        return null;
    }

    @Override
    public void reset(PetUser user) {
        super.reset(user);

        String name = user.getOwnerName();
        petMap.remove(name);
        pagerMap.remove(name);
    }
}
