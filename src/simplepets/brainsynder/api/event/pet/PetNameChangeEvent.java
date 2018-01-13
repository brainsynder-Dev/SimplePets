package simplepets.brainsynder.api.event.pet;

import org.bukkit.entity.Player;
import simplepets.brainsynder.api.event.CancellablePetEvent;

public class PetNameChangeEvent extends CancellablePetEvent {
    private String newName;
    private Player player;
    private boolean useColor;
    private boolean useMagic;

    public PetNameChangeEvent(Player player, String newName, boolean useColor, boolean useMagic) {
        super(PetEventType.NAME_CHANGE);
        this.newName = newName;
        this.player = player;
        this.useColor = useColor;
        this.useMagic = useMagic;
    }

    public void setMagic(boolean useMagic) {
        useMagic = useMagic;
    }

    public void setColor(boolean useColor) {
        useColor = useColor;
    }

    public boolean canUseColor() {
        return useColor;
    }

    public boolean canUseMagic() {
        return useMagic;
    }

    public String getNewName() {return this.newName;}

    public Player getPlayer() {return this.player;}

    public void setNewName(String newName) {this.newName = newName; }
}
