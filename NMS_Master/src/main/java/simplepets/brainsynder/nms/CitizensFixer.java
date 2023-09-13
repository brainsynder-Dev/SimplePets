/*
 * Copyright 2023
 * BSDevelopment <https://bsdevelopment.org>
 */

package simplepets.brainsynder.nms;

import lib.brainsynder.reflection.Reflection;
import net.minecraft.core.DefaultedMappedRegistry;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CitizensFixer {
    private static final MethodHandles.Lookup METHOD_LOOKUP = MethodHandles.lookup();
    private static final Field MODIFIERS;
    private static final MethodHandle PUT_OBJECT;
    private static final MethodHandle STATIC_FIELD_OFFSET;

    private static DefaultedRegistry<EntityType<?>> customRegistry = null;


    static {
        MODIFIERS = Reflection.getField(Field.class, "modifiers");

        try {
            Object UNSAFE = Reflection.getField(Class.forName("sun.misc.Unsafe"), "theUnsafe").get(null);

            STATIC_FIELD_OFFSET = getMethodHandle(UNSAFE.getClass(), "staticFieldOffset", Field.class).bindTo(UNSAFE);
            PUT_OBJECT = getMethodHandle(UNSAFE.getClass(), "putObject",  Object.class, long.class, Object.class).bindTo(UNSAFE);
        } catch (ClassNotFoundException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void resetCustomRegistry () {
        if (customRegistry == null) return;

        overrideRegistry(customRegistry);
    }

    public static void overrideRegistry(DefaultedRegistry<EntityType<?>> entityRegistry) {
        // Fetch the field in the BuiltInRegistries pertaining to the entity registry
        MethodHandle registrySetter = createStaticFinalSetter(BuiltInRegistries.class, VersionTranslator.REGISTRY_ENTITY_FIELD_NAME);

        try {
            // Set the field to use the specified registry
            registrySetter.invoke(entityRegistry);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to override the entity registry", e);
        }
    }

    public static DefaultedRegistry<EntityType<?>> getVanillaRegistry(DefaultedRegistry mappedRegistry) {
        if (!mappedRegistry.getClass().getName().equals(DefaultedMappedRegistry.class.getName())) {
            // If a custom registry was found (Citizens) store the registry instance for later use...
            if(customRegistry == null) customRegistry = mappedRegistry;

            for (Field field : mappedRegistry.getClass().getDeclaredFields()) {
                if ((field.getType() == DefaultedMappedRegistry.class) || (field.getType() == MappedRegistry.class)) {
                    Reflection.setFieldAccessible(field);

                    try {
                        // Find and fetch an instance of the vanilla entity registry
                        DefaultedRegistry<EntityType<?>> vanillaRegistry = (DefaultedRegistry<EntityType<?>>) field.get(mappedRegistry);
                        if (!vanillaRegistry.getClass().getName().equals(DefaultedMappedRegistry.class.getName()))
                            vanillaRegistry = getVanillaRegistry(vanillaRegistry);
                        return vanillaRegistry;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to find vanilla registry", e);
                    }
                }
            }
        }
        return mappedRegistry;
    }

    public static MethodHandle createStaticFinalSetter(Class<?> className, String fieldName) {
        Field field = Reflection.getField(className, fieldName);
        if (field == null) return null;

        // This check is a precaution if the modifiers field is not available
        if (MODIFIERS == null) {
            try {
                long offset = (long) STATIC_FIELD_OFFSET.invoke(field);

                return MethodHandles.insertArguments(PUT_OBJECT, 0, className, offset);
            } catch (Throwable throwable) {
                throw new RuntimeException("Failed to modify field '"+fieldName+"' in the "+className+" class", throwable);
            }
        }

        try {
            MODIFIERS.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            return METHOD_LOOKUP.unreflectSetter(field);
        } catch (Exception ignored) {}
        return null;
    }

    public static MethodHandle getMethodHandle(Class<?> className, String method, Class<?>... parameters) {
        if (className != null) {
            try {
                return METHOD_LOOKUP.unreflect(Reflection.getMethod(className, method, parameters));
            } catch (Exception exception) {
                throw new RuntimeException("Failed to lookup the method handler for the "+className.getName()+"."+method+" method", exception);
            }
        }
        return null;
    }
}
