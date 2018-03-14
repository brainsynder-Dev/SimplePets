package simplepets.brainsynder.nms;

import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.nms.registry.v1_11_R1.PetRegister;
import simplepets.brainsynder.reflection.ReflectionUtil;

public class VersionNMS extends ReflectionUtil {

    public void registerPets() {
        PetCore.get().debug("Registering Pets...");
        if (ServerVersion.getVersion() == ServerVersion.v1_11_R1) {
            PetCore.get().debug("Registering Mobs/Pets...");
            for (PetRegister register : PetRegister.values()) {
                register.registerPet();
            }
            return;
        }
        if (ServerVersion.getVersion() == ServerVersion.v1_12_R1) {
            for (simplepets.brainsynder.nms.registry.v1_12_R1.PetRegister register : simplepets.brainsynder.nms.registry.v1_12_R1.PetRegister.values()) {
                register.registerPet();
            }
        }
    }
}
