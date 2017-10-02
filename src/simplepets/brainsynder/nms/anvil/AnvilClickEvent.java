package simplepets.brainsynder.nms.anvil;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.event.SimplePetEvent;

public class AnvilClickEvent extends SimplePetEvent {
    private AnvilSlot slot;
    private ItemStack item;
    private String name;
    private Inventory inv;
    private boolean close = true;
    private boolean destroy = true;
    private boolean cancel = true;

    public AnvilClickEvent(Inventory inv, AnvilSlot slot, String name, ItemStack item) {
        this.slot = slot;
        this.name = name;
        this.item = item;
        this.inv = inv;
    }

    public boolean isCanceled() {
        return this.cancel;
    }

    public void setCanceled(boolean value) {
        this.cancel = value;
    }

    public Inventory getAnvilInventory() {
        return this.inv;
    }

    public ItemStack getOutputItem() {
        return this.item;
    }

    public AnvilSlot getSlot() {
        return this.slot;
    }

    public String getName() {
        return this.name;
    }

    public boolean getWillClose() {
        return this.close;
    }

    public void setWillClose(boolean close) {
        this.close = close;
    }

    public boolean getWillDestroy() {
        return this.destroy;
    }

    public void setWillDestroy(boolean destroy) {
        this.destroy = destroy;
    }
}
