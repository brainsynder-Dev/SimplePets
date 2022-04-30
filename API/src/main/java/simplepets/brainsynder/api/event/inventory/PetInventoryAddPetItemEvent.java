package simplepets.brainsynder.api.event.inventory;

import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

/**
 * This event is called when a pets item is added to the Selection/Saves GUI
 */
public class PetInventoryAddPetItemEvent extends CancellablePetEvent {
    private final PetUser user;
    private final PetType type;
    private ItemStack item;
    private final CustomInventory inventory;

    public PetInventoryAddPetItemEvent(CustomInventory inventory, PetUser user, PetType type, ItemStack item) {
        this.inventory = inventory;
        this.user = user;
        this.type = type;
        this.item = item;
    }

    public PetType getType() {
        return type;
    }

    public PetUser getUser() {
        return this.user;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public CustomInventory getInventory() {
        return inventory;
    }
}
