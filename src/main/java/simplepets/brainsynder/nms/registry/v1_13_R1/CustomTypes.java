package simplepets.brainsynder.nms.registry.v1_13_R1;

import com.mojang.datafixers.types.Type;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.MinecraftKey;
import net.minecraft.server.v1_13_R1.World;

import javax.annotation.Nullable;
import java.util.function.Function;

public class CustomTypes<T extends Entity> extends EntityTypes<T> {
    public CustomTypes(Class<? extends T> oclass, Function<? super World, ? extends T> function, boolean flag, boolean flag1, @Nullable Type<?> type) {
        super(oclass, function, flag, flag1, type);
    }


    public static <T extends Entity> EntityTypes<T> a(int id, String s, EntityTypes.a<T> entitytypes_a) {
        EntityTypes entitytypes = entitytypes_a.b().a(s);
        REGISTRY.a(id, new MinecraftKey(s), entitytypes);
        return entitytypes;
    }

}
