package simplepets.brainsynder.api.animation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovementFrames {
    private List<AnimationFrame> frames = new ArrayList<>();

    public MovementFrames(AnimationFrame... frames) {
        Collections.addAll(this.frames, frames);
    }

    public MovementFrames(List<AnimationFrame> frames) {
        this.frames = frames;
    }

    public List<AnimationFrame> getFrames() {
        return frames;
    }
}