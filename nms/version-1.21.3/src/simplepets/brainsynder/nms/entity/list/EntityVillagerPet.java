package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import simplepets.brainsynder.api.entity.passive.IEntityVillagerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.villager.BiomeType;
import simplepets.brainsynder.api.wrappers.villager.VillagerInfo;
import simplepets.brainsynder.api.wrappers.villager.VillagerLevel;
import simplepets.brainsynder.api.wrappers.villager.VillagerType;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.EntityUtils;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.npc.Villager}
 */
public class EntityVillagerPet extends EntityAgeablePet implements IEntityVillagerPet {
    private static final EntityDataAccessor<Integer> HEAD_ROLLING_TIME_LEFT = SynchedEntityData.defineId(EntityVillagerPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<VillagerData> VILLAGER_DATA = SynchedEntityData.defineId(EntityVillagerPet.class, EntityDataSerializers.VILLAGER_DATA);
    private boolean shaking = false;

    public EntityVillagerPet(PetType type, PetUser user) {
        super(EntityType.VILLAGER, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("shaking", isShaking());
        data.add("profession", getVillagerType().name());
        data.add("biome", getBiome().name());
        data.add("level", getMasteryLevel().name());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(HEAD_ROLLING_TIME_LEFT, 0);
        dataAccess.define(VILLAGER_DATA, new VillagerData(EntityUtils.getTypeFromBiome(BiomeType.PLAINS), VillagerProfession.NONE, 1));
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("shaking", isShaking());
        object.setTag("data", getVillagerData().toCompound());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        setShaking(object.getBoolean("shaking", false));
        if (object.hasKey("data"))
            setVillagerData(VillagerInfo.fromCompound(object.getCompoundTag("data")));
        if (object.hasKey("profession")) setVillagerType(object.getEnum("profession", VillagerType.class, VillagerType.NONE));
        if (object.hasKey("biome")) setBiome(object.getEnum("biome", BiomeType.class, BiomeType.PLAINS));
        if (object.hasKey("level")) setMasteryLevel(object.getEnum("level", VillagerLevel.class, VillagerLevel.NOVICE));
        super.applyCompound(object);
    }

    @Override
    public void setVillagerData(VillagerInfo data) {
        net.minecraft.world.entity.npc.VillagerType biome = EntityUtils.getTypeFromBiome(data.getBiome());
        entityData.set(VILLAGER_DATA, new VillagerData(biome, EntityUtils.getProfession(data.getType()), data.getLevel().ordinal()+1));
    }

    @Override
    public VillagerInfo getVillagerData() {
        VillagerData raw = getRawData();
        return  new VillagerInfo(EntityUtils.getBiomeFromType(raw.getType()), VillagerType.getVillagerType(raw.getProfession().toString()), VillagerLevel.getById(raw.getLevel()));
    }

    private VillagerData getRawData() {
        return entityData.get(VILLAGER_DATA);
    }

    @Override
    public void tick() {
        super.tick();

        if (shaking && (entityData.get(HEAD_ROLLING_TIME_LEFT) <= 20)) entityData.set(HEAD_ROLLING_TIME_LEFT, 5000);
    }

    @Override
    public boolean isShaking() {
        return entityData.get(HEAD_ROLLING_TIME_LEFT) > 0;
    }

    @Override
    public void setShaking(boolean shaking) {
        this.shaking = shaking;
        entityData.set(HEAD_ROLLING_TIME_LEFT, shaking ? 5000 : 0);
    }
}