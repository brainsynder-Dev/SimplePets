package simplepets.brainsynder.pet.types;

import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityZombieVillagerPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class ZombieVillagerDefault extends PetDefault {
    public ZombieVillagerDefault(PetCore plugin) {
        super(plugin, "zombie_villager", SoundMaker.ENTITY_ZOMBIE_VILLAGER_AMBIENT, EntityWrapper.ZOMBIE_VILLAGER);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return Utilities.getSkullMaterial(Utilities.SkullType.PLAYER).toBuilder(1)
                .setTexture("http://textures.minecraft.net/texture/e5e08a8776c1764c3fe6a6ddd412dfcb87f41331dad479ac96c21df4bf3ac89c")
                .withName("&f&lZombie Villager Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityZombieVillagerPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.ZOMBIE_VILLAGER;
    }
}
