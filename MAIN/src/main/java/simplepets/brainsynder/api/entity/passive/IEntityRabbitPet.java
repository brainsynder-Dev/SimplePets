package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.IAgeablePet;
import simplepets.brainsynder.wrapper.RabbitType;

public interface IEntityRabbitPet extends IAgeablePet {
    RabbitType getRabbitType();

    void setRabbitType(RabbitType type);
}
