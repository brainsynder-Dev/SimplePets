package simplepets.brainsynder.menu.menuItems.base;

import lib.brainsynder.VersionRestricted;
import lib.brainsynder.item.ItemBuilder;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.MIRename;

import java.util.List;

public abstract class MenuItem<E extends IEntityPet> implements VersionRestricted {
    protected E entityPet = null;
    protected PetType type;

    MenuItem(PetType type, E entityPet) {
        this.entityPet = entityPet;
        this.type = type;
    }

    MenuItem(PetType type) {
        this.type = type;
    }

    public String getName() {
        return getClass().getSimpleName();
    }
    public String getTargetName() {
        return getClass().getSimpleName().toLowerCase();
    }

    public E getEntityPet() {
        return entityPet;
    }

    public abstract ItemBuilder getItem();

    public abstract List<ItemBuilder> getDefaultItems();

    public String getPermission() {
        return (type.getPermission() + ".data." + getTargetName().toLowerCase());
    }

    public boolean hasPermission(Player player) {
        if (!PetCore.get().needsPermissions()) return true;
        if (!PetCore.get().needsDataPermissions()) return true;
        if (PetCore.hasPerm(player, type.getPermission() + ".*")) return true;
        return player.hasPermission(type.getPermission() + ".data.*") || (player.hasPermission(getPermission()));
    }

    public String formatName (ItemBuilder item, MIRename<E> entity) {
        String name = item.getName();
        return entity.run(entityPet, name);
    }
}
