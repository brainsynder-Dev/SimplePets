package simplepets.brainsynder.pet;

import lombok.Getter;
import simplepets.brainsynder.event.CancellablePetEvent;
import simplepets.brainsynder.menu.MenuItem;

public class PetDataChangeEvent extends CancellablePetEvent {
    @Getter
    private MenuItem menuItem;
    @Getter
    private ClickType clickType;

    public PetDataChangeEvent(MenuItem item, ClickType clickType) {
        super(PetEventType.DATA_CHANGE);
        menuItem = item;
        this.clickType = clickType;
    }

    public enum ClickType {
        RIGHT_CLICK,
        LEFT_CLICK
    }
}
