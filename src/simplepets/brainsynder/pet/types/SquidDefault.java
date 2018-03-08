package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntitySquidPet;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class SquidDefault extends PetDefault {
    public SquidDefault(PetCore plugin) {
        super(plugin, "squid", SoundMaker.ENTITY_SQUID_AMBIENT, EntityWrapper.SQUID);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.INK_SACK).withName("&f&lSquid Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntitySquidPet.class;
    }
}
