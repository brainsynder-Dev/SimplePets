package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityOcelotPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class OcelotPet extends PetType {
    public OcelotPet(PetCore plugin) {
        super(plugin, "ocelot", SoundMaker.ENTITY_CAT_AMBIENT, EntityWrapper.OCELOT);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/5657cd5c2989ff97570fec4ddcdc6926a68a3393250c1be1f0b114a1db1")
                .withName("&f&lOcelot Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityOcelotPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.OCELOT;
    }
}
