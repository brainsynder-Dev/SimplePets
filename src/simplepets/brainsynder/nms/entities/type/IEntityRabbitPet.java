package simplepets.brainsynder.nms.entities.type;

import simplepets.brainsynder.nms.entities.type.main.IAgeablePet;
import simplepets.brainsynder.wrapper.RabbitColor;

public interface IEntityRabbitPet extends IAgeablePet {
    RabbitColor getRabbitType();

    void setRabbitType(RabbitColor type);
}
