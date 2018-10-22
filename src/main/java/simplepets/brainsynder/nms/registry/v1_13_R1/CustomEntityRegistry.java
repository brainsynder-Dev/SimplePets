package simplepets.brainsynder.nms.registry.v1_13_R1;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.MinecraftKey;
import net.minecraft.server.v1_13_R1.RegistryMaterials;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CustomEntityRegistry extends RegistryMaterials {
    private final BiMap<MinecraftKey, EntityTypes> entities = HashBiMap.create();
    private final Map<EntityTypes, Integer> entityIds = Maps.newHashMap();
    private final RegistryMaterials wrapped;

    public CustomEntityRegistry(RegistryMaterials<MinecraftKey, EntityTypes<?>> original) {
        this.wrapped = original;
    }

    @Override
    public void a(int code, Object key, Object v) {
        put(code, (MinecraftKey) key, (EntityTypes) v);
        super.a(code, key, v);
    }

    @Override
    public int a(Object key) {
        if (entityIds.containsKey(key)) {
            return entityIds.get(key);
        }

        return wrapped.a(key);
    }

    @Override
    public Object a(Random paramRandom) {
        return wrapped.a(paramRandom);
    }

    @Override
    public boolean d(Object o) {
        return wrapped.d(o);
    }

    public EntityTypes findType(Class<?> search) {
        for (Object type : wrapped) {
            if (((EntityTypes) type).c() == search) {
                return (EntityTypes) type;
            }
        }
        return null;
    }

    @Override
    public Object get(Object key) {
        if (entities.containsKey(key)) {
            return entities.get(key);
        }

        return wrapped.get(key);
    }

    public RegistryMaterials<Object, Object> getWrapped() {
        return wrapped;
    }

    @Override
    public Iterator<Object> iterator() {
        return wrapped.iterator();
    }

    @Override
    public Set<Object> keySet() {
        return wrapped.keySet();
    }

    public void put(int entityId, MinecraftKey key, EntityTypes entityClass) {
        entities.put(key, entityClass);
        entityIds.put(entityClass, entityId);
    }
}