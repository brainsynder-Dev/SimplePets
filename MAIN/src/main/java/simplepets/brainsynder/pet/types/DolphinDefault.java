package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityDolphinPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

@SupportedVersion(version = ServerVersion.v1_13_R1)
public class DolphinDefault extends PetDefault {
    public DolphinDefault(PetCore plugin) {
        super(plugin, "dolphin", SoundMaker.ENTITY_DOLPHIN_AMBIENT, EntityWrapper.DOLPHIN);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.HEART_OF_THE_SEA).withName("&f&lDolphin Pet");
    }

    @Override
    public PetData getPetData() {
        return PetData.SILENT;
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityDolphinPet.class;
    }
}
