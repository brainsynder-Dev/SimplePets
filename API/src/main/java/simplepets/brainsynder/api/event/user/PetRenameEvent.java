package simplepets.brainsynder.api.event.user;

import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

/**
 * This event gets called when the player renames their pet
 *
 * Called before {@link PetNameChangeEvent}
 */
public class PetRenameEvent extends CancellablePetEvent {
    private final PetUser user;
    private final PetType type;
    private String name;

    public PetRenameEvent(PetUser user, PetType type, String name) {
        this.user = user;
        this.type = type;
        this.name = name;
    }

    public PetUser getUser() {
        return user;
    }

    public PetType getType() {
        return type;
    }

    /**
     * @return The new name of the PetType, Will return `null` if they are resetting the name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
