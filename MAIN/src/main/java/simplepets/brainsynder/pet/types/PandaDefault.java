package simplepets.brainsynder.pet.types;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityPandaPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class PandaDefault extends PetDefault {
    public PandaDefault(PetCore plugin) {
        super(plugin, "panda", SoundMaker.ENTITY_PANDA_AMBIENT, EntityWrapper.PANDA);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return ItemBuilder.getSkull(simple.brainsynder.utils.SkullType.PLAYER)
                .setTexture("http://textures.minecraft.net/texture/dca096eea506301bea6d4b17ee1605625a6f5082c71f74a639cc940439f47166")
                .withName("&f&lPanda Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityPandaPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.PANDA;
    }

    @Override
    public ServerVersion getAllowedVersion() {
        return ServerVersion.v1_14_R1;
    }

    @Override
    public boolean isSupported() {
        return ServerVersion.isEqualNew(ServerVersion.v1_14_R1);
    }
}
