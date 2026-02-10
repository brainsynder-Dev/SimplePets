package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.golem.CopperGolemState;
import net.minecraft.world.level.block.WeatheringCopper;
import simplepets.brainsynder.api.entity.passive.IEntityCopperGolemPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.OxidationWrapper;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.golem.CopperGolem}
 */
@SupportedVersion(version = ServerVersion.v1_21_9)
public class EntityCopperGolemPet extends EntityPetOverride implements IEntityCopperGolemPet {
    private static final EntityDataAccessor<WeatheringCopper.WeatherState> OXIDATION_STATE = SynchedEntityData.defineId(EntityCopperGolemPet.class, EntityDataSerializers.WEATHERING_COPPER_STATE);
    private static final EntityDataAccessor<CopperGolemState> COPPER_GOLEM_STATE = SynchedEntityData.defineId(EntityCopperGolemPet.class, EntityDataSerializers.COPPER_GOLEM_STATE);

    public EntityCopperGolemPet(PetType type, PetUser user) {
        super(EntityType.COPPER_GOLEM, type, user);
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);

        dataAccess.define(OXIDATION_STATE, WeatheringCopper.WeatherState.UNAFFECTED);
        dataAccess.define(COPPER_GOLEM_STATE, CopperGolemState.IDLE);
    }

    @Override
    public OxidationWrapper getOxidation() {
        return OxidationWrapper.valueOf(entityData.get(OXIDATION_STATE).name());
    }

    @Override
    public void setOxidation(OxidationWrapper wrapper) {
        entityData.set(OXIDATION_STATE, WeatheringCopper.WeatherState.valueOf(wrapper.name()));
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("oxidation", getOxidation().name());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setEnum("oxidation", getOxidation());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("oxidation")) setOxidation(object.getEnum("oxidation", OxidationWrapper.class, OxidationWrapper.UNAFFECTED));
        if (object.hasKey("golem_state")) {
            // TODO: This is just for testing, remove later
            CopperGolemState state = CopperGolemState.valueOf(object.getString("golem_state").toUpperCase());
            entityData.set(COPPER_GOLEM_STATE, state);
        }

        super.applyCompound(object);
    }
}
