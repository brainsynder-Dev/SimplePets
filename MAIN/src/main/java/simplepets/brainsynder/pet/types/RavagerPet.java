package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityRavagerPet;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.AdditionalData;
import simplepets.brainsynder.wrapper.EntityWrapper;

@AdditionalData(passive = false)
@SupportedVersion(version = ServerVersion.v1_14_R1)
public class RavagerPet extends PetType {
    public RavagerPet(PetCore plugin) {
        super(plugin, "ravager", SoundMaker.ENTITY_RAVAGER_AMBIENT, EntityWrapper.RAVAGER);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/79b625b80cfb0baf04eebbd2cb1ff9f1010b02f4df21b3baf86eb812ab7eba8b")
                .withName("&f&lRavager Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityRavagerPet.class;
    }
}
