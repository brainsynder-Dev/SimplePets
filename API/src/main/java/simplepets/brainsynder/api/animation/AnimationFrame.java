package simplepets.brainsynder.api.animation;

import lib.brainsynder.utils.AdvString;
import org.bukkit.util.EulerAngle;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;

public class AnimationFrame {
    private EulerAngle head;
    private EulerAngle body;
    private EulerAngle leftHand;
    private EulerAngle rightHand;
    private EulerAngle leftLeg;
    private EulerAngle rightLeg;

    public AnimationFrame setHead(EulerAngle head) {
        this.head = head;
        return this;
    }

    public AnimationFrame setBody(EulerAngle body) {
        this.body = body;
        return this;
    }

    public AnimationFrame setLeftHand(EulerAngle leftHand) {
        this.leftHand = leftHand;
        return this;
    }

    public AnimationFrame setRightHand(EulerAngle rightHand) {
        this.rightHand = rightHand;
        return this;
    }

    public AnimationFrame setLeftLeg(EulerAngle leftLeg) {
        this.leftLeg = leftLeg;
        return this;
    }

    public AnimationFrame setRightLeg(EulerAngle rightLeg) {
        this.rightLeg = rightLeg;
        return this;
    }

    public void setLocations(IEntityArmorStandPet armor) {
        armor.setHeadAngle(this.head == null ? armor.getHeadAngle() : this.head);
        armor.setBodyAngle(this.body == null ? armor.getBodyAngle() : this.body);
        armor.setLeftArmAngle(this.leftHand == null ? armor.getLeftArmAngle() : this.leftHand);
        armor.setRightArmAngle(this.rightHand == null ? armor.getRightArmAngle() : this.rightHand);
        armor.setLeftLegAngle(this.leftLeg == null ? armor.getLeftLegAngle() : this.leftLeg);
        armor.setRightLegAngle(this.rightLeg == null ? armor.getRightLegAngle() : this.rightLeg);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AnimationFrame{");
        if (head != null) builder.append("head=").append(formatAngle (head)).append(", ");
        if (body != null) builder.append("body=").append(formatAngle (body)).append(", ");
        if (leftHand != null) builder.append("leftHand=").append(formatAngle (leftHand)).append(", ");
        if (rightHand != null) builder.append("rightHand=").append(formatAngle (rightHand)).append(", ");
        if (leftLeg != null) builder.append("leftLeg=").append(formatAngle (leftLeg)).append(", ");
        if (rightLeg != null) builder.append("rightLeg=").append(formatAngle (rightLeg)).append(", ");
        return AdvString.replaceLast(", ", "}", builder.toString());
    }

    private String formatAngle (EulerAngle angle) {
        return "x: "+angle.getX() + " y: "+angle.getY() + " z: "+angle.getZ();
    }
}