package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityTurtlePet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

@SupportedVersion(version = ServerVersion.v1_13_R1)
public class TurtlePet extends PetType {
    public TurtlePet(PetCore plugin) {
        super(plugin, "turtle", SoundMaker.ENTITY_TURTLE_AMBIENT_LAND, EntityWrapper.TURTLE);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.TURTLE_HELMET).withName("&f&lTurtle Pet");
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
