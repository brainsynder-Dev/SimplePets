package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "frozen")
public class FrozenData extends PetData {
    public FrozenData() {
        addDefaultItem("true", new ItemBuilder(Material.ICE)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.GRASS_BLOCK)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityPet entity) {
        entity.setFrozen(!entity.isFrozen());
    }

    @Override
    public Object value(IEntityPet entity) {
        return entity.isFrozen();
    }

    @Override
    public boolean isModifiable(IEntityPet entity) {
        if (entity instanceof IEntityControllerPet controller) {
            controller.getVisibleEntity().setFrozen(!controller.getVisibleEntity().isFrozen());
            return false;
        }
        if (entity instanceof IEntityArmorStandPet) return false;
        return super.isModifiable(entity);
    }
}
