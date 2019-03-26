package simplepets.brainsynder.pet.types;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityPhantomPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class PhantomDefault extends PetDefault {
    public PhantomDefault(PetCore plugin) {
        super(plugin, "phantom", SoundMaker.ENTITY_PHANTOM_AMBIENT, EntityWrapper.PHANTOM);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Utilities.fetchMaterial("PHANTOM_MEMBRANE")).withName("&f&lPhantom Pet");
    }

    @Override
    public ServerVersion getAllowedVersion() {
        return ServerVersion.v1_13_R1;
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityPhantomPet.class;
    }

    @Override
    public boolean canFlyDefault() {
        return true;
    }

    @Override
    public PetData getPetData() {
        return PetData.SIZE;
    }
}
