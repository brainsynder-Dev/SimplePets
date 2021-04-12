package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.hostile.IEntityEndermanPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "screaming")
public class EndermanScreamData extends PetData<IEntityEndermanPet> {
    public EndermanScreamData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/120baf2ed7f2326803165ad801fc056d002243be8ccf2d87ea26b9c76dc3fa6e"));
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &cfalse")
                .setTexture("http://textures.minecraft.net/texture/96c0b36d53fff69a49c7d6f3932f2b0fe948e032226d5e8045ec58408a36e951"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityEndermanPet entity) {
        entity.setScreaming(!entity.isScreaming());
    }

    @Override
    public Object value(IEntityEndermanPet entity) {
        return entity.isScreaming();
    }
}
