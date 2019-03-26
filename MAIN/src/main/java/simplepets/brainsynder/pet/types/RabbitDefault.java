package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityRabbitPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class RabbitDefault extends PetDefault {
    public RabbitDefault(PetCore plugin) {
        super(plugin, "rabbit", SoundMaker.ENTITY_RABBIT_AMBIENT, EntityWrapper.RABBIT);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.RABBIT_FOOT).withName("&f&lRabbit Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityRabbitPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.RABBIT;
    }
}
