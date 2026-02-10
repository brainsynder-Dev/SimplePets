package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import simplepets.brainsynder.api.entity.hostile.IEntityZombieVillagerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.villager.BiomeType;
import simplepets.brainsynder.api.wrappers.villager.VillagerInfo;
import simplepets.brainsynder.api.wrappers.villager.VillagerLevel;
import simplepets.brainsynder.api.wrappers.villager.VillagerType;
import simplepets.brainsynder.nms.utils.EntityUtils;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.monster.ZombieVillager}
 */
public class EntityZombieVillagerPet extends EntityZombiePet implements IEntityZombieVillagerPet {
    private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(EntityZombieVillagerPet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<VillagerData> VILLAGER_DATA = SynchedEntityData.defineId(EntityZombieVillagerPet.class, EntityDataSerializers.VILLAGER_DATA);

    public EntityZombieVillagerPet(PetType type, PetUser user) {
        super(EntityType.ZOMBIE_VILLAGER, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("raised-arms", isArmsRaised());
        data.add("shaking", isShaking());
        data.add("profession", getVillagerType().name());
        data.add("biome", getBiome().name());
        data.add("level", getMasteryLevel().name());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(CONVERTING, false);
        dataAccess.define(VILLAGER_DATA, new VillagerData(EntityUtils.getTypeFromBiome(BiomeType.PLAINS), VillagerProfession.NONE, 1));
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("raised_arms", isArmsRaised());
        object.setBoolean("shaking", isShaking());
        object.setTag("data", getVillagerData().toCompound());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("data"))
            setVillagerData(VillagerInfo.fromCompound(object.getCompoundTag("data")));
        if (object.hasKey("raised_arms")) setArmsRaised(object.getBoolean("raised_arms"));
        if (object.hasKey("profession")) setVillagerType(object.getEnum("profession", VillagerType.class, VillagerType.NONE));
        if (object.hasKey("biome")) setBiome(object.getEnum("biome", BiomeType.class, BiomeType.PLAINS));
        if (object.hasKey("level")) setMasteryLevel(object.getEnum("level", VillagerLevel.class, VillagerLevel.NOVICE));
        super.applyCompound(object);
    }

    @Override
    public boolean isShaking() {
        return entityData.get(CONVERTING);
    }

    @Override
    public void setShaking(boolean value) {
        entityData.set(CONVERTING, value);
    }

    private VillagerData getRawData () {
        return entityData.get(VILLAGER_DATA);
    }

    @Override
    public VillagerInfo getVillagerData() {
        return  new VillagerInfo(EntityUtils.getBiomeFromType(getRawData().getType()), VillagerType.getVillagerType(getRawData().getProfession().toString()), VillagerLevel.getById(getRawData().getLevel()));
    }

    @Override
    public void setVillagerData(VillagerInfo data) {
        net.minecraft.world.entity.npc.VillagerType biome = EntityUtils.getTypeFromBiome(data.getBiome());

        entityData.set(VILLAGER_DATA, new VillagerData(biome, EntityUtils.getProfession(data.getType()), data.getLevel().ordinal()+1));
    }
}