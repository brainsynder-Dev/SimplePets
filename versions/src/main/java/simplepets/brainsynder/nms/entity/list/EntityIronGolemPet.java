package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.passive.IEntityIronGolemPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.GolemCrackLevel;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityPetOverride;

import static simplepets.brainsynder.api.pet.PetDataRegistry.IronGolem.CRACKS;

/**
 * NMS: {@link net.minecraft.world.entity.animal.golem.IronGolem}
 */
public class EntityIronGolemPet extends EntityPetOverride implements IEntityIronGolemPet {
    public EntityIronGolemPet(PetType type, PetUser user) {
        super(EntitySelector.IRON_GOLEM, type, user);
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(100.0D);
        setCrackLevel(GolemCrackLevel.NONE);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("cracks", getCrackLevel().name());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setEnum(CRACKS.namespace(), getCrackLevel());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        super.applyCompound(object);

        if (object.hasKey(CRACKS.namespace())) setCrackLevel(object.getEnum(CRACKS.namespace(), GolemCrackLevel.class, GolemCrackLevel.NONE));
    }

    @Override
    public GolemCrackLevel getCrackLevel() {
        return GolemCrackLevel.getByHealthModifier(getHealth() / getMaxHealth());
    }

    @Override
    public void setCrackLevel(GolemCrackLevel level) {
        setHealth((float) (getMaxHealth() * level.getHealthModifier()));
    }
}
