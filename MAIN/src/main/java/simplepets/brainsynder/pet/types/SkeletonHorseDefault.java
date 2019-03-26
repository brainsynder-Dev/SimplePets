package simplepets.brainsynder.pet.types;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntitySkeletonHorsePet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class SkeletonHorseDefault extends PetDefault {
    public SkeletonHorseDefault(PetCore plugin) {
        super(plugin, "skeleton_horse", SoundMaker.ENTITY_SKELETON_HORSE_AMBIENT, EntityWrapper.SKELETON_HORSE);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return ItemBuilder.getSkull(simple.brainsynder.utils.SkullType.PLAYER)
                .setTexture("http://textures.minecraft.net/texture/47effce35132c86ff72bcae77dfbb1d22587e94df3cbc2570ed17cf8973a")
                .withName("&f&lSkeleton Horse Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntitySkeletonHorsePet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.HORSE_OTHER;
    }
}
