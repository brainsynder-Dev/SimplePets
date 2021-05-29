package simplepets.brainsynder.api.event.inventory;

import lib.brainsynder.storage.IStorage;
import lib.brainsynder.storage.StorageList;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.api.user.PetUser;

import java.util.List;

/**
 * This event is called when the player opens the pet selector GUI
 */
public class PetInventoryOpenEvent extends CancellablePetEvent {
    private IStorage<PetTypeStorage> shownPetTypes = new StorageList<>();
    private final PetUser user;
    private IStorage<ItemStack> items = new StorageList<>();

    public PetInventoryOpenEvent(IStorage<PetTypeStorage> petTypes, PetUser user) {
        this(petTypes.toArrayList(), user);
    }

    public PetInventoryOpenEvent(List<PetTypeStorage> petTypes, PetUser user) {
        this.shownPetTypes = new StorageList<>(petTypes);
        this.user = user;
        if (shownPetTypes != null)
            while (shownPetTypes.hasNext()) {
                PetTypeStorage type = shownPetTypes.next();
                items.add(type.getItem());
            }
    }

    public IStorage<PetTypeStorage> getShownPetTypes() {return this.shownPetTypes;}

    public PetUser getUser() {return this.user;}

    public IStorage<ItemStack> getItems() {return this.items;}

    public void setItems(IStorage<ItemStack> items) {this.items = items; }
}
