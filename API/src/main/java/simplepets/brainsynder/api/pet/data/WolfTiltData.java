package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityWolfPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "tilted")
public class WolfTiltData extends PetData<IEntityWolfPet> {
    public WolfTiltData() {
        addDefaultItem("true", new ItemBuilder(Material.SKELETON_SKULL)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.SKELETON_SKULL)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityWolfPet entity) {
        entity.setHeadTilted(!entity.isHeadTilted());
    }

    @Override
    public Object value(IEntityWolfPet entity) {
        return entity.isHeadTilted();
    }
}
