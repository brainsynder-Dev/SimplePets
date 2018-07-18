package simplepets.brainsynder.pet.types;

import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityGiantPet;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class GiantDefault extends PetDefault {
    public GiantDefault(PetCore plugin) {
        super(plugin, "giant", SoundMaker.ENTITY_ZOMBIE_AMBIENT, EntityWrapper.GIANT);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return Utilities.getSkullMaterial(Utilities.SkullType.PLAYER).toBuilder(1)
                .setTexture("http://textures.minecraft.net/texture/77f844bfea25429d45e1fcf96ef6654dfaaa6fc902dc1b6b68c0abc1343447")
                .withName("&f&lGiant Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityGiantPet.class;
    }
}
