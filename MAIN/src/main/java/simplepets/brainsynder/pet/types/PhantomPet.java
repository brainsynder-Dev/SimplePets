package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityPhantomPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

@SupportedVersion(version = ServerVersion.v1_13_R1)
public class PhantomPet extends PetType {
    public PhantomPet(PetCore plugin) {
        super(plugin, "phantom", SoundMaker.ENTITY_PHANTOM_AMBIENT, EntityWrapper.PHANTOM);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PHANTOM_MEMBRANE).withName("&f&lPhantom Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityPhantomPet.class;
    }

    @Override
    public boolean canFlyDefault() {
        return true;
    }

    @Override
    public PetData getPetData() {
        return PetData.SIZE;
    }
}
