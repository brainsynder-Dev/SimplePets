package simplepets.brainsynder.api.inventory.handler;

import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.api.inventory.Item;

import java.util.Optional;

public interface ItemHandler {
    /**
     * Registers the item so the names can be used in the GUIs
     *
     * @param item - Item being registered
     * @return
     *      true - Successfully registered
     *      false - Failed to register (name already exists?)
     */
    boolean register (Class<? extends Item> clazz);

    /**
     * Fetches the item from the registered items
     *
     * @param clazz - The class you are looking for (EG: Placeholder.class)
     */
    <T extends Item> Optional<T> getItem(Class<T> clazz);

    /**
     * Fetches the item from the registered items
     *
     * @param namespace - Name of the item
     */
    Optional<Item> getItem(String namespace);

    /**
     * Fetches the item from the registered items
     *
     * @param item - ItemStack being checked
     */
    Optional<Item> getItem(ItemStack item);
}
