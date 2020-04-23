package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityFoxPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

@SupportedVersion(version = ServerVersion.v1_14_R1)
public class FoxDefault extends PetDefault {
    public FoxDefault(PetCore plugin) {
        super(plugin, "fox", SoundMaker.ENTITY_FOX_AMBIENT, EntityWrapper.FOX);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a")
                .withName("&f&lFox Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityFoxPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.FOX;
    }
}
