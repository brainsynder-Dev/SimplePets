package simplepets.brainsynder.menu.menuItems.panda;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityPandaPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.List;

@SupportedVersion(version = ServerVersion.v1_14_R1)
@ValueType(type = "boolean", def = "false")
public class PandaSneeze extends MenuItemAbstract<IEntityPandaPet> {

    public PandaSneeze(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public PandaSneeze(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder builder = type.getDataItemByName(getTargetName(), 0);
        builder.withName(formatName(builder, (entity, name) -> {
            name = name.replace("%value%", String.valueOf(entity.isSneezing()));
            return name;
        }));
        return builder;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        ItemBuilder builder = new ItemBuilder(Material.PLAYER_HEAD);
        builder.setTexture("http://textures.minecraft.net/texture/5c2d25e956337d82791fa0e6617a40086f02d6ebfbfd5a6459889cf206fca787");
        builder.withName("&6&lSneezing: &e%value%");
        items.add(builder);
        return items;
    }

    @Override
    public void onLeftClick() {
        entityPet.setSneezing(!entityPet.isSneezing());
    }

    @Override
    public String getTargetName() {
        return "sneeze";
    }
}
