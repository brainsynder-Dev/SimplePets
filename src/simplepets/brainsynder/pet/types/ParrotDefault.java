package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityParrotPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class ParrotDefault extends PetDefault {
    public ParrotDefault(PetCore plugin) {
        super(plugin, "parrot", SoundMaker.ENTITY_PARROT_STEP, EntityWrapper.PARROT);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SKULL_ITEM).withData(3)
                .setTexture("http://textures.minecraft.net/texture/a4ba8d66fecb1992e94b8687d6ab4a5320ab7594ac194a2615ed4df818edbc3")
                .withName("&f&lParrot Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityParrotPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.PARROT;
    }
}
