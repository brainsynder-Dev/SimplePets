package simplepets.brainsynder.pet.types;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityTraderLlamaPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class TraderLlamaDefault extends PetDefault {
    public TraderLlamaDefault(PetCore plugin) {
        super(plugin, "trader_llama", SoundMaker.ENTITY_LLAMA_AMBIENT, EntityWrapper.TRADER_LLAMA);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return ItemBuilder.getSkull(simple.brainsynder.utils.SkullType.PLAYER)
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

    @Override
    public ServerVersion getAllowedVersion() {
        return ServerVersion.v1_14_R1;
    }

    @Override
    public boolean isSupported() {
        return ServerVersion.isEqualNew(ServerVersion.v1_14_R1);
    }
}
