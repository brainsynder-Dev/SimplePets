package simplepets.brainsynder.api.wrappers;

import org.bukkit.NamespacedKey;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityChickenPet;
import simplepets.brainsynder.api.entity.passive.IEntityCowPet;
import simplepets.brainsynder.api.entity.passive.IEntityFrogPet;
import simplepets.brainsynder.api.entity.passive.IEntityPigPet;

import java.util.Locale;

public enum TemperatureVariant {
    TEMPERATE,
    WARM,
    COLD;

    public NamespacedKey getKey() {
        return NamespacedKey.minecraft(this.name().toLowerCase(Locale.ROOT));
    }

    public String getTextureByEntity(Class<? extends IEntityPet> entityPet) {
        if (entityPet.isAssignableFrom(IEntityFrogPet.class)) {
            return switch (this) {
                case TEMPERATE -> "23ce6f9998ed2da757d1e6372f04efa20e57dfc17c3a06478657bbdf51c2f2a2";
                case WARM -> "f77314fa038ec31357845a93274b4dc884124686728ffe0ded9c35466aca0aab";
                case COLD -> "ce62e8a048d040eb0533ba26a866cd9c2d0928c931c50b4482ac3a3261fab6f0";
            };
        }
        if (entityPet.isAssignableFrom(IEntityChickenPet.class)) {
            return switch (this) {
                case TEMPERATE -> "3ad3dd0083faa69a062f9ad81418f5a596180bf1592e4b8d1303b230b64bc79e";
                case WARM -> "58c231bf464f9a6c6786a22a4d119ea5e5057624c5a391470d35fa2b6ffa5183";
                case COLD -> "71753dcf9ab37874c8d578e1559b383cb1d1f0bb3b29e6c9605f3a817be8fdc7";
            };
        }
        if (entityPet.isAssignableFrom(IEntityCowPet.class)) {
            return switch (this) {
                case TEMPERATE -> "5cc04c81f781b46db142a15eb23b06e21b8a3c5627d0e8e70e61fe09ee372f54";
                case WARM -> "23b3376faf281c14ba472d2e7f6b6d0169e4bc2374835a9f8d0788e04d81343d";
                case COLD -> "e33221484d39f0d4ecdffa729567ce77fa8ce05b091db76dd5b079a20f7bf339";
            };
        }
        if (entityPet.isAssignableFrom(IEntityPigPet.class)) {
            return "http://textures.minecraft.net/texture/" + switch (this) {
                case TEMPERATE -> "9b1760e3778f8087046b86bec6a0a83a567625f30f0d6bce866d4bed95dba6c1";
                case WARM -> "7beba1a2d56e84f8e51fed6659f2cb7c14fed43859af584737897bf70c039475";
                case COLD -> "ba18d4043cd6c903866788914fd534315281af9f259e34837e3e175e545c2ede";
            };
        }

        // Will default to a Question Mark if it could not find a Temperature Variant
        return "fc271052719ef64079ee8c1498951238a74dac4c27b95640db6fbddc2d6b5b6e";
    }

    public static TemperatureVariant getByID(int id) {
        for (TemperatureVariant variant : values()) {
            if (variant.ordinal() == id) return variant;
        }
        return TEMPERATE;
    }

    public static TemperatureVariant getByName(String name) {
        for (TemperatureVariant variant : values()) {
            if (variant.name().equalsIgnoreCase(name)) return variant;
        }
        return TEMPERATE;
    }

    public static TemperatureVariant getPrevious(TemperatureVariant current) {
        return (current == TEMPERATE) ? COLD : values()[current.ordinal() - 1];
    }

    public static TemperatureVariant getNext(TemperatureVariant current) {
        return (current == COLD) ? TEMPERATE : values()[current.ordinal() + 1];
    }
}
