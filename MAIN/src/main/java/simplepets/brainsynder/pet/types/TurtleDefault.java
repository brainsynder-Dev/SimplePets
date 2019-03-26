package simplepets.brainsynder.pet.types;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityTurtlePet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class TurtleDefault extends PetDefault {
    public TurtleDefault(PetCore plugin) {
        super(plugin, "turtle", SoundMaker.ENTITY_TURTLE_AMBIENT_LAND, EntityWrapper.TURTLE);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Utilities.fetchMaterial("TURTLE_HELMET")).withName("&f&lTurtle Pet");
    }

    @Override
    public ServerVersion getAllowedVersion() {
        return ServerVersion.v1_13_R1;
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityTurtlePet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.AGE;
    }
}
