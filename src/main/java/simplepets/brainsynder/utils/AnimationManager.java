package simplepets.brainsynder.utils;

import org.bukkit.util.EulerAngle;

/**
 * This class was pulled form my SimpleMobs plugin.
 */
public class AnimationManager {

    public static final MovementFrames walk = new MovementFrames(
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(0.5D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(-0.5D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(0.4D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(-0.4D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(0.3D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(-0.3D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(0.2D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(-0.2D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(0.1D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(-0.1D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(0.0D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(0.0D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(-0.1D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(0.1D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(-0.2D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(0.2D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(-0.3D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(0.3D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(-0.4D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(0.4D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(-0.5D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(0.5D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(-0.4D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(0.4D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(-0.3D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(0.3D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(-0.2D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(0.2D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(-0.1D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(0.1D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(0.0D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(0.0D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(0.1D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(-0.1D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(0.2D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(-0.2D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(0.3D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(-0.3D, 0.0D, 0.0D)),
            (new AnimationFrame())
                    .setLeftLeg(new EulerAngle(0.4D, 0.0D, 0.0D))
                    .setRightLeg(new EulerAngle(-0.4D, 0.0D, 0.0D)));
    public static final MovementFrames arm_swing = new MovementFrames(
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(0.5, 0, 0))
                    .setRightHand(new EulerAngle(-0.5, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(0.4, 0, 0))
                    .setRightHand(new EulerAngle(-0.4, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(0.3, 0, 0))
                    .setRightHand(new EulerAngle(-0.3, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(0.2, 0, 0))
                    .setRightHand(new EulerAngle(-0.2, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(0.1, 0, 0))
                    .setRightHand(new EulerAngle(-0.1, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(0, 0, 0))
                    .setRightHand(new EulerAngle(0, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(-0.1, 0, 0))
                    .setRightHand(new EulerAngle(0.1, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(-0.2, 0, 0))
                    .setRightHand(new EulerAngle(0.2, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(-0.3, 0, 0))
                    .setRightHand(new EulerAngle(0.3, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(-0.4, 0, 0))
                    .setRightHand(new EulerAngle(0.4, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(-0.5, 0, 0))
                    .setRightHand(new EulerAngle(0.5, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(-0.4, 0, 0))
                    .setRightHand(new EulerAngle(0.4, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(-0.3, 0, 0))
                    .setRightHand(new EulerAngle(0.3, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(-0.2, 0, 0))
                    .setRightHand(new EulerAngle(0.2, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(-0.1, 0, 0))
                    .setRightHand(new EulerAngle(0.1, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(0, 0, 0))
                    .setRightHand(new EulerAngle(0, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(0.1, 0, 0))
                    .setRightHand(new EulerAngle(-0.1, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(0.2, 0, 0))
                    .setRightHand(new EulerAngle(-0.2, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(0.3, 0, 0))
                    .setRightHand(new EulerAngle(-0.3, 0, 0)),
            (new AnimationFrame())
                    .setLeftHand(new EulerAngle(0.4, 0, 0))
                    .setRightHand(new EulerAngle(-0.4, 0, 0))
    );
}
