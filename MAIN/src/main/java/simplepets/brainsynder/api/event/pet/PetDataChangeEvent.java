package simplepets.brainsynder.api.event.pet;

import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.menu.menuItems.base.MenuItem;

public class PetDataChangeEvent extends CancellablePetEvent {
    private final MenuItem menuItem;
    private final ClickType clickType;

    public PetDataChangeEvent(MenuItem item, ClickType clickType) {
        menuItem = item;
        this.clickType = clickType;
    }

    public MenuItem getMenuItem() {return this.menuItem;}

    public ClickType getClickType() {return this.clickType;}

    public enum ClickType {
        RIGHT_CLICK,
        LEFT_CLICK
    }
}
