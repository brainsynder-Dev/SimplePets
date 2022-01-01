package simplepets.brainsynder.managers;

import com.google.common.collect.Lists;
import org.bukkit.util.EulerAngle;
import simplepets.brainsynder.api.animation.AnimationFrame;
import simplepets.brainsynder.api.animation.MovementFrames;

public class AnimationManager {
    public static final MovementFrames WALKING_ANIMATION;

    static {
        WALKING_ANIMATION = new MovementFrames(Lists.newArrayList(new AnimationFrame()
                        .setLeftLeg(new EulerAngle(0, 0.0D, 0.0D))
                        .setRightLeg(new EulerAngle(0, 0.0D, 0.0D))
                        .setLeftHand(new EulerAngle(0, 0, 0))
                        .setRightHand(new EulerAngle(0, 0, 0)),
                new AnimationFrame()
                        .setLeftLeg(new EulerAngle(0.1, 0.0D, 0.0D))
                        .setRightLeg(new EulerAngle(-0.1, 0.0D, 0.0D))
                        .setLeftHand(new EulerAngle(-0.1, 0, 0))
                        .setRightHand(new EulerAngle(0.1, 0, 0)),
                new AnimationFrame()
                        .setLeftLeg(new EulerAngle(0.2, 0.0D, 0.0D))
                        .setRightLeg(new EulerAngle(-0.2, 0.0D, 0.0D))
                        .setLeftHand(new EulerAngle(-0.2, 0, 0))
                        .setRightHand(new EulerAngle(0.2, 0, 0)),
                new AnimationFrame()
                        .setLeftLeg(new EulerAngle(0.3, 0.0D, 0.0D))
                        .setRightLeg(new EulerAngle(-0.3, 0.0D, 0.0D))
                        .setLeftHand(new EulerAngle(-0.3, 0, 0))
                        .setRightHand(new EulerAngle(0.3, 0, 0)),
                new AnimationFrame()
                        .setLeftLeg(new EulerAngle(0.4, 0.0D, 0.0D))
                        .setRightLeg(new EulerAngle(-0.4, 0.0D, 0.0D))
                        .setLeftHand(new EulerAngle(-0.4, 0, 0))
                        .setRightHand(new EulerAngle(0.4, 0, 0)),
                new AnimationFrame()
                        .setLeftLeg(new EulerAngle(-0.3, 0.0D, 0.0D))
                        .setRightLeg(new EulerAngle(0.3, 0.0D, 0.0D))
                        .setLeftHand(new EulerAngle(0.3, 0, 0))
                        .setRightHand(new EulerAngle(-0.3, 0, 0)),
                new AnimationFrame()
                        .setLeftLeg(new EulerAngle(-0.2, 0.0D, 0.0D))
                        .setRightLeg(new EulerAngle(0.2, 0.0D, 0.0D))
                        .setLeftHand(new EulerAngle(0.2, 0, 0))
                        .setRightHand(new EulerAngle(-0.2, 0, 0)),
                new AnimationFrame()
                        .setLeftLeg(new EulerAngle(-0.1, 0.0D, 0.0D))
                        .setRightLeg(new EulerAngle(0.1, 0.0D, 0.0D))
                        .setLeftHand(new EulerAngle(0.1, 0, 0))
                        .setRightHand(new EulerAngle(-0.1, 0, 0))
        ));

        // This bit of code is used to generate the code above...
//        double value = 0.0;
//        List<AnimationFrame> frames = new ArrayList<>();
//        boolean negative = false;
//
//        String positiveKey = "new AnimationFrame()\n" +
//                "                    .setLeftLeg(new EulerAngle({value}, 0.0D, 0.0D))\n" +
//                "                    .setRightLeg(new EulerAngle(-{value}, 0.0D, 0.0D))\n" +
//                "                    .setLeftHand(new EulerAngle(-{value}, 0, 0))\n" +
//                "                    .setRightHand(new EulerAngle({value}, 0, 0))";
//
//        String negativeKey = "new AnimationFrame()\n" +
//                "                    .setLeftLeg(new EulerAngle(-{value}, 0.0D, 0.0D))\n" +
//                "                    .setRightLeg(new EulerAngle({value}, 0.0D, 0.0D))\n" +
//                "                    .setLeftHand(new EulerAngle({value}, 0, 0))\n" +
//                "                    .setRightHand(new EulerAngle(-{value}, 0, 0))";
//
//        StringBuilder builder = new StringBuilder("new MovementFrames(Lists.newArrayList(");
//        for (int i = 0; i < 18; i++) {
//            if (i >= 9) negative = true;
//
//            if (negative) {
//                frames.add(
//                        new AnimationFrame()
//                                .setLeftLeg(new EulerAngle(-value, 0.0D, 0.0D))
//                                .setRightLeg(new EulerAngle(value, 0.0D, 0.0D))
//                                .setLeftHand(new EulerAngle(value, 0.0D, 0.0D))
//                                .setRightHand(new EulerAngle(-value, 0.0D, 0.0D))
//                );
//                builder.append(negativeKey.replace("{value}", String.valueOf(value))).append(",  \n");
//                value = (value - 0.05);
//            } else {
//                frames.add(
//                        new AnimationFrame()
//                                .setLeftLeg(new EulerAngle(value, 0.0D, 0.0D))
//                                .setRightLeg(new EulerAngle(-value, 0.0D, 0.0D))
//                                .setLeftHand(new EulerAngle(-value, 0.0D, 0.0D))
//                                .setRightHand(new EulerAngle(value, 0.0D, 0.0D))
//                );
//                builder.append(positiveKey.replace("{value}", String.valueOf(value))).append(", \n");
//                value = (value + 0.05);
//            }
//        }
//
//        SimplePets.getDebugLogger().debug(DebugLevel.DEBUG, "Movement: " + AdvString.replaceLast(", ", "", builder.toString()) + "));");
    }
}