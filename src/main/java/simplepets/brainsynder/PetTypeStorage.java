package simplepets.brainsynder;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.pet.PetType;

public class PetTypeStorage {
    @Getter
    private PetType type;
    @Getter
    @Setter
    private ItemStack item;

    public PetTypeStorage(PetType type) {
        this.type = type;
        this.item = type.getItem();
    }

    public boolean isSimilar(PetTypeStorage storage) {
        return (type == storage.type) && (storage.item.isSimilar(item));
    }
}
