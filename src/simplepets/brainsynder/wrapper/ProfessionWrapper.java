package simplepets.brainsynder.wrapper;

import lombok.AccessLevel;
import lombok.Getter;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.reflection.ReflectionUtil;

public enum ProfessionWrapper {
    FARMER(0, 18, false),
    LIBRARIAN(1, 18, false),
    PRIEST(2, 18, false),
    BLACKSMITH(3, 18, false),
    BUTCHER(4, 18, false),
    NITWIT(5, 111, false);

    private boolean zombie;
    @Getter
    private int id;
    @Getter(AccessLevel.PRIVATE)
    private int version;

    ProfessionWrapper(int id, int version, boolean zombie) {
        this.id = id;
        this.zombie = zombie;
        this.version = version;
    }

    public static ProfessionWrapper getById(int id) {
        for (ProfessionWrapper wrapper : values()) {
            if (wrapper.id == id) {
                return wrapper;
            }
        }
        return null;
    }

    public static ProfessionWrapper getPrevious(ProfessionWrapper current) {
        int original = current.id;
        if (original == 0) {
            if (ServerVersion.getVersion().getIntVersion() >= 111)
                return NITWIT;
            return BUTCHER;
        }
        return values()[(original - 1)];
    }

    public static ProfessionWrapper getNext(ProfessionWrapper current) {
        int original = current.id;
        if (ServerVersion.getVersion().getIntVersion() >= 111) {
            if (original == 5) {
                return FARMER;
            }
        } else {
            if (original == 4) {
                return FARMER;
            }
        }
        return values()[(original + 1)];
    }

    public boolean isSupported() {
        return (ReflectionUtil.getVersionInt() >= this.version);
    }

    public boolean isZombie() {
        return this.zombie;
    }

    public enum ZombieProfessions {
        NORMAL(),
        HUSK()

    }

}
