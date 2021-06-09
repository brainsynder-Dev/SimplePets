package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.VillagerProfession;
import simplepets.brainsynder.api.entity.passive.IEntityVillagerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.villager.BiomeType;
import simplepets.brainsynder.api.wrappers.villager.VillagerData;
import simplepets.brainsynder.api.wrappers.villager.VillagerLevel;
import simplepets.brainsynder.api.wrappers.villager.VillagerType;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityAgeablePet;
import simplepets.brainsynder.versions.v1_17_R1.utils.DataWatcherWrapper;
import simplepets.brainsynder.versions.v1_17_R1.utils.EntityUtils;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityVillager}
 */
public class EntityVillagerPet extends EntityAgeablePet implements IEntityVillagerPet {
    private static final DataWatcherObject<Integer> HEAD_ROLLING_TIME_LEFT;
    private static final DataWatcherObject<net.minecraft.server.v1_16_R3.VillagerData> VILLAGER_DATA;
    private boolean shaking = false;

    public EntityVillagerPet(PetType type, PetUser user) {
        super(EntityTypes.VILLAGER, type, user);
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
            setVillagerData(VillagerData.fromCompound(object.getCompoundTag("data")));
        if (object.hasKey("profession")) setVillagerType(object.getEnum("profession", VillagerType.class, VillagerType.NONE));
        if (object.hasKey("biome")) setBiome(object.getEnum("biome", BiomeType.class, BiomeType.PLAINS));
        if (object.hasKey("level")) setLevel(object.getEnum("level", VillagerLevel.class, VillagerLevel.NOVICE));
        super.applyCompound(object);
    }

    @Override
    public void setVillagerData(VillagerData data) {
        net.minecraft.server.v1_16_R3.VillagerType biome = EntityUtils.getTypeFromBiome(data.getBiome());
        datawatcher.set(VILLAGER_DATA, new net.minecraft.server.v1_16_R3.VillagerData(biome, EntityUtils.getProfession(data.getType()), data.getLevel().ordinal()+1));
    }

    @Override
    public VillagerData getVillagerData() {
        net.minecraft.server.v1_16_R3.VillagerData raw = getRawData();
        return  new VillagerData(EntityUtils.getBiomeFromType(raw.getType()), VillagerType.getVillagerType(raw.getProfession().toString()), VillagerLevel.getById(raw.getLevel()));
    }

    private net.minecraft.server.v1_16_R3.VillagerData getRawData() {
        return datawatcher.get(VILLAGER_DATA);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(HEAD_ROLLING_TIME_LEFT, 0);
        this.datawatcher.register(VILLAGER_DATA, new net.minecraft.server.v1_16_R3.VillagerData(EntityUtils.getTypeFromBiome(BiomeType.PLAINS), VillagerProfession.NONE, 1));
    }

    static {
        HEAD_ROLLING_TIME_LEFT = DataWatcher.a(EntityVillagerPet.class, DataWatcherWrapper.INT);
        VILLAGER_DATA = DataWatcher.a(EntityVillagerPet.class, DataWatcherWrapper.DATA);
    }

    @Override
    public void tick() {
        super.tick();

        if (shaking && (datawatcher.get(HEAD_ROLLING_TIME_LEFT) <= 20)) datawatcher.set(HEAD_ROLLING_TIME_LEFT, 5000);
    }

    @Override
    public boolean isShaking() {
        return datawatcher.get(HEAD_ROLLING_TIME_LEFT) > 0;
    }

    @Override
    public void setShaking(boolean shaking) {
        this.shaking = shaking;
        datawatcher.set(HEAD_ROLLING_TIME_LEFT, shaking ? 5000 : 0);
    }
}
