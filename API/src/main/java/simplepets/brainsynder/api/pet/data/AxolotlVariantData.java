package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityAxolotlPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.AxolotlVariant;

@Namespace(namespace = "variant")
public class AxolotlVariantData extends PetData<IEntityAxolotlPet> {
    public AxolotlVariantData() {
        for (AxolotlVariant type : AxolotlVariant.values()) {
            addDefaultItem(type.name(), new ItemBuilder(Material.PLAYER_HEAD)
                    .withName("&#c8c8c8{name}: &a"+type.name())
                    .setTexture(type.getTexture()));
        }
    }

    @Override
    public Object getDefaultValue() {
        return AxolotlVariant.LUCY;
    }

    @Override
    public void onLeftClick(IEntityAxolotlPet entity) {
        entity.setVariant(AxolotlVariant.getNext(entity.getVariant()));
    }

    @Override
    public void onRightClick(IEntityAxolotlPet entity) {
        entity.setVariant(AxolotlVariant.getPrevious(entity.getVariant()));
    }

    @Override
    public Object value(IEntityAxolotlPet entity) {
        return entity.getVariant();
    }
}
