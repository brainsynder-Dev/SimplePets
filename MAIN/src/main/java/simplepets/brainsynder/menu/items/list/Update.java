package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.menu.holders.ArmorHolder;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.inventory.list.ArmorMenu;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.player.PetOwner;

import java.io.File;

public class Update extends Item {
    public Update(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder (org.bukkit.Material.PLAYER_HEAD).setTexture("http://textures.minecraft.net/texture/bc8def67a12622ead1decd3d89364257b531896d87e469813131ca235b5c7").withName("&e&lUpdate ArmorStand");
    }

    @Override
    public void onClick(PetOwner owner, CustomInventory inventory) {
        if (!(inventory instanceof ArmorMenu)) return;
        if (!owner.hasPet()) return;
        Player player = owner.getPlayer();
        IEntityArmorStandPet stand = (IEntityArmorStandPet) owner.getPet().getVisableEntity();

        if (player.getOpenInventory() == null) return;
        Inventory open = player.getOpenInventory().getTopInventory();
        if (open.getHolder() == null) return;
        if (!(open.getHolder() instanceof ArmorHolder)) return;

        stand.setHeadItem(open.getItem(13));
        stand.setLeftArmItem(open.getItem(21));
        stand.setBodyItem(open.getItem(22));
        stand.setRightArmItem(open.getItem(23));
        stand.setLegItem(open.getItem(31));
        stand.setFootItem(open.getItem(40));
    }
}
