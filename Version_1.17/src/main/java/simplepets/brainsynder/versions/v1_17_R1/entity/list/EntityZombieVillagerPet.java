package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import simplepets.brainsynder.api.entity.hostile.IEntityZombieVillagerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.villager.BiomeType;
import simplepets.brainsynder.api.wrappers.villager.VillagerLevel;
import simplepets.brainsynder.api.wrappers.villager.VillagerType;
import simplepets.brainsynder.versions.v1_17_R1.utils.EntityUtils;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityZombieVillager}
 */
public class EntityZombieVillagerPet extends EntityZombiePet implements IEntityZombieVillagerPet {
    private static final EntityDataAccessor<Boolean> CONVERTING;
    private static final EntityDataAccessor<net.minecraft.world.entity.npc.VillagerData> VILLAGER_DATA;

    public EntityZombieVillagerPet(PetType type, PetUser user) {
        super(EntityType.ZOMBIE_VILLAGER, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        entityData.define(CONVERTING, false);
        entityData.define(VILLAGER_DATA, new net.minecraft.world.entity.npc.VillagerData(EntityUtils.getTypeFromBiome(BiomeType.PLAINS), VillagerProfession.NONE, 1));

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
            setVillagerData(simplepets.brainsynder.api.wrappers.villager.VillagerData.fromCompound(object.getCompoundTag("data")));
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

    private net.minecraft.world.entity.npc.VillagerData getRawData () {
        return entityData.get(VILLAGER_DATA);
    }

    @Override
    public simplepets.brainsynder.api.wrappers.villager.VillagerData getVillagerData() {
        return  new simplepets.brainsynder.api.wrappers.villager.VillagerData(EntityUtils.getBiomeFromType(getRawData().getType()), VillagerType.getVillagerType(getRawData().getProfession().toString()), VillagerLevel.getById(getRawData().getLevel()));
    }

    @Override
    public void setVillagerData(simplepets.brainsynder.api.wrappers.villager.VillagerData data) {
        net.minecraft.world.entity.npc.VillagerType biome = EntityUtils.getTypeFromBiome(data.getBiome());

        entityData.set(VILLAGER_DATA, new net.minecraft.world.entity.npc.VillagerData(biome, EntityUtils.getProfession(data.getType()), data.getLevel().ordinal()+1));
    }

    static {
        CONVERTING = SynchedEntityData.defineId(EntityZombieVillagerPet.class, EntityDataSerializers.BOOLEAN);
        VILLAGER_DATA = SynchedEntityData.defineId(EntityZombieVillagerPet.class, EntityDataSerializers.VILLAGER_DATA);
    }
}
