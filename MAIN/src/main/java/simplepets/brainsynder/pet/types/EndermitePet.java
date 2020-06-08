package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityEndermitePet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class EndermitePet extends PetType {
    public EndermitePet(PetCore plugin) {
        super(plugin, "endermite", SoundMaker.ENTITY_ENDERMITE_AMBIENT, EntityWrapper.ENDERMITE);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.ENDER_EYE).withName("&f&lEndermite Pet");
    }

    @Override
    public PetData getPetData() {
        return PetData.SILENT;
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityEndermitePet.class;
    }
}
