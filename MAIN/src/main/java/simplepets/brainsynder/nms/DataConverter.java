package simplepets.brainsynder.nms;

import org.bukkit.Material;
import simplepets.brainsynder.utils.Utilities;

public class DataConverter {
    public Utilities.Data getColoredMaterial(Utilities.MatType type, int data) {
        return new Utilities.Data(findMaterial(type.name()), data);
    }

    public Utilities.Data getSkullMaterial(Utilities.SkullType type) {
        return new Utilities.Data(Material.SKULL_ITEM, type.ordinal());
    }

    public Material findMaterial(String name) {
        try {
            return Material.valueOf(name);
        } catch (Exception ignored) {
        }

        try {
            return Material.matchMaterial(name);
        } catch (Exception ignored) {
        }

        return Material.AIR;
    }
}
