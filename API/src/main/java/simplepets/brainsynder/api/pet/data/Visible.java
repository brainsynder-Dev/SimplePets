package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.pet.annotations.DisableDefault;

@DisableDefault
@Namespace(namespace = "visible")
public class Visible extends PetData<IEntityPet> {
    public Visible() {
        addDefaultItem("true", new ItemBuilder(Material.GLASS_BOTTLE)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.SPLASH_POTION)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return true;
    }

    @Override
    public void onLeftClick(IEntityPet entity) {
        if (entity instanceof IEntityControllerPet controller) {
            controller.getVisibleEntity().setPetVisible(!controller.getVisibleEntity().isPetVisible());
            return;
        }

        entity.setPetVisible(!entity.isPetVisible());
    }

    @Override
    public Object value(IEntityPet entity) {
        if (entity instanceof IEntityControllerPet controller) return controller.getVisibleEntity().isPetVisible();
        return entity.isPetVisible();
    }
}
