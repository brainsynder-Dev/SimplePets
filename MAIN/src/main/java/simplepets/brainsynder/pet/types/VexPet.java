package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityVexPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.AdditionalData;
import simplepets.brainsynder.wrapper.EntityWrapper;

@AdditionalData(passive = false)
public class VexPet extends PetType {
    public VexPet(PetCore plugin) {
        super(plugin, "vex", SoundMaker.ENTITY_VEX_AMBIENT, EntityWrapper.VEX);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/5e7330c7d5cd8a0a55ab9e95321535ac7ae30fe837c37ea9e53bea7ba2de86b")
                .withName("&f&lVex Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityVexPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.POWERED;
    }
}
