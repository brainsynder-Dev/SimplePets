package simplepets.brainsynder.storage;

import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.pet.PetType;

public class PetTypeStorage {
    private PetType type;
    private ItemStack item;

    public PetTypeStorage(PetType type) {
        this.type = type;
        this.item = type.getItem();
    }

    public boolean isSimilar(PetTypeStorage storage) {
        return (type == storage.type) && (storage.item.isSimilar(item));
    }

    public PetType getType() {return this.type;}

    public ItemStack getItem() {return this.item;}

    public void setItem(ItemStack item) {this.item = item; }
}
