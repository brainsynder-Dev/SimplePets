package simplepets.brainsynder.nms.v1_13_R1.registry;

import com.mojang.datafixers.types.Type;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.MinecraftKey;
import net.minecraft.server.v1_13_R1.World;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.reflection.ReflectionUtil;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Function;

public class CustomTypes<T extends Entity> extends EntityTypes<T> {
    private static CustomEntityRegistry CUSTOM;
    
    static {
        try {
            if (!PetCore.get().getConfiguration().getBoolean("OldPetRegistering")) {
                Field field = ReflectionUtil.getField(EntityTypes.class, "REGISTRY");
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                CUSTOM = new CustomEntityRegistry(REGISTRY);
                field.set(null, CUSTOM);
            }
        } catch (Exception e) {
            CUSTOM = null;
        }
    }

    public CustomTypes(Class<? extends T> oclass, Function<? super World, ? extends T> function, boolean flag, boolean flag1, @Nullable Type<?> type) {
        super(oclass, function, flag, flag1, type);
    }

    public static <T extends Entity> EntityTypes<T> a(int id, String s, EntityTypes.a<T> entitytypes_a) {
        EntityTypes entitytypes = entitytypes_a.b().a(s);
        if (CUSTOM != null) {
            CUSTOM.a(id, new MinecraftKey(s), entitytypes);
        }else{
            REGISTRY.a(id, new MinecraftKey(s), entitytypes);
        }
        return entitytypes;
    }
}
