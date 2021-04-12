package simplepets.brainsynder.api.entity.hostile;

import org.bukkit.block.data.BlockData;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.ENDERMAN)
public interface IEntityEndermanPet extends IEntityPet {
    boolean isScreaming();
    void setScreaming(boolean flag);

    BlockData getCarriedBlock ();
    void setCarriedBlock(BlockData blockData);
}
