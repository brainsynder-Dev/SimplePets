package simplepets.brainsynder.utils;

import lombok.Getter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public class AnimationFrame {
    @Getter
    private EulerAngle head;
    @Getter
    private EulerAngle body;
    @Getter
    private EulerAngle leftHand;
    @Getter
    private EulerAngle rightHand;
    @Getter
    private EulerAngle leftLeg;
    @Getter
    private EulerAngle rightLeg;

    public AnimationFrame setHead(EulerAngle head) {
        this.head = head;
        return this;
    }

    public AnimationFrame setBody(EulerAngle body) {
        this.body = body;
        return this;
    }

    AnimationFrame setLeftHand(EulerAngle leftHand) {
        this.leftHand = leftHand;
        return this;
    }

    AnimationFrame setRightHand(EulerAngle rightHand) {
        this.rightHand = rightHand;
        return this;
    }

    AnimationFrame setLeftLeg(EulerAngle leftLeg) {
        this.leftLeg = leftLeg;
        return this;
    }

    AnimationFrame setRightLeg(EulerAngle rightLeg) {
        this.rightLeg = rightLeg;
        return this;
    }

    void setLocations(ArmorStand armor) {
        armor.setHeadPose(this.head == null ? armor.getHeadPose() : this.head);
        armor.setBodyPose(this.body == null ? armor.getBodyPose() : this.body);
        armor.setLeftArmPose(this.leftHand == null ? armor.getLeftArmPose() : this.leftHand);
        armor.setRightArmPose(this.rightHand == null ? armor.getRightArmPose() : this.rightHand);
        armor.setLeftLegPose(this.leftLeg == null ? armor.getLeftLegPose() : this.leftLeg);
        armor.setRightLegPose(this.rightLeg == null ? armor.getRightLegPose() : this.rightLeg);
    }
}
