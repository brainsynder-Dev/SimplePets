package simplepets.brainsynder.storage.files;

import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.storage.files.base.FileMaker;

import java.io.File;

@Deprecated
public class PlayerPetInv extends FileMaker {
    private String fileName;

    public PlayerPetInv(String fileName) {
        super(PetCore.get(), new File(PetCore.get().getDataFolder().toString() + "/PetInventories/").toString(), fileName);
        this.fileName = fileName;
    }

    public String getFileName() {return this.fileName;}
}
