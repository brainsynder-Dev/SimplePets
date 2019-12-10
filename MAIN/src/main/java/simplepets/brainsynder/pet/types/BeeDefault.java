package simplepets.brainsynder.pet.types;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityBeePet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class BeeDefault extends PetDefault {
    public BeeDefault(PetCore plugin) {
        super(plugin, "bee", SoundMaker.ENTITY_BEE_LOOP, EntityWrapper.BEE);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return ItemBuilder.getSkull(simple.brainsynder.utils.SkullType.PLAYER)
                .setTexture("http://textures.minecraft.net/texture/fa2cb74c13245d3ce9bacc8b1600af02fd7c91f501feaf97364e1f8b6f04f47f")
                .withName("&f&lBee Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityBeePet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.BEE;
    }

    @Override
    public boolean canFlyDefault() {
        return true;
    }
}
