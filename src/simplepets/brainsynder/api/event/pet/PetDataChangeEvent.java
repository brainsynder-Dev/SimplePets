package simplepets.brainsynder.api.event.pet;

import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.menu.MenuItem;

public class PetDataChangeEvent extends CancellablePetEvent {
    private MenuItem menuItem;
    private ClickType clickType;

    public PetDataChangeEvent(MenuItem item, ClickType clickType) {
        super(PetEventType.DATA_CHANGE);
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
