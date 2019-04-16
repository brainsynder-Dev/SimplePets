package simplepets.brainsynder.pet.types;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityWanderingTraderPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class WanderingTraderDefault extends PetDefault {
    public WanderingTraderDefault(PetCore plugin) {
        super(plugin, "wandering_trader", SoundMaker.ENTITY_WANDERING_TRADER_AMBIENT, EntityWrapper.WANDERING_TRADER);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return ItemBuilder.getSkull(simple.brainsynder.utils.SkullType.PLAYER)
                .setTexture("http://textures.minecraft.net/texture/5f1379a82290d7abe1efaabbc70710ff2ec02dd34ade386bc00c930c461cf932")
                .withName("&f&lWandering Trader Pet");
    }

    @Override
    public PetData getPetData() {
        return PetData.SILENT;
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityWanderingTraderPet.class;
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
