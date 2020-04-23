package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityControllerPet;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class ArmorStandDefault extends PetDefault {
    public ArmorStandDefault(PetCore plugin) {
        super(plugin, "armor_stand", SoundMaker.ENTITY_ARMORSTAND_FALL, EntityWrapper.ARMOR_STAND);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.ARMOR_STAND).withName("&f&lArmorStand Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityControllerPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.ARMOR_STAND;
    }
}
