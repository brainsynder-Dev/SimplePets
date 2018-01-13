package simplepets.brainsynder.wrapper;

import simple.brainsynder.utils.ServerVersion;

public enum WizardSpell {
    NONE(0, 0.0D, 0.0D, 0.0D),
    SUMMON_VEX(1, 0.7D, 0.7D, 0.8D),
    FANGS(2, 0.4D, 0.3D, 0.35D),
    WOLOLO(3, 0.7D, 0.5D, 0.2D),
    DISAPPEAR(4, 0.3D, 0.3D, 0.8D),
    BLINDNESS(5, 0.1D, 0.1D, 0.2D);

    private final int id;
    private final double[] array;

    WizardSpell(int id, double var4, double var8, double var6) {
        this.id = id;
        this.array = new double[]{var4, var6, var8};
    }

    public static WizardSpell getByName(String name) {
        for (WizardSpell wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return NONE;
    }

    public static WizardSpell fromID(int var0) {
        for (WizardSpell spell : values()) {
            if (var0 == spell.id) {
                return spell;
            }
        }
        return NONE;
    }

    public static WizardSpell getPrevious(WizardSpell current) {
        int original = current.id;
        if (original == 0) {
            if (ServerVersion.getVersion().getIntVersion() == 111) {
                return WOLOLO;
            }
            return BLINDNESS;
        }
        return values()[(original - 1)];
    }

    public static WizardSpell getNext(WizardSpell current) {
        int original = current.id;
        if (ServerVersion.getVersion().getIntVersion() == 111) {
            if (original == 3)
                return NONE;
        }
        if (original == 5) {
            return NONE;
        }
        return values()[(original + 1)];
    }

    public int getId() {
        return id;
    }

    public double[] getArray() {
        return array;
    }
}
