package simplepets.brainsynder.managers;

import simplepets.brainsynder.api.animation.AnimationFrame;
import simplepets.brainsynder.api.animation.MovementFrames;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.debug.DebugLevel;

import java.util.List;

public class AnimationController {
    private final IEntityArmorStandPet standPet;
    private final List<AnimationFrame> frames;
    private int currentFrame = 0;

    public AnimationController(IEntityArmorStandPet standPet, MovementFrames frames) {
        this.frames = frames.getFrames();
        this.standPet = standPet;
    }

    public void cycleFrame () {
        if (this.currentFrame == this.frames.size()) this.currentFrame = 0;

        try {
            this.frames.get(this.currentFrame).setLocations(this.standPet);
            if (currentFrame != 0) SimplePets.getDebugLogger().debug(DebugLevel.DEBUG, "Frame: " + currentFrame);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.currentFrame++;
    }

    public AnimationController reset() {
        currentFrame = 0;
        return this;
    }
}