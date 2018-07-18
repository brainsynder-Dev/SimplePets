package simplepets.brainsynder.pet.types;

import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityOcelotPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class OcelotDefault extends PetDefault {
    public OcelotDefault(PetCore plugin) {
        super(plugin, "ocelot", SoundMaker.ENTITY_CAT_AMBIENT, EntityWrapper.OCELOT);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return Utilities.getSkullMaterial(Utilities.SkullType.PLAYER).toBuilder(1)
                .setTexture("http://textures.minecraft.net/texture/5657cd5c2989ff97570fec4ddcdc6926a68a3393250c1be1f0b114a1db1")
                .withName("&f&lOcelot Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityOcelotPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.OCELOT;
    }
}
