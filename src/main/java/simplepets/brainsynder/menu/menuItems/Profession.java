package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityVillagerPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.ProfessionWrapper;

public class Profession extends MenuItemAbstract {
    public Profession(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Profession(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = null;
        if (entityPet instanceof IEntityVillagerPet) {
            IEntityVillagerPet var = (IEntityVillagerPet) entityPet;
            ProfessionWrapper typeID = ProfessionWrapper.FARMER;
            if (var.getProfession() != null) {
                typeID = var.getProfession();
            }
            switch (typeID) {
                case BLACKSMITH:
                    item = new ItemBuilder(Material.IRON_SWORD);
                    item.withName("&6BlackSmith");
                    break;
                case BUTCHER:
                    item = new ItemBuilder(Material.COOKED_BEEF);
                    item.withName("&6Butcher");
                    break;
                case FARMER:
                    item = new ItemBuilder(Material.SEEDS);
                    item.withName("&6Farmer");
                    break;
                case LIBRARIAN:
                    item = new ItemBuilder(Material.BOOK);
                    item.withName("&6Librarian");
                    break;
                case PRIEST:
                    item = new ItemBuilder(Material.ENCHANTED_BOOK);
                    item.withName("&6Priest");
                    break;
                case NITWIT:
                    item = new ItemBuilder(Material.INK_SACK, (byte) 2);
                    item.withName("&6NitWit");
                    break;
            }
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = null;
        if (entityPet instanceof IEntityVillagerPet) {
            IEntityVillagerPet var = (IEntityVillagerPet) entityPet;
            ProfessionWrapper typeID = ProfessionWrapper.FARMER;
            if (var.getProfession() != null) {
                typeID = var.getProfession();
            }
            switch (typeID) {
                case BLACKSMITH:
                    item = new ItemBuilder(Material.IRON_SWORD);
                    item.withName("&6BlackSmith");
                    break;
                case BUTCHER:
                    item = new ItemBuilder(Material.COOKED_BEEF);
                    item.withName("&6Butcher");
                    break;
                case FARMER:
                    item = new ItemBuilder(Material.SEEDS);
                    item.withName("&6Farmer");
                    break;
                case LIBRARIAN:
                    item = new ItemBuilder(Material.BOOK);
                    item.withName("&6Librarian");
                    break;
                case PRIEST:
                    item = new ItemBuilder(Material.ENCHANTED_BOOK);
                    item.withName("&6Priest");
                    break;
                case NITWIT:
                    item = new ItemBuilder(Material.INK_SACK, (byte) 2);
                    item.withName("&6NitWit");
                    break;
            }
        }
        return item;
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
}
