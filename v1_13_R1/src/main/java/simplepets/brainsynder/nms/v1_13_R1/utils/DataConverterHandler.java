package simplepets.brainsynder.nms.v1_13_R1.utils;

import org.bukkit.Material;
import simplepets.brainsynder.nms.DataConverter;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

public class DataConverterHandler extends DataConverter {
    @Override
    public Utilities.Data getColoredMaterial(Utilities.MatType type, int data) {
        DyeColorWrapper dye = DyeColorWrapper.getByWoolData((byte) data);
        if (type == Utilities.MatType.INK_SACK)
            dye = DyeColorWrapper.getByDyeData((byte) data);

        String name = dye.name();
        if (name.equalsIgnoreCase("SILVER")) name = "LIGHT_GRAY";


        Material material;
        if (type == Utilities.MatType.INK_SACK) {
            if (dye == DyeColorWrapper.WHITE) {
                material = Material.BONE_MEAL;
            } else if (dye == DyeColorWrapper.YELLOW) {
                material = Material.DANDELION_YELLOW;
            } else if (dye == DyeColorWrapper.BLUE) {
                material = Material.LAPIS_LAZULI;
            } else if (dye == DyeColorWrapper.BROWN) {
                material = Material.COCOA_BEANS;
            } else if (dye == DyeColorWrapper.GREEN) {
                material = Material.CACTUS_GREEN;
            } else if (dye == DyeColorWrapper.RED) {
                material = Material.ROSE_RED;
            } else if (dye == DyeColorWrapper.BLACK) {
                material = Material.INK_SAC;
            } else {
                material = findMaterial(name + "_DYE");
            }
        } else {
            material = findMaterial(name + "_" + type.getName());
        }


        return new Utilities.Data(material, -1);
    }

    @Override
    public Utilities.Data getSkullMaterial(Utilities.SkullType type) {
        Material material = Material.PLAYER_HEAD;
        switch (type) {
            case DRAGON:
                material = Material.DRAGON_HEAD;
                break;
            case CREEPER:
                material = Material.CREEPER_HEAD;
                break;
            case ZOMBIE:
                material = Material.ZOMBIE_HEAD;
                break;
            case SKELETON:
                material = Material.SKELETON_SKULL;
                break;
            case WITHER:
                material = Material.WITHER_SKELETON_SKULL;
                break;
        }

        return new Utilities.Data(material, -1);
    }

    @Override
    public Material findMaterial(String name) {
        try {
            return Material.valueOf(name);
        } catch (Exception ignored) {
        }

        try {
            return Material.valueOf("LEGACY_" + name);
        } catch (Exception ignored) {
        }

        try {
            return Material.matchMaterial(name);
        } catch (Exception ignored) {
        }

        try {
            return Material.matchMaterial(name, true);
        } catch (Exception ignored) {
        }

        return super.findMaterial(name);
    }
}
