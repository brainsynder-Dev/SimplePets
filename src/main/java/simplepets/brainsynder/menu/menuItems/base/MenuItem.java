package simplepets.brainsynder.menu.menuItems.base;

import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.reflection.ReflectionUtil;
import simplepets.brainsynder.utils.ItemBuilder;

import java.util.List;

public abstract class MenuItem {
    protected IEntityPet entityPet = null;
    protected PetDefault type;

    MenuItem(PetDefault type, IEntityPet entityPet) {
        this.entityPet = entityPet;
        this.type = type;
    }

    MenuItem(PetDefault type) {
        this.type = type;
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    public int getVersion() {
        return 18;
    }

    public boolean isSupported() {
        return (ReflectionUtil.getVersionInt() >= getVersion());
    }

    public IEntityPet getEntityPet() {
        return entityPet;
    }

    public abstract ItemBuilder getItem();

    public abstract List<ItemBuilder> getDefaultItems();

    public String getPermission() {
        return (type.getPermission() + ".data." + getClass().getSimpleName().toLowerCase());
    }

    public boolean hasPermission(Player player) {
        if (!PetCore.get().getConfiguration().getBoolean("Needs-Permission")) return true;
        if (!PetCore.get().getConfiguration().getBoolean("Needs-Data-Permissions")) return true;
        if (PetCore.hasPerm(player, type.getPermission() + ".*")) return true;
        return player.hasPermission(type.getPermission() + ".data.*") || (player.hasPermission(getPermission()));
    }
}
