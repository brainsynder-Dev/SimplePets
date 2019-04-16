package simplepets.brainsynder.pet.types;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityFoxPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class FoxDefault extends PetDefault {
    public FoxDefault(PetCore plugin) {
        super(plugin, "fox", SoundMaker.ENTITY_FOX_AMBIENT, EntityWrapper.FOX);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return ItemBuilder.getSkull(simple.brainsynder.utils.SkullType.PLAYER)
                .setTexture("http://textures.minecraft.net/texture/d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a")
                .withName("&f&lFox Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityFoxPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.FOX;
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
