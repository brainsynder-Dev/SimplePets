package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntitySalmonPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

@SupportedVersion(version = ServerVersion.v1_13_R1)
public class SalmonPet extends PetType {
    public SalmonPet(PetCore plugin) {
        super(plugin, "salmon", SoundMaker.ENTITY_SALMON_AMBIENT, EntityWrapper.SALMON);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SALMON).withName("&f&lSalmon Pet");
    }

    @Override
    public PetData getPetData() {
        return PetData.SILENT;
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntitySalmonPet.class;
    }
}
