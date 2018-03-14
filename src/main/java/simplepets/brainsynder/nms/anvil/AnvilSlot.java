package simplepets.brainsynder.nms.anvil;

public enum AnvilSlot {
    INPUT_LEFT(0),
    INPUT_CENTER(1),
    OUTPUT(2);

    private int slot;

    AnvilSlot(int slot) {
        this.slot = slot;
    }

    public static AnvilSlot bySlot(int slot) {
        AnvilSlot[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            AnvilSlot anvilSlot = var1[var3];
            if (anvilSlot.getSlot() == slot) {
                return anvilSlot;
            }
        }

        return null;
    }

    public int getSlot() {
        return this.slot;
    }
}