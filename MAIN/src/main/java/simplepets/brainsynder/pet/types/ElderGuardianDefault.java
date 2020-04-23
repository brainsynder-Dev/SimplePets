package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityElderGuardianPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class ElderGuardianDefault extends PetDefault {
    public ElderGuardianDefault(PetCore plugin) {
        super(plugin, "elder_guardian", SoundMaker.ENTITY_ELDER_GUARDIAN_AMBIENT, EntityWrapper.ELDER_GUARDIAN);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SEA_LANTERN).withName("&f&lElder Guardian Pet");
    }

    @Override
    public PetData getPetData() {
        return PetData.SILENT;
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityElderGuardianPet.class;
    }
}
