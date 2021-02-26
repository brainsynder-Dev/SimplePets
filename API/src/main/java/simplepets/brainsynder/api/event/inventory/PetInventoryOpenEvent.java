package simplepets.brainsynder.api.event.inventory;

import lib.brainsynder.storage.IStorage;
import lib.brainsynder.storage.StorageList;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.api.event.CancellablePetEvent;

import java.util.List;

public class PetInventoryOpenEvent extends CancellablePetEvent {
    private IStorage<PetTypeStorage> shownPetTypes = new StorageList<>();
    private final Player player;
    private IStorage<ItemStack> items = new StorageList<>();

    public PetInventoryOpenEvent(IStorage<PetTypeStorage> petTypes, Player player) {
        this(petTypes.toArrayList(), player);
    }

    public PetInventoryOpenEvent(List<PetTypeStorage> petTypes, Player player) {
        this.shownPetTypes = new StorageList<>(petTypes);
        this.player = player;
        if (shownPetTypes != null)
            while (shownPetTypes.hasNext()) {
                PetTypeStorage type = shownPetTypes.next();
                items.add(type.getItemBuilder().build());
            }
    }

    public IStorage<PetTypeStorage> getShownPetTypes() {return this.shownPetTypes;}

    public Player getPlayer() {return this.player;}

    public IStorage<ItemStack> getItems() {return this.items;}

    public void setItems(IStorage<ItemStack> items) {this.items = items; }
}
