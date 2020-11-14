package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.ISleeper;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.storage.files.Messages;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.List;

@SupportedVersion(version = ServerVersion.v1_14_R1)
@ValueType(type = "boolean", def = "false")
public class Sleep extends MenuItemAbstract<ISleeper> {

    public Sleep(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Sleep(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder builder = type.getDataItemByName(getTargetName(), 0);
        builder.withName(formatName(builder, (entity, name) -> {
            name = name.replace("%value%", Messages.getTrueOrFalse(entity.isSleeping()));
            return name;
        }));
        return builder;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        items.add(new ItemBuilder (Material.RED_BED).withName("&6Sleeping: &e%value%"));
        return items;
    }

    @Override
    public void onLeftClick() {
        entityPet.setSleeping(!entityPet.isSleeping());
    }

    @Override
    public String getTargetName() {
        return "sleep";
    }
}
