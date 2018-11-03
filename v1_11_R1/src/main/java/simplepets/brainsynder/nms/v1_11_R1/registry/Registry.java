package simplepets.brainsynder.nms.v1_11_R1.registry;

import simplepets.brainsynder.nms.IPetRegistry;

public class Registry implements IPetRegistry {
    @Override
    public void registerPets() {
        for (PetRegister register : PetRegister.values())
            register.registerPet();
    }
}
