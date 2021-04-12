package simplepets.brainsynder.api.wrappers;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.nms.DataConverter;
import lib.brainsynder.utils.DyeColorWrapper;
import org.bukkit.Material;

public enum WizardSpell {
        NONE(0, 0.0D, 0.0D, 0.0D, new ItemBuilder(Material.BARRIER)),
        SUMMON_VEX(1, 0.7D, 0.7D, 0.8D, DataConverter.getColoredMaterial(DataConverter.MaterialType.INK_SACK, DyeColorWrapper.GRAY)),
        FANGS(2, 0.4D, 0.3D, 0.35D, DataConverter.getColoredMaterial(DataConverter.MaterialType.INK_SACK, DyeColorWrapper.BROWN)),
        WOLOLO(3, 0.7D, 0.5D, 0.2D, DataConverter.getColoredMaterial(DataConverter.MaterialType.INK_SACK, DyeColorWrapper.ORANGE)),
        DISAPPEAR(4, 0.3D, 0.3D, 0.8D, DataConverter.getColoredMaterial(DataConverter.MaterialType.INK_SACK, DyeColorWrapper.LIGHT_BLUE)),
        BLINDNESS(5, 0.1D, 0.1D, 0.2D, DataConverter.getColoredMaterial(DataConverter.MaterialType.INK_SACK, DyeColorWrapper.BLACK));

        private final int id;
        private final double[] array;
        private final ItemBuilder builder;

        WizardSpell(int id, double var4, double var8, double var6, ItemBuilder builder) {
            this.id = id;
            this.builder = builder;
            this.array = new double[]{var4, var6, var8};
        }

        public ItemBuilder getIcon () {
            return this.builder;
        }

        public static WizardSpell getByName(String name) {
            for (WizardSpell wrapper : values()) {
                if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
            }
            return NONE;
        }

        public static WizardSpell fromID(int var0) {
            for (WizardSpell spell : values()) {
                if (var0 == spell.id) return spell;
            }
            return NONE;
        }

        public static WizardSpell getPrevious(WizardSpell current) {
            if (current.id == 0) return BLINDNESS;
            return values()[(current.id - 1)];
        }

        public static WizardSpell getNext(WizardSpell current) {
            if (current.id == 5) return NONE;
            return values()[(current.id + 1)];
        }

        public int getId() {
            return id;
        }

        public double[] getArray() {
            return array;
        }
    }