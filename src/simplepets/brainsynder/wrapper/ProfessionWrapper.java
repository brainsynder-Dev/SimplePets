package simplepets.brainsynder.wrapper;

import lombok.Getter;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.reflection.ReflectionUtil;

public enum ProfessionWrapper {
    FARMER(1, 18, false),
    LIBRARIAN(2, 18, false),
    PRIEST(3, 18, false),
    BLACKSMITH(4, 18, false),
    BUTCHER(5, 18, false),
    NITWIT(6, 111, false);

    private boolean zombie;
    @Getter
    private int id;
    private int version;

    ProfessionWrapper(int id, int version, boolean zombie) {
        this.id = id;
        this.zombie = zombie;
        this.version = version;
    }

    public static ProfessionWrapper getById(int id) {
        for (ProfessionWrapper wrapper : values()) {
            if (wrapper.ordinal() == id) {
                return wrapper;
            }
        }
        return null;
    }

    public static ProfessionWrapper getPrevious(ProfessionWrapper current) {
        ProfessionWrapper target = FARMER;

        switch (current) {
            case FARMER:
                if (ServerVersion.getVersion().getIntVersion() >= 111) {
                    target =  NITWIT;
                }else{
                    target = BUTCHER;
                }
                break;
            case LIBRARIAN: break;
            case PRIEST:
                target = LIBRARIAN;
                break;
            case BLACKSMITH:
                target = PRIEST;
                break;
            case BUTCHER:
                target = BLACKSMITH;
                break;
            case NITWIT:
                target = BUTCHER;
                break;
        }

        return target;
    }

    public static ProfessionWrapper getNext(ProfessionWrapper current) {
        ProfessionWrapper target = FARMER;

        switch (current) {
            case FARMER:
                target = LIBRARIAN;
                break;
            case LIBRARIAN:
                target = PRIEST;
                break;
            case PRIEST:
                target = BLACKSMITH;
                break;
            case BLACKSMITH:
                target = BUTCHER;
                break;
            case BUTCHER:
                if (ServerVersion.getVersion().getIntVersion() >= 111)
                    target = NITWIT;
                break;
            case NITWIT: break;
        }
        return target;
    }

    public static ProfessionWrapper getProfession(String name) {
        for (ProfessionWrapper wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }

        return FARMER;
    }

    public boolean isSupported() {
        return (ReflectionUtil.getVersionInt() >= this.version);
    }

    public boolean isZombie() {
        return this.zombie;
    }

    public enum ZombieProfession {
        NORMAL(0),
        HUSK(7);
        private int id;

        ZombieProfession(int id) {
            this.id = id;
        }


        public static ZombieProfession getPrevious(ZombieProfession current) {
            if (current == NORMAL) {
                return HUSK;
            } else {
                return NORMAL;
            }
        }

        public int getId() {
            return id;
        }

        public static ZombieProfession getNext(ZombieProfession current) {
            if (current == NORMAL) {
                return HUSK;
            }else{
                return NORMAL;
            }
        }

        public static ZombieProfession getProfession(String name) {
            for (ZombieProfession wrapper : values()) {
                if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
            }

            return NORMAL;
        }
    }

}
