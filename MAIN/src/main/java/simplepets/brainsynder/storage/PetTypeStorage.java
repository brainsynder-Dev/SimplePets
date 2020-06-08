package simplepets.brainsynder.storage;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.pet.PetType;

public class PetTypeStorage {
    private final PetType type;
    private ItemBuilder item;

    public PetTypeStorage(PetType type) {
        this.type = type;
        this.item = type.getItemBuilder();
    }

    public boolean isSimilar(PetTypeStorage storage) {
        return (type == storage.type) && (item.isSimilar(storage.item.build()));
    }

    public PetType getType() {return this.type;}

    public ItemStack getItem() {return this.item.build();}

    public ItemBuilder getItemBuilder() {return this.item;}

    public void setItem(ItemBuilder item) {this.item = item; }
}
