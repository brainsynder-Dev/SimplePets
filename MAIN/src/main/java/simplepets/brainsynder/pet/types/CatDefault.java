package simplepets.brainsynder.pet.types;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityCatPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class CatDefault extends PetDefault {
    public CatDefault(PetCore plugin) {
        super(plugin, "cat", SoundMaker.ENTITY_CAT_AMBIENT, EntityWrapper.CAT);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return ItemBuilder.getSkull(simple.brainsynder.utils.SkullType.PLAYER)
                .setTexture("http://textures.minecraft.net/texture/16df72c34e7fdad4bea41d96678b72f29f606e2ca75e3590a278932714be98")
                .withName("&f&lCat Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityCatPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.OCELOT;
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
