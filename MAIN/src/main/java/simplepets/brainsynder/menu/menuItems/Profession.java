package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.nms.DataConverter;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityVillagerPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ValueType;
import simplepets.brainsynder.wrapper.ProfessionWrapper;

import java.util.ArrayList;
import java.util.List;

@ValueType(def = "FARMER", target = "https://github.com/brainsynder-Dev/SimplePets/blob/master/MAIN/src/main/java/simplepets/brainsynder/wrapper/ProfessionWrapper.java")
public class Profession extends MenuItemAbstract {
    public Profession(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Profession(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("profession", 0);
        if (entityPet instanceof IEntityVillagerPet) {
            IEntityVillagerPet var = (IEntityVillagerPet) entityPet;
            ProfessionWrapper typeID = ProfessionWrapper.FARMER;
            if (var.getProfession() != null) {
                typeID = var.getProfession();
            }
            switch (typeID) {
                case BLACKSMITH:
                    item = type.getDataItemByName("profession", 0);
                    break;
                case BUTCHER:
                    item = type.getDataItemByName("profession", 1);
                    break;
                case FARMER:
                    item = type.getDataItemByName("profession", 2);
                    break;
                case LIBRARIAN:
                    item = type.getDataItemByName("profession", 3);
                    break;
                case PRIEST:
                    item = type.getDataItemByName("profession", 4);
                    break;
                case NITWIT:
                    item = type.getDataItemByName("profession", 5);
                    break;
            }
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        ItemBuilder item = new ItemBuilder(Material.IRON_SWORD);
        item.withName("&6BlackSmith");
        items.add(item);
        item = new ItemBuilder(Material.COOKED_BEEF);
        item.withName("&6Butcher");
        items.add(item);
        item = new ItemBuilder(DataConverter.getMaterial("SEEDS","WHEAT_SEEDS"));
        item.withName("&6Farmer");
        items.add(item);
        item = new ItemBuilder(Material.BOOK);
        item.withName("&6Librarian");
        items.add(item);
        item = new ItemBuilder(Material.ENCHANTED_BOOK);
        item.withName("&6Priest");
        items.add(item);
        item = DataConverter.getColoredMaterial(DataConverter.MaterialType.INK_SACK, 2);
        item.withName("&6NitWit");
        items.add(item);
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityVillagerPet) {
            IEntityVillagerPet var = (IEntityVillagerPet) entityPet;
            ProfessionWrapper wrapper = ProfessionWrapper.FARMER;
            if (var.getProfession() != null)
                wrapper = var.getProfession();
            var.setProfession(ProfessionWrapper.getNext(wrapper));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IEntityVillagerPet) {
            IEntityVillagerPet var = (IEntityVillagerPet) entityPet;
            ProfessionWrapper wrapper = ProfessionWrapper.FARMER;
            if (var.getProfession() != null)
                wrapper = var.getProfession();
            var.setProfession(ProfessionWrapper.getPrevious(wrapper));
        }
    }

    @Override
    public boolean isSupported() {
        return ServerVersion.isEqualOld(ServerVersion.v1_13_R2);
    }
}
