package simplepets.brainsynder.api.inventory.handler;

import simplepets.brainsynder.api.inventory.CustomInventory;

import java.util.Optional;

public interface GUIHandler {
    /**
     * Registers the Inventory
     *
     * @param inventory - Inventory being registered
     */
    void register (Class<? extends CustomInventory> clazz);

    /**
     * Fetches the gui from the registered inventories
     *
     * @param clazz - The class you are looking for (EG: SelectionMenu.class)
     */
    <T extends CustomInventory> Optional<T> getInventory(Class<T> clazz);
}
