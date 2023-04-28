package simplepets.brainsynder.menu.inventory;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.utils.Colorize;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.handler.InventoryType;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.managers.ItemManager;
import simplepets.brainsynder.menu.inventory.holders.PetDataHolder;

import java.io.File;
import java.util.*;

public class DataMenu extends CustomInventory {
    private Map<String, PetType> typeMap;

    public DataMenu(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        typeMap = new HashMap<>();
        setDefault("size", 54);
        setDefault("title_comment", "The title of the GUI can support regular color codes '&c' and HEX color codes '&#FFFFFF'");
        setDefault("title", "&#de9790[] &#b35349Pet Data Changer");

        Map<Integer, String> object = new HashMap<>();
        Arrays.asList(20, 21, 22, 23, 24, 25, 26, 29, 30, 31, 32, 33, 34, 35).forEach(slot -> object.put(slot, "air"));
        object.put(1, "storage");
        object.put(5, "name");
        object.put(49, "ride");
        object.put(50, "remove");
        object.put(51, "hat");

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
        return InventoryType.DATA_GUI;
    }

    public PetType getType(Player player) {
        return typeMap.getOrDefault(player.getName(), PetType.UNKNOWN);
    }

    public void setType(Player player, PetType type) {
        if ((type == null) || (type == PetType.UNKNOWN)) {
            typeMap.remove(player.getName());
        } else {
            typeMap.put(player.getName(), type);
        }
    }


    @Override
    public void open(PetUser user, int page) {
        if (!isEnabled()) return;
        Player player = user.getPlayer();

        PetType type = typeMap.getOrDefault(player.getName(), PetType.UNKNOWN);

        Inventory inv = Bukkit.createInventory(new PetDataHolder(), getInteger("size", 54), Colorize.translateBungeeHex(getString("title", "&#de9790[] &#b35349Pet Data Changer")));
        int placeHolder = inv.getSize();
        while (placeHolder > 0) {
            inv.setItem(placeHolder - 1, ItemManager.PLACEHOLDER.getItemBuilder().build());
            placeHolder--;
        }

        getSlots().forEach((slot, item) -> {
            if (item.isEnabled() && item.addItemToInv(user, this))
                inv.setItem(slot, item.getItemBuilder().build());
        });

        if (user.hasPet(type)) {
            user.getPetEntity(type).ifPresent(entityPet -> addPetData(inv, entityPet));
        }


        if (ConfigOption.INSTANCE.MISC_TOGGLES_CLEAR_ALL_PLACEHOLDERS.getValue())
            inv.remove(ItemManager.PLACEHOLDER.getItemBuilder().build());

        player.openInventory(inv);
        setType(player, type);
    }


    @Override
    public void update(PetUser user) {
        if (!isEnabled()) return;
        if (user == null) return;
        Player player = user.getPlayer();
        if (!user.hasPets()) {
            player.closeInventory();
            return;
        }

        Inventory inv = player.getOpenInventory().getTopInventory();
        if (inv.getHolder() == null) return;
        if (!(inv.getHolder() instanceof PetDataHolder)) return;

        PetType type = typeMap.getOrDefault(player.getName(), PetType.UNKNOWN);
        int placeHolder = inv.getSize();
        while (placeHolder > 0) {
            inv.setItem(placeHolder - 1, ItemManager.PLACEHOLDER.getItemBuilder().build());
            placeHolder--;
        }

        if (!user.hasPet(type)) {
            player.closeInventory();
            return;
        }

        getSlots().forEach((slot, item) -> {
            if (item.isEnabled() && item.addItemToInv(user, this))
                inv.setItem(slot, item.getItemBuilder().build());
        });

        user.getPetEntity(type).ifPresent(entityPet -> addPetData(inv, entityPet));

        if (ConfigOption.INSTANCE.MISC_TOGGLES_CLEAR_ALL_PLACEHOLDERS.getValue())
            inv.remove(ItemManager.PLACEHOLDER.getItemBuilder().build());
    }

    private void addPetData (Inventory inv, IEntityPet entityPet) {
        PetType type = entityPet.getPetType();

        IEntityPet pet = entityPet;
        if (entityPet instanceof IEntityControllerPet) {
            pet = ((IEntityControllerPet)entityPet).getVisibleEntity();
        }

        IEntityPet finalPet = pet;
        type.getPetData().forEach(petData -> {
            if (!petData.isEnabled(finalPet)) return;
            if (ConfigOption.INSTANCE.PERMISSIONS_DATA_PERMS.getValue()
                    && (!entityPet.getPetUser().getPlayer().hasPermission(type.getPermission("data."+petData.getNamespace().namespace())))) return;
            if (!petData.isModifiable(finalPet)) return;
            petData.getItem(finalPet).ifPresent(o -> {
                inv.addItem(((ItemBuilder) o).build());
            });
        });
    }

    @Override
    public void reset(PetUser user) {
        super.reset(user);
        typeMap.remove(user.getOwnerName());
    }
}
