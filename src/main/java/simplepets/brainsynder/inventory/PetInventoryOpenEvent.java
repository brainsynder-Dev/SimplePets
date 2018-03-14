package simplepets.brainsynder.inventory;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simplepets.brainsynder.PetTypeStorage;
import simplepets.brainsynder.event.CancellablePetEvent;

public class PetInventoryOpenEvent extends CancellablePetEvent {
    @Getter
    private IStorage<PetTypeStorage> shownPetTypes = new StorageList<>();
    @Getter
    private Player player;
    @Getter
    @Setter
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
}
