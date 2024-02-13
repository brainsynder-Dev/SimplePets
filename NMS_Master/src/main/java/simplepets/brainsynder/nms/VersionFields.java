/*
 * Copyright 2023
 * BSDevelopment <https://bsdevelopment.org>
 */

package simplepets.brainsynder.nms;

import lib.brainsynder.ServerVersion;
import simplepets.brainsynder.nms.utils.FieldValues;

public enum VersionFields implements FieldValues {
    v1_19 (
            // net.minecraft.network.syncher.DataWatcher
            // private static final it.unimi.dsi.fastutil.objects.Object2IntMap<java.lang.Class<? extends net.minecraft.world.entity.Entity>>
            "f", // entityDataField

            // net.minecraft.world.entity.EntityTypes
            // private final net.minecraft.world.entity.EntityTypes.b<T>
            "bs", // entityFactory

            // net.minecraft.core.RegistryMaterials
            // private boolean
            "ca", // registryFrozen

            // net.minecraft.core.RegistryMaterials
            // @Nullable private java.util.Map<T, net.minecraft.core.Holder.c<T>>
            "cc", // registryIntrusive

            // net.minecraft.core.registries.BuiltInRegistries
            // net.minecraft.core.RegistryBlocks<net.minecraft.world.entity.EntityTypes<?>>
            "", // entityRegistry

            // net.minecraft.world.entity.EntityLiving
            // protected int deathScore;
            // public float lastHurt;
            // protected boolean jumping;
            // public float xxa;
            "bn" // entityJump
    ),
    v1_19_1 (
            // net.minecraft.network.syncher.DataWatcher
            // private static final it.unimi.dsi.fastutil.objects.Object2IntMap<java.lang.Class<? extends net.minecraft.world.entity.Entity>>
            "f", // entityDataField

            // net.minecraft.world.entity.EntityTypes
            // private final net.minecraft.world.entity.EntityTypes.b<T>
            "bs", // entityFactory

            // net.minecraft.core.RegistryMaterials
            // private boolean
            "ca", // registryFrozen

            // net.minecraft.core.RegistryMaterials
            // @Nullable private java.util.Map<T, net.minecraft.core.Holder.c<T>>
            "cc", // registryIntrusive

            // net.minecraft.core.registries.BuiltInRegistries
            // net.minecraft.core.RegistryBlocks<net.minecraft.world.entity.EntityTypes<?>>
            "", // entityRegistry

            // net.minecraft.world.entity.EntityLiving
            // protected int deathScore;
            // public float lastHurt;
            // protected boolean jumping;
            // public float xxa;
            "bn" // entityJump
    ),
    v1_19_2 (
            // net.minecraft.network.syncher.DataWatcher
            // private static final it.unimi.dsi.fastutil.objects.Object2IntMap<java.lang.Class<? extends net.minecraft.world.entity.Entity>>
            "f", // entityDataField

            // net.minecraft.world.entity.EntityTypes
            // private final net.minecraft.world.entity.EntityTypes.b<T>
            "bs", // entityFactory

            // net.minecraft.core.RegistryMaterials
            // private boolean
            "ca", // registryFrozen

            // net.minecraft.core.RegistryMaterials
            // @Nullable private java.util.Map<T, net.minecraft.core.Holder.c<T>>
            "cc", // registryIntrusive

            // net.minecraft.core.registries.BuiltInRegistries
            // net.minecraft.core.RegistryBlocks<net.minecraft.world.entity.EntityTypes<?>>
            "", // entityRegistry

            // net.minecraft.world.entity.EntityLiving
            // protected int deathScore;
            // public float lastHurt;
            // protected boolean jumping;
            // public float xxa;
            "bn" // entityJump
    ),
    v1_19_3 (
            // net.minecraft.network.syncher.DataWatcher
            // private static final it.unimi.dsi.fastutil.objects.Object2IntMap<java.lang.Class<? extends net.minecraft.world.entity.Entity>>
            "e", // entityDataField

            // net.minecraft.world.entity.EntityTypes
            // private final net.minecraft.world.entity.EntityTypes.b<T>
            "bu", // entityFactory

            // net.minecraft.core.RegistryMaterials
            // private boolean
            "l", // registryFrozen

            // net.minecraft.core.RegistryMaterials
            // @Nullable private java.util.Map<T, net.minecraft.core.Holder.c<T>>
            "m", // registryIntrusive

            // net.minecraft.core.registries.BuiltInRegistries
            // net.minecraft.core.RegistryBlocks<net.minecraft.world.entity.EntityTypes<?>>
            "h", // entityRegistry

            // net.minecraft.world.entity.EntityLiving
            // protected int deathScore;
            // public float lastHurt;
            // protected boolean jumping;
            // public float xxa;
            "bn" // entityJump
    ),
    v1_19_4 (
            // net.minecraft.network.syncher.DataWatcher
            // private static final it.unimi.dsi.fastutil.objects.Object2IntMap<java.lang.Class<? extends net.minecraft.world.entity.Entity>>
            "b", // entityDataField

            // net.minecraft.world.entity.EntityTypes
            // private final net.minecraft.world.entity.EntityTypes.b<T>
            "bA", // entityFactory

            // net.minecraft.core.RegistryMaterials
            // private boolean
            "l", // registryFrozen

            // net.minecraft.core.RegistryMaterials
            // @Nullable private java.util.Map<T, net.minecraft.core.Holder.c<T>>
            "m", // registryIntrusive

            // net.minecraft.core.registries.BuiltInRegistries
            // net.minecraft.core.RegistryBlocks<net.minecraft.world.entity.EntityTypes<?>>
            "h", // entityRegistry

            // net.minecraft.world.entity.EntityLiving
            // protected int deathScore;
            // public float lastHurt;
            // protected boolean jumping;
            // public float xxa;
            "bi" // entityJump
    ),
    v1_20 (
            // net.minecraft.network.syncher.DataWatcher
            // private static final it.unimi.dsi.fastutil.objects.Object2IntMap<java.lang.Class<? extends net.minecraft.world.entity.Entity>>
            "b", // entityDataField

            // net.minecraft.world.entity.EntityTypes
            // private final net.minecraft.world.entity.EntityTypes.b<T>
            "bA", // entityFactory

            // net.minecraft.core.RegistryMaterials
            // private boolean
            "l", // registryFrozen

            // net.minecraft.core.RegistryMaterials
            // @Nullable private java.util.Map<T, net.minecraft.core.Holder.c<T>>
            "m", // registryIntrusive

            // net.minecraft.core.registries.BuiltInRegistries
            // net.minecraft.core.RegistryBlocks<net.minecraft.world.entity.EntityTypes<?>>
            "h", // entityRegistry

            // net.minecraft.world.entity.EntityLiving
            // protected int deathScore;
            // public float lastHurt;
            // protected boolean jumping;
            // public float xxa;
            "bk" // entityJump
    ),
    v1_20_1 (
            // net.minecraft.network.syncher.DataWatcher
            // private static final it.unimi.dsi.fastutil.objects.Object2IntMap<java.lang.Class<? extends net.minecraft.world.entity.Entity>>
            "b", // entityDataField

            // net.minecraft.world.entity.EntityTypes
            // private final net.minecraft.world.entity.EntityTypes.b<T>
            "bA", // entityFactory

            // net.minecraft.core.RegistryMaterials
            // private boolean
            "l", // registryFrozen

            // net.minecraft.core.RegistryMaterials
            // @Nullable private java.util.Map<T, net.minecraft.core.Holder.c<T>>
            "m", // registryIntrusive

            // net.minecraft.core.registries.BuiltInRegistries
            // net.minecraft.core.RegistryBlocks<net.minecraft.world.entity.EntityTypes<?>>
            "h", // entityRegistry

            // net.minecraft.world.entity.EntityLiving
            // protected int deathScore;
            // public float lastHurt;
            // protected boolean jumping;
            // public float xxa;
            "bk" // entityJump
    ),
    v1_20_2 (
            // net.minecraft.network.syncher.DataWatcher
            // private static final it.unimi.dsi.fastutil.objects.Object2IntMap<java.lang.Class<? extends net.minecraft.world.entity.Entity>>
            "b", // entityDataField

            // net.minecraft.world.entity.EntityTypes
            // private final net.minecraft.world.entity.EntityTypes.b<T>
            "bA", // entityFactory

            // net.minecraft.core.RegistryMaterials
            // private boolean
            "l", // registryFrozen

            // net.minecraft.core.RegistryMaterials
            // @Nullable private java.util.Map<T, net.minecraft.core.Holder.c<T>>
            "m", // registryIntrusive

            // net.minecraft.core.registries.BuiltInRegistries
            // net.minecraft.core.RegistryBlocks<net.minecraft.world.entity.EntityTypes<?>>
            "h", // entityRegistry

            // net.minecraft.world.entity.EntityLiving
            // protected int deathScore;
            // public float lastHurt;
            // protected boolean jumping;
            // public float xxa;
            "bj" // entityJump
    ),
    v1_20_3 (
            // net.minecraft.network.syncher.DataWatcher
            // private static final it.unimi.dsi.fastutil.objects.Object2IntMap<java.lang.Class<? extends net.minecraft.world.entity.Entity>>
            "b", // entityDataField

            // net.minecraft.world.entity.EntityTypes
            // private final net.minecraft.world.entity.EntityTypes.b<T>
            "bC", // entityFactory

            // net.minecraft.core.RegistryMaterials
            // private boolean
            "l", // registryFrozen

            // net.minecraft.core.RegistryMaterials
            // @Nullable private java.util.Map<T, net.minecraft.core.Holder.c<T>>
            "m", // registryIntrusive

            // net.minecraft.core.registries.BuiltInRegistries
            // net.minecraft.core.RegistryBlocks<net.minecraft.world.entity.EntityTypes<?>>
            "g", // entityRegistry

            // net.minecraft.world.entity.EntityLiving
            // protected int deathScore;
            // public float lastHurt;
            // protected boolean jumping;
            // public float xxa;
            "bj" // entityJump
    ),
    v1_20_4 (
            // net.minecraft.network.syncher.DataWatcher
            // private static final it.unimi.dsi.fastutil.objects.Object2IntMap<java.lang.Class<? extends net.minecraft.world.entity.Entity>>
            "b", // entityDataField

            // net.minecraft.world.entity.EntityTypes
            // private final net.minecraft.world.entity.EntityTypes.b<T>
            "bC", // entityFactory

            // net.minecraft.core.RegistryMaterials
            // private boolean
            "l", // registryFrozen

            // net.minecraft.core.RegistryMaterials
            // @Nullable private java.util.Map<T, net.minecraft.core.Holder.c<T>>
            "m", // registryIntrusive

            // net.minecraft.core.registries.BuiltInRegistries
            // net.minecraft.core.RegistryBlocks<net.minecraft.world.entity.EntityTypes<?>>
            "g", // entityRegistry

            // net.minecraft.world.entity.EntityLiving
            // protected int deathScore;
            // public float lastHurt;
            // protected boolean jumping;
            // public float xxa;
            "bj" // entityJump
    );

    public static VersionFields fromServerVersion (ServerVersion version) {
        return valueOf(version.name());
    }

    private final String entityDataMap, entityFactory, registryFrozen, registryIntrusive, entityRegistry, entityJump;

    VersionFields (String entityDataMap, String entityFactory, String registryFrozen, String registryIntrusive, String entityRegistry, String entityJump) {
        this.entityDataMap = entityDataMap;
        this.entityFactory = entityFactory;
        this.registryFrozen = registryFrozen;
        this.registryIntrusive = registryIntrusive;
        this.entityRegistry = entityRegistry;
        this.entityJump = entityJump;
    }

    @Override
    public String getEntityDataMapField() {
        return entityDataMap;
    }

    @Override
    public String getEntityFactoryField() {
        return entityFactory;
    }

    @Override
    public String getRegistryFrozenField() {
        return registryFrozen;
    }

    @Override
    public String getRegistryIntrusiveField() {
        return registryIntrusive;
    }

    @Override
    public String getEntityRegistryField() {
        return entityRegistry;
    }

    @Override
    public String getEntityJumpField() {
        return entityJump;
    }
}
