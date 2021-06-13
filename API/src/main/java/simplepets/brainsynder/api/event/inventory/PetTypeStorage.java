package simplepets.brainsynder.api.event.inventory;

import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;

public class PetTypeStorage {
    private final PetType type;
    private ItemStack item;

    public PetTypeStorage(PetType type) {
        this.type = type;
        SimplePets.getPetConfigManager().getPetConfig(type).ifPresent(config -> {
            PetTypeStorage.this.item = config.getBuilder().build();
        });
    }

    public PetTypeStorage(PetType type, ItemStack item) {
        this.type = type;
        this.item = item;
    }

    public boolean isSimilar(PetTypeStorage storage) {
        return (type == storage.type) && (item.isSimilar(storage.item));
    }

    public PetType getType() {return this.type;}

    public ItemStack getItem() {return this.item;}

    public PetTypeStorage setItem(ItemStack item) {
        this.item = item;
        return this;
    }
}