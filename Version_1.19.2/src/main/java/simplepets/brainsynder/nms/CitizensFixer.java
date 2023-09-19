/*
 * Copyright 2023
 * BSDevelopment <https://bsdevelopment.org>
 */

package simplepets.brainsynder.nms;

import lib.brainsynder.reflection.Reflection;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;

public class CitizensFixer {
    private static final boolean CITIZENS_FOUND;

    static {
        CITIZENS_FOUND = (Bukkit.getServer().getPluginManager().getPlugin("Citizens") != null);
    }

    public static DefaultedRegistry<EntityType<?>> getVanillaRegistry(DefaultedRegistry mappedRegistry) {
        if (!CITIZENS_FOUND) return mappedRegistry;
        if (!mappedRegistry.getClass().getName().equals(DefaultedRegistry.class.getName())) {

            for (Field field : mappedRegistry.getClass().getDeclaredFields()) {
                if ((field.getType() == DefaultedRegistry.class) || (field.getType() == MappedRegistry.class)) {
                    Reflection.setFieldAccessible(field);

                    try {
                        // Find and fetch an instance of the vanilla entity registry
                        DefaultedRegistry<EntityType<?>> vanillaRegistry = (DefaultedRegistry<EntityType<?>>) field.get(mappedRegistry);
                        if (!vanillaRegistry.getClass().getName().equals(DefaultedRegistry.class.getName()))
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
}
