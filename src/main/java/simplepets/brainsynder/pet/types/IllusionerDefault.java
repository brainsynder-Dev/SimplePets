package simplepets.brainsynder.pet.types;

import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityIllusionerPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class IllusionerDefault extends PetDefault {
    public IllusionerDefault(PetCore plugin) {
        super(plugin, "illusioner", SoundMaker.ENTITY_ILLUSION_ILLAGER_AMBIENT, EntityWrapper.ILLUSIONER);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return Utilities.getSkullMaterial(Utilities.SkullType.PLAYER).toBuilder(1)
                .setTexture("http://textures.minecraft.net/texture/1c678c9f4c6dd4d991930f82e6e7d8b89b2891f35cba48a4b18539bbe7ec927")
                .withName("&f&lIllusioner Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityIllusionerPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.WIZARD;
    }
}
