package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityZombiePet;
import simplepets.brainsynder.api.entity.misc.ISkeletonAbstract;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "raised_arms")
public class ArmsData extends PetData {
    public ArmsData() {
        addDefaultItem("true", new ItemBuilder(Material.STICK)
                .withName("&#c8c8c8Arms Raised: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.STICK)
                .withName("&#c8c8c8Arms Raised: &cfalse"));
    }

    @Override
    public void onLeftClick(IEntityPet entity) {
        if (entity instanceof IEntityZombiePet) {
            IEntityZombiePet zombie = (IEntityZombiePet)entity;
            zombie.setArmsRaised(!zombie.isArmsRaised());
        }else if (entity instanceof ISkeletonAbstract) {
            ISkeletonAbstract skeleton = (ISkeletonAbstract)entity;
            skeleton.setArmsRaised(!skeleton.isArmsRaised());
        }
    }

    @Override
    public Object value(IEntityPet entity) {
        if (entity instanceof IEntityZombiePet) {
            IEntityZombiePet zombie = (IEntityZombiePet)entity;
            return zombie.isArmsRaised();
        }else if (entity instanceof ISkeletonAbstract) {
            ISkeletonAbstract skeleton = (ISkeletonAbstract)entity;
            return skeleton.isArmsRaised();
        }
        return false;
    }
}
