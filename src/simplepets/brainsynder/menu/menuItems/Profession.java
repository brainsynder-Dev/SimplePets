package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntityVillagerPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.ProfessionWrapper;

public class Profession extends MenuItemAbstract {
    public Profession(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        ItemMaker item = null;
        if (entityPet instanceof IEntityVillagerPet) {
            IEntityVillagerPet var = (IEntityVillagerPet) entityPet;
            ProfessionWrapper typeID = ProfessionWrapper.FARMER;
            if (var.getProfession() != null) {
                typeID = var.getProfession();
            }
            switch (typeID) {
                case BLACKSMITH:
                    item = new ItemMaker(Material.IRON_SWORD);
                    item.setName("&6BlackSmith");
                    break;
                case BUTCHER:
                    item = new ItemMaker(Material.COOKED_BEEF);
                    item.setName("&6Butcher");
                    break;
                case FARMER:
                    item = new ItemMaker(Material.SEEDS);
                    item.setName("&6Farmer");
                    break;
                case LIBRARIAN:
                    item = new ItemMaker(Material.BOOK);
                    item.setName("&6Librarian");
                    break;
                case PRIEST:
                    item = new ItemMaker(Material.ENCHANTED_BOOK);
                    item.setName("&6Priest");
                    break;
                case NITWIT:
                    item = new ItemMaker(Material.INK_SACK, (byte) 2);
                    item.setName("&6NitWit");
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
