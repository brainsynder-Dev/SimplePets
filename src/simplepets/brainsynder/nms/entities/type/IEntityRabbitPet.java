package simplepets.brainsynder.nms.entities.type;

import simplepets.brainsynder.nms.entities.type.main.IAgeablePet;
import simplepets.brainsynder.wrapper.RabbitType;

public interface IEntityRabbitPet extends IAgeablePet {
    RabbitType getRabbitType();

    void setRabbitType(RabbitType type);
}
