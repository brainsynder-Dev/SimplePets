package simplepets.brainsynder.pet.types;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityVexPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class VexDefault extends PetDefault {
    public VexDefault(PetCore plugin) {
        super(plugin, "vex", SoundMaker.ENTITY_VEX_AMBIENT, EntityWrapper.VEX);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return ItemBuilder.getSkull(simple.brainsynder.utils.SkullType.PLAYER)
                .setTexture("http://textures.minecraft.net/texture/5e7330c7d5cd8a0a55ab9e95321535ac7ae30fe837c37ea9e53bea7ba2de86b")
                .withName("&f&lVex Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityVexPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.POWERED;
    }
}
