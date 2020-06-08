package simplepets.brainsynder.menu.inventory.list;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.menu.holders.ArmorHolder;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.items.list.Air;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.files.Messages;

import java.io.File;
import java.util.*;

public class ArmorMenu extends CustomInventory {

    public ArmorMenu(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        defaults.put("title", "&a&lArmorStand Armor");
        defaults.put("status", "CAN NOT MODIFY THE LAYOUT OF THE ARMOR");

        Map<Integer, String> object = new HashMap<>();
        object.put(1, "storage");
        object.put(5, "name");
        object.put(49, "ride");
        object.put(50, "remove");
        object.put(51, "hat");
        object.put(54, "update");

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
    public void open(PetOwner owner) {
        if (owner == null) return;
        if (!owner.hasPet()) return;
        Player player = Bukkit.getPlayer(owner.getUuid());
        Inventory inv = Bukkit.createInventory(new ArmorHolder(), 54, getTitle());
        int placeHolder = inv.getSize();
        while (placeHolder > 0) {
            inv.setItem(placeHolder - 1, PetCore.get().getItemLoaders().RED_PLACEHOLDER.getItemBuilder().build());
            placeHolder--;
        }

        getSlots().forEach((slot, item) -> {
            if (item.isEnabled() && item.addItemToInv(owner, this))
                inv.setItem(slot, item.getItemBuilder().build());
        });
        IEntityPet pet = owner.getPet().getVisableEntity();

        IEntityArmorStandPet stand = (IEntityArmorStandPet) pet;

        Messages messages = PetCore.get().getMessages();
        inv.setItem(13, addLore(messages, stand.getHeadItem()));
        inv.setItem(21, addLore(messages, stand.getLeftArmItem()));
        inv.setItem(22, addLore(messages, stand.getBodyItem()));
        inv.setItem(23, addLore(messages, stand.getRightArmItem()));
        inv.setItem(31, addLore(messages, stand.getLegItem()));
        inv.setItem(40, addLore(messages, stand.getFootItem()));
        player.openInventory(inv);
    }

    @Override
    public void onClick(int slot, ItemStack item, Player player) {
        PetOwner owner = PetOwner.getPetOwner(player);
        if (!owner.hasPet()) return;
        IEntityPet pet = owner.getPet().getVisableEntity();
        if (!(pet instanceof IEntityArmorStandPet)) return;
        IEntityArmorStandPet stand = (IEntityArmorStandPet) pet;
        boolean update = false;

        switch (slot) {
            case 13:
                stand.setHeadItem(new ItemStack(Material.AIR));
                update = true;
                break;
            case 21:
                stand.setLeftArmItem(new ItemStack(Material.AIR));
                update = true;
                break;
            case 22:
                stand.setBodyItem(new ItemStack(Material.AIR));
                update = true;
                break;
            case 23:
                stand.setRightArmItem(new ItemStack(Material.AIR));
                update = true;
                break;
            case 31:
                stand.setLegItem(new ItemStack(Material.AIR));
                update = true;
                break;
            case 40:
                stand.setFootItem(new ItemStack(Material.AIR));
                update = true;
                break;
        }

        // Only update the inventory if it is a armor slot
        if (update) open(owner);

    }

    public void update(PetOwner owner) {
        if (!isEnabled()) return;
        if (owner == null) return;
        Player player = Bukkit.getPlayer(owner.getUuid());

        if (player.getOpenInventory() == null) return;
        Inventory inv = player.getOpenInventory().getTopInventory();
        if (inv.getHolder() == null) return;
        if (!(inv.getHolder() instanceof ArmorHolder)) return;

        getSlots().forEach((slot, item) -> {
            if (item instanceof Air)
                if (item.isEnabled() && item.addItemToInv(owner, this))
                    inv.setItem(slot, item.getItemBuilder().build());
        });
        IEntityPet pet = owner.getPet().getEntity();

        if (!(pet instanceof IEntityArmorStandPet)) return;
        IEntityArmorStandPet stand = (IEntityArmorStandPet) pet;
        Messages messages = PetCore.get().getMessages();
        inv.setItem(13, addLore(messages, stand.getHeadItem()));
        inv.setItem(21, addLore(messages, stand.getLeftArmItem()));
        inv.setItem(22, addLore(messages, stand.getBodyItem()));
        inv.setItem(23, addLore(messages, stand.getRightArmItem()));
        inv.setItem(31, addLore(messages, stand.getLegItem()));
        inv.setItem(40, addLore(messages, stand.getFootItem()));
    }

    private ItemStack addLore (Messages messages, ItemStack stack) {
        Material material = stack.getType();
        if (material == Material.AIR) return stack;
        ItemBuilder builder = ItemBuilder.fromItem(stack);
        List<String> lore = builder.getMetaValue(ItemMeta.class, ItemMeta::getLore);
        if (lore == null) lore = new ArrayList<>();

        List<String> addition = messages.getStringList("ArmorMenu.ClickToRemove");
        lore.addAll(addition);

        return builder.withLore(lore).build();
    }
}
