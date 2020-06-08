package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityCatPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

@SupportedVersion(version = ServerVersion.v1_14_R1)
public class CatPet extends PetType {
    public CatPet(PetCore plugin) {
        super(plugin, "cat", SoundMaker.ENTITY_CAT_AMBIENT, EntityWrapper.CAT);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/16df72c34e7fdad4bea41d96678b72f29f606e2ca75e3590a278932714be98")
                .withName("&f&lCat Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityCatPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.CAT;
    }
}
