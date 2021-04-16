package simplepets.brainsynder.api.event.inventory;

import lib.brainsynder.item.ItemBuilder;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;

import javax.annotation.Nonnull;

public class PetTypeStorage {
    private final PetType type;
    private ItemBuilder item;

    public PetTypeStorage(@Nonnull PetType type) {
        this.type = type;
        SimplePets.getPetConfigManager().getPetConfig(type).ifPresent(config -> {
            PetTypeStorage.this.item = config.getBuilder();
        });
    }

    public boolean isSimilar(@Nonnull PetTypeStorage storage) {
        return (type == storage.type) && (item.isSimilar(storage.item.build()));
    }

    public PetType getType() {return this.type;}

    public ItemBuilder getItemBuilder() {return this.item;}

    public PetTypeStorage setItem(ItemBuilder item) {
        this.item = item;
        return this;
    }
}