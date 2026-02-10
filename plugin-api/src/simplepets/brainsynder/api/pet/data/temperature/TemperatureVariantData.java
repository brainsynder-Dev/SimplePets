package simplepets.brainsynder.api.pet.data.temperature;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.ITemperaturePet;
import simplepets.brainsynder.api.entity.passive.IEntityChickenPet;
import simplepets.brainsynder.api.entity.passive.IEntityCowPet;
import simplepets.brainsynder.api.entity.passive.IEntityFrogPet;
import simplepets.brainsynder.api.entity.passive.IEntityPigPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.TemperatureVariant;

public class TemperatureVariantData extends PetData<ITemperaturePet> {
    public TemperatureVariantData(Class<? extends IEntityPet> entityClass) {
        for (TemperatureVariant type : TemperatureVariant.values()) {
            addDefaultItem(type.name(), new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &a" + type.name())
                .setTexture("http://textures.minecraft.net/texture/" + type.getTextureByEntity(entityClass)));
        }
    }

    @Override
    public Object getDefaultValue() {
        return TemperatureVariant.TEMPERATE;
    }

    @Override
    public void onLeftClick(ITemperaturePet entity) {
        entity.setVariant(TemperatureVariant.getNext(entity.getVariant()));
    }

    @Override
    public void onRightClick(ITemperaturePet entity) {
        entity.setVariant(TemperatureVariant.getPrevious(entity.getVariant()));
    }

    @Override
    public Object value(ITemperaturePet entity) {
        return entity.getVariant();
    }

    @SupportedVersion(version = ServerVersion.v1_21_5)
    @Namespace(namespace = "variant")
    public static class ChickenTemperature extends TemperatureVariantData {
        public ChickenTemperature() {
            super(IEntityChickenPet.class);
        }
    }

    @SupportedVersion(version = ServerVersion.v1_21_5)
    @Namespace(namespace = "variant")
    public static class CowTemperature extends TemperatureVariantData {
        public CowTemperature() {
            super(IEntityCowPet.class);
        }
    }

    @Namespace(namespace = "variant")
    public static class FrogTemperature extends TemperatureVariantData {
        public FrogTemperature() {
            super(IEntityFrogPet.class);
        }
    }

    @SupportedVersion(version = ServerVersion.v1_21_5)
    @Namespace(namespace = "variant")
    public static class PigTemperature extends TemperatureVariantData {
        public PigTemperature() {
            super(IEntityPigPet.class);
        }
    }
}
