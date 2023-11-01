package simplepets.brainsynder.menu.inventory;

import com.google.common.collect.Lists;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.*;
import lib.brainsynder.utils.Colorize;
import lib.brainsynder.utils.ListPager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.event.inventory.PetInventoryAddPetItemEvent;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.inventory.handler.InventoryType;
import simplepets.brainsynder.api.pet.IPetConfig;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.managers.ItemManager;
import simplepets.brainsynder.menu.inventory.holders.SavesHolder;
import simplepets.brainsynder.menu.items.list.Air;
import simplepets.brainsynder.utils.Utilities;

import java.io.File;
import java.util.*;

public class SavesMenu extends CustomInventory {
    private Map<String, ListPager<PetUser.Entry<PetType, StorageTagCompound>>> pagerMap;
    private Map<String, Map<StorageTagCompound, PetUser.Entry<PetType, ItemStack>>> itemMap;

    public SavesMenu(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        pagerMap = new HashMap<>();
        itemMap = new HashMap<>();

        defaults.add("size", 54);
        setDefault("title_comment", "The title of the GUI can support regular color codes '&c' and HEX color codes '&#FFFFFF'");
        defaults.add("title", "&#de9790[] &#b35349Pet Saves");

        Map<Integer, String> object = new HashMap<>();
        Arrays.asList(
                11, 12, 13, 14, 15, 16, 17,
                20, 21, 22, 23, 24, 25, 26,
                29, 30, 31, 32, 33, 34, 35,
                38, 39, 40, 41, 42, 43, 44
        ).forEach(slot -> object.put(slot, "air"));
        object.put(28, "savepet");
        object.put(5, "name");
        object.put(46, "previouspage");
        object.put(49, "ride");
        object.put(50, "remove");
        object.put(51, "hat");
        object.put(54, "nextpage");

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
        return InventoryType.SAVES_GUI;
    }

    @Override
    public void open(PetUser user, int page) {
        if (!isEnabled()) return;
        Player player = Bukkit.getPlayer(user.getPlayer().getUniqueId());
        pageSave.put(player.getName(), page);
        Inventory inv = Bukkit.createInventory(new SavesHolder(), getSize(), Colorize.translateBungeeHex(getTitle()));
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
        ISpawnUtil spawnUtil = SimplePets.getSpawnUtil();

        List<PetUser.Entry<PetType, StorageTagCompound>> savedPets = Lists.newArrayList();
        boolean removeNoPerms = ConfigOption.INSTANCE.PERMISSIONS_PLAYER_ACCESS.getValue();
        user.getSavedPets().forEach(entry -> {
            PetType type = entry.getKey();
            if (type.isInDevelopment()
                    && (!ConfigOption.INSTANCE.PET_TOGGLES_DEV_MOBS.getValue()))
                return;

            SimplePets.getPetConfigManager().getPetConfig(type).ifPresent(config -> {
                if (!config.isEnabled()) return;
                if (!type.isSupported()) return;
                if (!spawnUtil.isRegistered(type)) return;
                if (player instanceof Player) {
                    if (removeNoPerms && (!Utilities.hasPermission(player, type.getPermission()))) return;
                    savedPets.add(entry);
                }
            });

        });


        ListPager<PetUser.Entry<PetType, StorageTagCompound>> pages = new ListPager<>(maxPets, new ArrayList<>(savedPets));
        pagerMap.put(player.getName(), pages);

        getSlots().forEach((slot, item) -> {
            if (item.isEnabled() && item.addItemToInv(user, this))
                inv.setItem(slot, item.getItemBuilder().build());
        });

        Map<StorageTagCompound, PetUser.Entry<PetType, ItemStack>> storageMap = itemMap.getOrDefault(player.getName(), new HashMap<>());
        if (!pages.isEmpty()) {
            pages.getPage(page).forEach(entry -> {
                PetType type = entry.getKey();
                IPetConfig petConfig = SimplePets.getPetConfigManager().getPetConfig(type).orElse(null);

                StorageTagCompound compound = entry.getValue();
                if (type != null) {
                    ItemStack stack;
                    if (storageMap.containsKey(compound)) {
                        stack = storageMap.get(compound).getValue();
                    }else {
                        ItemBuilder builder = type.getBuilder().clone();
                        if (petConfig != null) builder = petConfig.getBuilder();
                        builder.clearLore();
                        if (compound.hasKey("name") && (!compound.getString("name").equals("null")))
                            builder.withName(compound.getString("name").replaceAll("%player%", player.getName()));

                        ItemBuilder finalBuilder = builder;
                        compound.getKeySet().forEach(key -> {
                            if (!key.equals("name")) {
                                StorageBase base = compound.getTag(key);
                                if (base instanceof StorageTagCompound) {
                                    finalBuilder.addLore("  §e" + key + "§6:");
                                    for (String keys : ((StorageTagCompound)base).getKeySet()) {
                                        finalBuilder.addLore("  - §e" + keys + "§6: §7" + fetchValue(compound.getTag(keys)));
                                    }
                                }else{
                                    finalBuilder.addLore("  §e" + key + "§6: §7" + fetchValue(base));
                                }
                            }
                        });

                        stack = builder.replaceString("{player}", user.getPlayer().getName()).replaceString("{type}", type.getName()).build();
                        storageMap.put(compound, new PetUser.Entry<>(type, stack));
                    }

                    PetInventoryAddPetItemEvent event = new PetInventoryAddPetItemEvent(this, user, entry.getKey(), stack);
                    Bukkit.getPluginManager().callEvent(event);


                    if (!event.isCancelled()) inv.addItem(event.getItem());
                }
            });
        }
        itemMap.put(player.getName(), storageMap);

        if (ConfigOption.INSTANCE.MISC_TOGGLES_CLEAR_ALL_PLACEHOLDERS.getValue())
            inv.remove(ItemManager.PLACEHOLDER.getItemBuilder().build());

        player.openInventory(inv);
    }

    private String fetchValue(StorageBase base) {
        if (base instanceof StorageTagByte) {
            byte tagByte = ((StorageTagByte) base).getByte();
            if ((tagByte == 0) || (tagByte == 1))
                return String.valueOf(tagByte == 1);
            return String.valueOf(tagByte);
        }
        if (base instanceof StorageTagByteArray)
            return Arrays.toString(((StorageTagByteArray) base).getByteArray());
        if (base instanceof StorageTagDouble)
            return String.valueOf(((StorageTagDouble) base).getDouble());
        if (base instanceof StorageTagFloat)
            return String.valueOf(((StorageTagFloat) base).getFloat());
        if (base instanceof StorageTagInt)
            return String.valueOf(((StorageTagInt) base).getInt());
        if (base instanceof StorageTagIntArray)
            return Arrays.toString(((StorageTagIntArray) base).getIntArray());
        if (base instanceof StorageTagLong)
            return String.valueOf(((StorageTagLong) base).getLong());
        if (base instanceof StorageTagShort)
            return String.valueOf(((StorageTagShort) base).getShort());
        if (base instanceof StorageTagString)
            return String.valueOf(((StorageTagString) base).getString());
        if (base instanceof StorageTagList list) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < list.tagCount(); i++) {
                builder.append(fetchValue(list.get(i)));
            }
            return builder.toString();
        }

        return "";
    }

    public void resetMaps (PetUser owner) {
        Player player = Bukkit.getPlayer(owner.getPlayer().getUniqueId());
        itemMap.remove(player.getName());
        pagerMap.remove(player.getName());
    }

    public ListPager<PetUser.Entry<PetType, StorageTagCompound>> getPages(PetUser owner) {
        Player player = Bukkit.getPlayer(owner.getPlayer().getUniqueId());
        if (pagerMap.containsKey(player.getName()))
            return pagerMap.get(player.getName());
        return null;
    }

    public Map<StorageTagCompound, PetUser.Entry<PetType, ItemStack>> getItemStorage(PetUser owner) {
        Player player = Bukkit.getPlayer(owner.getPlayer().getUniqueId());
        return itemMap.getOrDefault(player.getName(), new HashMap<>());
    }



    @Override
    public void reset(PetUser user) {
        super.reset(user);
        String name = user.getOwnerName();
        itemMap.remove(name);
        pagerMap.remove(name);
    }
}
