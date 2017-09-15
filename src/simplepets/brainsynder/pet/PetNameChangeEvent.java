package simplepets.brainsynder.pet;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import simplepets.brainsynder.event.CancellablePetEvent;

public class PetNameChangeEvent extends CancellablePetEvent {
    @Getter
    @Setter
    private String newName;
    @Getter
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
}
