package simplepets.brainsynder.files;

import simplepets.brainsynder.PetCore;

import java.io.File;

public class PlayerPetInv extends FileMaker {
    private String fileName;

    public PlayerPetInv(String fileName) {
        super(PetCore.get(), new File(PetCore.get().getDataFolder().toString() + "/PetInventories/").toString(), fileName);
        this.fileName = fileName;
    }

    public String getFileName() {return this.fileName;}
}
