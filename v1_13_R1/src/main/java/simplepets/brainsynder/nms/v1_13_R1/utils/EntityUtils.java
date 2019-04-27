package simplepets.brainsynder.nms.v1_13_R1.utils;

import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.MinecraftKey;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class EntityUtils {

    public static EntityTypes<?> getType (EntityWrapper wrapper) {
        return EntityTypes.REGISTRY.get(new MinecraftKey(wrapper.getName().toLowerCase()));
    }
}
