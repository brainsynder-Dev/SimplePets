package simplepets.brainsynder.nms;

import net.minecraft.world.entity.Entity;
import simplepets.brainsynder.nms.helper.VersionTranslator;

public class NMSVersionTranslator implements VersionTranslator {
    @Override
    public String getVersionIdentifier() {
        return "1.21.11";
    }

    @Override
    public void setInvulnerable (Entity entity, boolean invulnerable) {
        entity.setInvulnerable(invulnerable);
    }
}
