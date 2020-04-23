package simplepets.brainsynder.menu.inventory.list;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.nbt.*;
import lib.brainsynder.utils.ListPager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.menu.holders.SavesHolder;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.menu.items.list.Air;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.player.PetOwner;

import java.io.File;
import java.util.*;

public class SavesMenu extends CustomInventory {
    private Map<String, ListPager<StorageTagCompound>> pagerMap;
    private Map<String, Map<StorageTagCompound, ItemStack>> itemMap;

    public SavesMenu(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        pagerMap = new HashMap<>();
        itemMap = new HashMap<>();

        defaults.put("size", "54");
        defaults.put("title", "&a&lPet Saves:");

        Map<Integer, String> object = new HashMap<>();
        Arrays.asList(
                11, 12, 13, 14, 15, 16, 17,
                20, 21, 22, 23, 24, 25, 26,
                29, 30, 31, 32, 33, 34, 35,
                38, 39, 40, 41, 42, 43, 44
        ).forEach(slot -> object.put(slot, "air"));
        object.put(5, "savepet");
        object.put(46, "previouspage");
        object.put(54, "nextpage");

        Set<Map.Entry<Integer, String>> set = object.entrySet();
        List<Map.Entry<Integer, String>> list = new ArrayList<>(set);
        Collections.sort(list, Comparator.comparing(o -> (o.getKey())));

        JSONArray array = new JSONArray();
        for (Map.Entry<Integer, String> entry : list) {
            JSONObject json = new JSONObject();
            json.put("slot", entry.getKey());
            json.put("item", entry.getValue());
            array.add(json);
        }

        defaults.put("slots", array);
    }

    @Override
    public void open(PetOwner owner, int page) {
        if (!isEnabled()) return;
        Player player = Bukkit.getPlayer(owner.getUuid());
        pageSave.put(player.getName(), page);
        Inventory inv = Bukkit.createInventory(new SavesHolder(), getSize(), getTitle());
        int placeHolder = inv.getSize();
        int maxPets = 0;
        while (placeHolder > 0) {
            int slot = (placeHolder - 1);
            if (getSlots().containsKey(slot)) {
                Item item = getSlots().get(slot);
                if (item instanceof Air) {
                    maxPets++;
                } else {
                    inv.setItem(placeHolder - 1, PetCore.get().getItemLoaders().PLACEHOLDER.getItemBuilder().build());
                }
            } else {
                inv.setItem(placeHolder - 1, PetCore.get().getItemLoaders().PLACEHOLDER.getItemBuilder().build());
            }
            placeHolder--;
        }

        ListPager<StorageTagCompound> pages = new ListPager<>(maxPets, new ArrayList<>(owner.getSavedPets()));

        if (pagerMap.containsKey(player.getName())) {
            pages = pagerMap.get(player.getName());
        } else {
            pagerMap.put(player.getName(), pages);
        }

        getSlots().forEach((slot, item) -> {
            if (item.isEnabled() && item.addItemToInv(owner, this))
                inv.setItem(slot, item.getItemBuilder().build());
        });

        Map<StorageTagCompound, ItemStack> storageMap = itemMap.getOrDefault(player.getName(), new HashMap<>());
        if (!pages.isEmpty()) {
            pages.getPage(page).forEach(compound -> {

                PetDefault type = PetCore.get().getTypeManager().getType(compound.getString("PetType"));
                if (type != null) {
                    ItemStack stack;
                    if (storageMap.containsKey(compound)) {
                        stack = storageMap.get(compound);
                    }else {
                        ItemBuilder builder = type.getItemBuilder().clone();
                        builder.clearLore();
                        if (compound.hasKey("name") && (!compound.getString("name").equals("null")))
                            builder.withName(compound.getString("name"));
                        compound.getKeySet().forEach(key -> {
                            if (!key.equals("name")) {
                                StorageBase base = compound.getTag(key);
                                if (base instanceof StorageTagCompound) {
                                    builder.addLore("  §e" + key + "§6:");
                                    for (String keys : ((StorageTagCompound)base).getKeySet()) {
                                        builder.addLore("  - §e" + keys + "§6: §7" + fetchValue(compound.getTag(keys)));
                                    }
                                }else{
                                    builder.addLore("  §e" + key + "§6: §7" + fetchValue(base));
                                }
                            }
                        });

                        stack = builder.build();
                        storageMap.put(compound, stack);
                    }
                    inv.addItem(stack);
                }
            });
        }
        itemMap.put(player.getName(), storageMap);
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
        if (base instanceof StorageTagList) {
            StorageTagList list = (StorageTagList) base;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < list.tagCount(); i++) {
                builder.append(fetchValue(list.get(i)));
            }
            return builder.toString();
        }

        return "";
    }

    public void resetMaps (PetOwner owner) {
        Player player = Bukkit.getPlayer(owner.getUuid());
        itemMap.remove(player.getName());
        pagerMap.remove(player.getName());
    }

    public ListPager<StorageTagCompound> getPages(PetOwner owner) {
        Player player = Bukkit.getPlayer(owner.getUuid());
        if (pagerMap.containsKey(player.getName()))
            return pagerMap.get(player.getName());
        return null;
    }

    public Map<StorageTagCompound, ItemStack> getItemStorage(PetOwner owner) {
        Player player = Bukkit.getPlayer(owner.getUuid());
        return itemMap.getOrDefault(player.getName(), new HashMap<>());
    }



    @Override
    public void reset(PetOwner owner) {
        Player player = Bukkit.getPlayer(owner.getUuid());
        super.reset(owner);
        itemMap.remove(player.getName());
        pagerMap.remove(player.getName());
    }
}
