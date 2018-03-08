package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class HorseDefault extends PetDefault {
    public HorseDefault(PetCore plugin) {
        super(plugin, "horse", SoundMaker.ENTITY_HORSE_AMBIENT, EntityWrapper.HORSE);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SKULL_ITEM).withData(3)
                .setTexture("http://textures.minecraft.net/texture/5c6d5abbf68ccb2386bf16af25ac38d8b77bb0e043152461bd97f3f630dbb8bc")
                .withName("&f&lHorse Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityHorsePet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.HORSE;
    }
}
