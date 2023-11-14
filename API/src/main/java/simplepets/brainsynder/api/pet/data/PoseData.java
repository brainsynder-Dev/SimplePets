package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.EntityPose;

@Namespace(namespace = "pose")
public class PoseData extends PetData<IEntityPet> {
    public PoseData() {
        for (EntityPose pose : EntityPose.values()) {
            addDefaultItem(pose.name(), new ItemBuilder(Material.AMETHYST_SHARD)
                    .withName("&#c8c8c8Pose: &#85d6ec{name}"));
        }
    }

    @Override
    public boolean isModifiable(IEntityPet entity) {
        return entity.getPetType().getEntityPoses().size() > 1;
    }

    @Override
    public Object getDefaultValue() {
        return EntityPose.STANDING;
    }

    @Override
    public void onLeftClick(IEntityPet entity) {
        entity.setPetPose(EntityPose.getNext(entity.getPetPose(), entity.getPetType().getEntityPoses()));
    }

    @Override
    public void onRightClick(IEntityPet entity) {
        entity.setPetPose(EntityPose.getPrevious(entity.getPetPose(), entity.getPetType().getEntityPoses()));
    }

    @Override
    public Object value(IEntityPet entity) {
        return entity.getPetPose();
    }
}
