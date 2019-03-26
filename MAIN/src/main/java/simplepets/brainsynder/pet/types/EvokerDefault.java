package simplepets.brainsynder.pet.types;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityEvokerPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class EvokerDefault extends PetDefault {
    public EvokerDefault(PetCore plugin) {
        super(plugin, "evoker", SoundMaker.ENTITY_EVOCATION_ILLAGER_AMBIENT, EntityWrapper.EVOKER);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return ItemBuilder.getSkull(simple.brainsynder.utils.SkullType.PLAYER)
                .setTexture("http://textures.minecraft.net/texture/d954135dc82213978db478778ae1213591b93d228d36dd54f1ea1da48e7cba6")
                .withName("&f&lEvoker Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityEvokerPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.WIZARD;
    }
}
