package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityIllusionerPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.AdditionalData;
import simplepets.brainsynder.wrapper.EntityWrapper;

@AdditionalData(passive = false)
public class IllusionerPet extends PetType {
    public IllusionerPet(PetCore plugin) {
        super(plugin, "illusioner", SoundMaker.ENTITY_ILLUSION_ILLAGER_AMBIENT, EntityWrapper.ILLUSIONER);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/1c678c9f4c6dd4d991930f82e6e7d8b89b2891f35cba48a4b18539bbe7ec927")
                .withName("&f&lIllusioner Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityIllusionerPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.WIZARD;
    }
}
