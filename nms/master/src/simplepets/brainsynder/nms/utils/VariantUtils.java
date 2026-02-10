package simplepets.brainsynder.nms.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;

import java.util.Objects;
import java.util.Optional;

public class VariantUtils {
    public static final String TAG_VARIANT = "variant";

    public static <T> Holder<T> getDefaultOrAny(RegistryAccess var0, ResourceKey<T> var1) {
        Registry<T> var2 = var0.lookupOrThrow(var1.registryKey());
        Optional var10000 = var2.get(var1);
        Objects.requireNonNull(var2);
        return (Holder)var10000.or(var2::getAny).orElseThrow();
    }

    public static <T> Holder<T> getAny(RegistryAccess var0, ResourceKey<? extends Registry<T>> var1) {
        return var0.lookupOrThrow(var1).getAny().orElseThrow();
    }
}