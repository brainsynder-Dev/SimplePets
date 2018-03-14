package simplepets.brainsynder.nms;

import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.nms.registry.PetRegistry;
import simplepets.brainsynder.nms.registry.v1_11_R1.PetRegister;
import simplepets.brainsynder.reflection.ReflectionUtil;

public class VersionNMS extends ReflectionUtil {
    private static PetRegistry register;

    public static void registerPets() {
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
            return;
        }
        getRegister().registerAll();
    }

    private static PetRegistry getRegister() {
        if (register == null)
            register = new PetRegistry();
        return register;
    }

    public static void unregisterPets() {
        if (ReflectionUtil.getVersionInt() >= 111) {
            return;
        }
        getRegister().unregisterAll();
    }
}
