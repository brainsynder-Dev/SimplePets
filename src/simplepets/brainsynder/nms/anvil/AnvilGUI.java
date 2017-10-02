package simplepets.brainsynder.nms.anvil;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.nms.anvil.v1_12_R1.HandleAnvilGUI;

public class AnvilGUI {
    private IAnvilGUI gui = null;

    public AnvilGUI(Plugin plugin, Player player, IAnvilClickEvent handler) {
        ServerVersion version = ServerVersion.getVersion();
        switch (version) {
            case v1_8_R3:
                gui = new simplepets.brainsynder.nms.anvil.v1_8_R3.HandleAnvilGUI (plugin, player, handler);
                break;
            case v1_9_R1:
                gui = new simplepets.brainsynder.nms.anvil.v1_9_R1.HandleAnvilGUI (plugin, player, handler);
                break;
            case v1_9_R2:
                gui = new simplepets.brainsynder.nms.anvil.v1_9_R2.HandleAnvilGUI (plugin, player, handler);
                break;
            case v1_10_R1:
                gui = new simplepets.brainsynder.nms.anvil.v1_10_R1.HandleAnvilGUI (plugin, player, handler);
                break;
            case v1_11_R1:
                gui = new simplepets.brainsynder.nms.anvil.v1_11_R1.HandleAnvilGUI (plugin, player, handler);
                break;
            case v1_12_R1:
                gui = new HandleAnvilGUI (plugin, player, handler);
                break;
            default:
                PetCore.get().debug("Could not link to a version for HandleAnvilGUI. Possibly an Unsupported NMS version.");
                gui = null;
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