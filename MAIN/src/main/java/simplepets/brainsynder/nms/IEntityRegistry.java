package simplepets.brainsynder.nms;

import simplepets.brainsynder.wrapper.EntityWrapper;

public interface IEntityRegistry<E> extends IPetRegistry {

    Class<? extends E> getNMS();

    Class<? extends E> getPet();

    EntityWrapper getEntityWrapper();
}
