package simplepets.brainsynder.api.wrappers;

import java.util.LinkedList;

public enum EntityPose {
    STANDING,
    FALL_FLYING,
    SLEEPING,
    SWIMMING,
    SPIN_ATTACK,
    CROUCHING,
    LONG_JUMPING,
    DYING,

    /**
     * Warden Poses
     */
    ROARING,
    SNIFFING,
    EMERGING,
    DIGGING,

    /**
     * Frog Poses
     */
    CROAKING,
    USING_TONGUE;


    public static EntityPose getByName(String name) {
        for (EntityPose wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return STANDING;
    }

    public static EntityPose getPrevious(EntityPose current, LinkedList<EntityPose> poseList) {
        if (poseList.isEmpty()) return STANDING;
        int index = 0;

        for (EntityPose pose : poseList) {
            if (pose == current) break;
            index++;
        }

        int target = index-1;

        if (target < 0) target = (poseList.size()-1);
        return poseList.get(target);
    }

    public static EntityPose getNext(EntityPose current, LinkedList<EntityPose> poseList) {
        if (poseList.isEmpty()) return STANDING;
        int index = 0;

        for (EntityPose pose : poseList) {
            if (pose == current) break;
            index++;
        }

        int target = index+1;

        if (target > (poseList.size()-1)) target = 0;
        return poseList.get(target);
    }
}