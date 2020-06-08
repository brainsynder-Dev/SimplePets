package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntitySkeletonHorsePet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class SkeletonHorsePet extends PetType {
    public SkeletonHorsePet(PetCore plugin) {
        super(plugin, "skeleton_horse", SoundMaker.ENTITY_SKELETON_HORSE_AMBIENT, EntityWrapper.SKELETON_HORSE);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
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
