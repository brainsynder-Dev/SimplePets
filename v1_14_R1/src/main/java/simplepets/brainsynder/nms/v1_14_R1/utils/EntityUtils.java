package simplepets.brainsynder.nms.v1_14_R1.utils;

import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.IRegistry;
import net.minecraft.server.v1_14_R1.MinecraftKey;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class EntityUtils {

    public static EntityTypes<?> getType (EntityWrapper wrapper) {
        return IRegistry.ENTITY_TYPE.get(new MinecraftKey(wrapper.getTypeName()));
    }
}
