package simplepets.brainsynder.menu.inventory.list;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.menu.holders.ArmorHolder;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.items.list.Air;
import simplepets.brainsynder.player.PetOwner;

import java.io.File;
import java.util.*;

public class ArmorMenu extends CustomInventory {

    public ArmorMenu(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        defaults.put("title", "&a&lArmorStand Armor");
        defaults.put("status", "CAN NOT BE MODIFIED");

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
        inv.setItem(13, stand.getHeadItem());
        inv.setItem(21, stand.getLeftArmItem());
        inv.setItem(22, stand.getBodyItem());
        inv.setItem(23, stand.getRightArmItem());
        inv.setItem(31, stand.getLegItem());
        inv.setItem(40, stand.getFootItem());
        player.openInventory(inv);
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
        inv.setItem(13, stand.getHeadItem());
        inv.setItem(21, stand.getLeftArmItem());
        inv.setItem(22, stand.getBodyItem());
        inv.setItem(23, stand.getRightArmItem());
        inv.setItem(31, stand.getLegItem());
        inv.setItem(40, stand.getFootItem());
    }
}
