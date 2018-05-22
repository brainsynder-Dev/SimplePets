package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityStrayPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class StrayDefault extends PetDefault {
    public StrayDefault(PetCore plugin) {
        super(plugin, "stray", SoundMaker.ENTITY_SKELETON_AMBIENT, EntityWrapper.STRAY);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SKULL_ITEM).withData(3)
                .setTexture("http://textures.minecraft.net/texture/78ddf76e555dd5c4aa8a0a5fc584520cd63d489c253de969f7f22f85a9a2d56")
                .withName("&f&lStray Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityStrayPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.ARMS;
    }
}
