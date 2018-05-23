package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityIronGolemPet;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class IronGolemDefault extends PetDefault {
    public IronGolemDefault(PetCore plugin) {
        super(plugin, "iron_golem", SoundMaker.ENTITY_IRONGOLEM_STEP, EntityWrapper.IRON_GOLEM);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/89091d79ea0f59ef7ef94d7bba6e5f17f2f7d4572c44f90f76c4819a714")
                .withName("&f&lIron Golem Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityIronGolemPet.class;
    }
}
