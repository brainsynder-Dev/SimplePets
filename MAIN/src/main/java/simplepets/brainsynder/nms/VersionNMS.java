package simplepets.brainsynder.nms;

import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.reflection.ReflectionUtil;

public class VersionNMS extends ReflectionUtil {

    public void registerPets() {
        ServerVersion version = ServerVersion.getVersion();
        if (version.getIntVersion() > ServerVersion.v1_12_R1.getIntVersion()) return;
        PetCore.get().debug("Registering Pets...");
        try {
            Class<?> clazz = Class.forName("simplepets.brainsynder.nms.registry." + version.name() + ".PetRegister");
            if (clazz == null) return;
            if (IEntityRegistry.class.isAssignableFrom(clazz)) {
                IEntityRegistry registry = (IEntityRegistry)clazz.getConstructor().newInstance();
                PetCore.get().debug("Registering Mobs/Pets...");
                for (IPetRegistry register : registry.list()) {
                    register.registerPet();
                }
            }
        }catch (Exception e){
            PetCore.get().debug("Could not link to a version for PetRegister. Possibly an Unsupported NMS version.");
        }
    }
}
