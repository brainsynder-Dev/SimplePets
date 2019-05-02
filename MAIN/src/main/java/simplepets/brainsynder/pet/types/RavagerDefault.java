package simplepets.brainsynder.pet.types;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityRavagerPet;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class RavagerDefault extends PetDefault {
    public RavagerDefault(PetCore plugin) {
        super(plugin, "ravager", SoundMaker.ENTITY_RAVAGER_AMBIENT, EntityWrapper.RAVAGER);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return ItemBuilder.getSkull(simple.brainsynder.utils.SkullType.PLAYER)
                .setTexture("http://textures.minecraft.net/texture/79b625b80cfb0baf04eebbd2cb1ff9f1010b02f4df21b3baf86eb812ab7eba8b")
                .withName("&f&lRavager Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityRavagerPet.class;
    }

    @Override
    public ServerVersion getAllowedVersion() {
        return ServerVersion.v1_14_R1;
    }
}
