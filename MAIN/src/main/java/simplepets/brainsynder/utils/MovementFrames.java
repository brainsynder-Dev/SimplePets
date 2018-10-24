package simplepets.brainsynder.utils;

import java.util.ArrayList;
import java.util.Collections;

public class MovementFrames {
    private ArrayList<AnimationFrame> frames = new ArrayList<>();

    public MovementFrames(AnimationFrame... frames) {
        Collections.addAll(this.frames, frames);
    }

    public ArrayList<AnimationFrame> getFrames() {
        return frames;
    }
}
