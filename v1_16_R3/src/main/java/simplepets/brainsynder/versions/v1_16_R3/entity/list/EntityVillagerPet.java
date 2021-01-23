package simplepets.brainsynder.versions.v1_16_R3.entity.list;

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
import simplepets.brainsynder.api.wrappers.villager.VillagerType;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityAgeablePet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;
import simplepets.brainsynder.versions.v1_16_R3.utils.EntityUtils;

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
        super.applyCompound(object);
    }

    @Override
    public void setVillagerData(VillagerData data) {
        net.minecraft.server.v1_16_R3.VillagerType biome = EntityUtils.getTypeFromBiome(data.getBiome());
        datawatcher.set(VILLAGER_DATA, new net.minecraft.server.v1_16_R3.VillagerData(biome, EntityUtils.getProfession(data.getType()), data.getLevel()));
    }

    @Override
    public VillagerData getVillagerData() {
        net.minecraft.server.v1_16_R3.VillagerData raw = getRawData();
        return  new VillagerData(EntityUtils.getBiomeFromType(raw.getType()), VillagerType.getVillagerType(raw.getProfession().toString()), raw.getLevel());
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
