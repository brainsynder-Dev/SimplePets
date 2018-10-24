package simplepets.brainsynder.nms.anvil;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;

import java.lang.reflect.Constructor;

public class AnvilGUI {
    private IAnvilGUI gui = null;

    public AnvilGUI(Plugin plugin, Player player, IAnvilClickEvent handler) {
        ServerVersion version = ServerVersion.getVersion();
        try {
            Class<?> clazz = Class.forName("simplepets.brainsynder.nms.anvil." + version.name() + ".HandleAnvilGUI");
            if (clazz == null) return;

            if (IAnvilGUI.class.isAssignableFrom(clazz)) {
                Constructor<?> constructor = clazz.getConstructor(Plugin.class, Player.class, IAnvilClickEvent.class);
                gui = (IAnvilGUI)constructor.newInstance(plugin, player, handler);
                PetCore.get().debug("Successfully found AnvilGUI Handler for "+version.name());
            }
        }catch (Exception e){
            PetCore.get().debug("Could not link to a version for HandleAnvilGUI. Possibly an Unsupported NMS version.");
        }
    }

    public void setSlot(AnvilSlot slot, ItemStack item) {
        if (gui != null) {
            gui.setSlot(slot, item);
        }
    }

    public void open() {
        if (gui != null) {
            gui.open();
        }

    }
}