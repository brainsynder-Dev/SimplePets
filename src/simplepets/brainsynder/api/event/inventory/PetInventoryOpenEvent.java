package simplepets.brainsynder.api.event.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simplepets.brainsynder.PetTypeStorage;
import simplepets.brainsynder.api.event.CancellablePetEvent;

public class PetInventoryOpenEvent extends CancellablePetEvent {
    private IStorage<PetTypeStorage> shownPetTypes = new StorageList<>();
    private Player player;
    private IStorage<ItemStack> items = new StorageList<>();

    public PetInventoryOpenEvent(IStorage<PetTypeStorage> petTypes, Player player) {
        super(PetEventType.INVENTORY_OPEN);
        this.shownPetTypes = petTypes;
        this.player = player;
        if (shownPetTypes != null)
            while (shownPetTypes.hasNext()) {
                PetTypeStorage type = shownPetTypes.next();
                items.add(type.getItem());
            }
    }

    public IStorage<PetTypeStorage> getShownPetTypes() {return this.shownPetTypes;}

    public Player getPlayer() {return this.player;}

    public IStorage<ItemStack> getItems() {return this.items;}

    public void setItems(IStorage<ItemStack> items) {this.items = items; }
}
