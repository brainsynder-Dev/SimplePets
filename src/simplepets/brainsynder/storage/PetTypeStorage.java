package simplepets.brainsynder.storage;

import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

public class PetTypeStorage {
    private PetDefault type;
    private ItemBuilder item;

    public PetTypeStorage(PetDefault type) {
        this.type = type;
        this.item = type.getItemBuilder();
    }

    public boolean isSimilar(PetTypeStorage storage) {
        return (type == storage.type) && (item.isSimilar(storage.item.build()));
    }

    public PetDefault getType() {return this.type;}

    public ItemStack getItem() {return this.item.build();}

    public ItemBuilder getItemBuilder() {return this.item;}

    public void setItem(ItemBuilder item) {this.item = item; }
}
