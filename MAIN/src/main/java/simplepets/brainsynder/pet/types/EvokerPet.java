package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityEvokerPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class EvokerPet extends PetType {
    public EvokerPet(PetCore plugin) {
        super(plugin, "evoker", SoundMaker.ENTITY_EVOCATION_ILLAGER_AMBIENT, EntityWrapper.EVOKER);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/d954135dc82213978db478778ae1213591b93d228d36dd54f1ea1da48e7cba6")
                .withName("&f&lEvoker Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityEvokerPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.WIZARD;
    }
}
