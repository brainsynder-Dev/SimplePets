package simplepets.brainsynder.api.inventory.handler;

public interface InventoryType {
    InventoryType SUMMON_GUI = () -> "pet-summon";
    InventoryType TYPE_SELECTION_GUI = () -> "pet-type-selection";
    InventoryType SAVES_GUI = () -> "pet-saves";
    InventoryType DATA_GUI = () -> "pet-data";
    InventoryType ADDON_GUI = () -> "pet-addons";

    String getRawType ();
}
