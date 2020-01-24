package simplepets.brainsynder.nms;

import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.reflection.ReflectionUtil;

public class VersionNMS extends ReflectionUtil {

    public void registerPets() {
        ServerVersion version = ServerVersion.getVersion();
        if (!(version == ServerVersion.v1_11_R1 || version == ServerVersion.v1_12_R1)) return;
        PetCore.get().debug("Registering Pets...");
        try {
            Class<?> clazz = Class.forName("simplepets.brainsynder.nms." + version.name() + ".registry.Registry");
            if (clazz == null) return;
            IPetRegistry registry = (IPetRegistry) clazz.getConstructor().newInstance();
            PetCore.get().debug("Registering Mobs/Pets...");
            registry.registerPets();
        } catch (Exception e) {
            PetCore.get().debug("Could not link to a version for PetRegister. Possibly an Unsupported NMS version.");
            e.printStackTrace();
        }
    }
}
