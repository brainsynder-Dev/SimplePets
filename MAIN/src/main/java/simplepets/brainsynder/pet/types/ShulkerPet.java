package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityControllerPet;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class ShulkerPet extends PetType {
    public ShulkerPet(PetCore plugin) {
        super(plugin, "shulker", SoundMaker.ENTITY_SHULKER_AMBIENT, EntityWrapper.SHULKER);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SHULKER_SHELL).withName("&f&lShulker Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityControllerPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.SHULKER;
    }
}
