package simplepets.brainsynder.api.wrappers;

import java.util.HashMap;
import java.util.Map;

public enum TropicalPattern {
    KOB(0, false),
    SUNSTREAK(1, false),
    SNOOPER(2, false),
    DASHER(3, false),
    BRINELY(4, false),
    SPOTTY(5, false),
    FLOPPER(0, true),
    STRIPEY(1, true),
    GLITTER(2, true),
    BLOCKFISH(3, true),
    BETTY(4, true),
    CLAYFISH(5, true);

    private final int variant;
    private final boolean large;
    private static final Map<Integer, TropicalPattern> BY_DATA = new HashMap();

    TropicalPattern(int variant, boolean large) {
        this.variant = variant;
        this.large = large;
    }

    public int getDataValue() {
        return this.variant << 8 | (this.large ? 1 : 0);
    }

    public static TropicalPattern getByName(String name) {
        for (TropicalPattern wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return KOB;
    }

    public static TropicalPattern fromData(int data) {
        return BY_DATA.get(data);
    }

    public static TropicalPattern getPrevious(TropicalPattern current) {
        int target = current.ordinal()-1;
        if (target < 0) target = (values().length-1);
        return TropicalPattern.values()[target];
    }

    public static TropicalPattern getNext(TropicalPattern current) {
        int target = current.ordinal()+1;
        if (target > (values().length-1)) target = 0;
        return TropicalPattern.values()[target];
    }

    static {
        for(TropicalPattern type : values()) {
            BY_DATA.put(type.getDataValue(), TropicalPattern.values()[type.ordinal()]);
        }
    }
}