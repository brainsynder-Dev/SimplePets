package simplepets.brainsynder.wrapper;

import lib.brainsynder.ServerVersion;

public enum ProfessionWrapper {
    FARMER(1, ServerVersion.v1_8_R3),
    LIBRARIAN(2, ServerVersion.v1_8_R3),
    PRIEST(3, ServerVersion.v1_8_R3),
    BLACKSMITH(4, ServerVersion.v1_8_R3),
    BUTCHER(5, ServerVersion.v1_8_R3),
    NITWIT(6, ServerVersion.v1_11_R1);

    private final int id;
    private final ServerVersion version;

    ProfessionWrapper(int id, ServerVersion version) {
        this.id = id;
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
        return ServerVersion.isEqualNew(version);
    }

    public int getId() {
        return id;
    }
}