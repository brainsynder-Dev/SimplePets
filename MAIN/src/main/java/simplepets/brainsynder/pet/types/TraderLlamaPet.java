package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityTraderLlamaPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

@SupportedVersion(version = ServerVersion.v1_14_R1)
public class TraderLlamaPet extends PetType {
    public TraderLlamaPet(PetCore plugin) {
        super(plugin, "trader_llama", SoundMaker.ENTITY_LLAMA_AMBIENT, EntityWrapper.TRADER_LLAMA);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/8424780b3c5c5351cf49fb5bf41fcb289491df6c430683c84d7846188db4f84d")
                .withName("&f&lTrader Llama Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityTraderLlamaPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.LLAMA;
    }
}
