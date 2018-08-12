package simplepets.brainsynder.api.entity.ambient;

import org.bukkit.util.EulerAngle;
import simplepets.brainsynder.api.entity.IDisplayEntity;

public interface IEntityArmorStandPet extends IDisplayEntity {
    boolean isSmall();
    void setSmall(boolean flag);

    boolean isOwner();
    void setOwner(boolean flag);

    default EulerAngle getHeadPose () { return new EulerAngle(0,0,0); }
    default EulerAngle getBodyPose () { return new EulerAngle(0,0,0); }
    default EulerAngle getLeftArmPose () { return new EulerAngle(0,0,0); }
    default EulerAngle getRightArmPose () { return new EulerAngle(0,0,0); }
    default EulerAngle getLeftLegPose () { return new EulerAngle(0,0,0); }
    default EulerAngle getRightLegPose () { return new EulerAngle(0,0,0); }

    default void setHeadPose(EulerAngle vector) {}
    default void setBodyPose(EulerAngle vector) {}
    default void setLeftArmPose(EulerAngle vector) {}
    default void setRightArmPose(EulerAngle vector) {}
    default void setLeftLegPose(EulerAngle vector) {}
    default void setRightLegPose(EulerAngle vector) {}

    void setBasePlate(boolean flag);
    boolean hasBasePlate();

    boolean hasArms();
    void setArms(boolean flag);
}
