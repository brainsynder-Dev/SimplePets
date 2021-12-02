package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import simplepets.brainsynder.api.entity.passive.IEntityVillagerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.villager.BiomeType;
import simplepets.brainsynder.api.wrappers.villager.VillagerData;
import simplepets.brainsynder.api.wrappers.villager.VillagerLevel;
import simplepets.brainsynder.api.wrappers.villager.VillagerType;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityAgeablePet;
import simplepets.brainsynder.versions.v1_17_R1.utils.EntityUtils;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityVillager}
 */
public class EntityVillagerPet extends EntityAgeablePet implements IEntityVillagerPet {
    private static final EntityDataAccessor<Integer> HEAD_ROLLING_TIME_LEFT;
    private static final EntityDataAccessor<net.minecraft.world.entity.npc.VillagerData> VILLAGER_DATA;
    private boolean shaking = false;

    public EntityVillagerPet(PetType type, PetUser user) {
        super(EntityType.VILLAGER, type, user);
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
        if (object.hasKey("level")) setMasteryLevel(object.getEnum("level", VillagerLevel.class, VillagerLevel.NOVICE));
        super.applyCompound(object);
    }

    @Override
    public void setVillagerData(VillagerData data) {
        net.minecraft.world.entity.npc.VillagerType biome = EntityUtils.getTypeFromBiome(data.getBiome());
        entityData.set(VILLAGER_DATA, new net.minecraft.world.entity.npc.VillagerData(biome, EntityUtils.getProfession(data.getType()), data.getLevel().ordinal()+1));
    }

    @Override
    public VillagerData getVillagerData() {
        net.minecraft.world.entity.npc.VillagerData raw = getRawData();
        return  new VillagerData(EntityUtils.getBiomeFromType(raw.getType()), VillagerType.getVillagerType(raw.getProfession().toString()), VillagerLevel.getById(raw.getLevel()));
    }

    private net.minecraft.world.entity.npc.VillagerData getRawData() {
        return entityData.get(VILLAGER_DATA);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(HEAD_ROLLING_TIME_LEFT, 0);
        this.entityData.define(VILLAGER_DATA, new net.minecraft.world.entity.npc.VillagerData(EntityUtils.getTypeFromBiome(BiomeType.PLAINS), VillagerProfession.NONE, 1));
    }

    static {
        HEAD_ROLLING_TIME_LEFT = SynchedEntityData.defineId(EntityVillagerPet.class, EntityDataSerializers.INT);
        VILLAGER_DATA = SynchedEntityData.defineId(EntityVillagerPet.class, EntityDataSerializers.VILLAGER_DATA);
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
