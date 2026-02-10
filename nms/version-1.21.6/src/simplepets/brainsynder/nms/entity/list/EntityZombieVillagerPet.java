package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftVillager;
import org.bukkit.entity.Villager;
import simplepets.brainsynder.api.entity.hostile.IEntityZombieVillagerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.villager.BiomeType;
import simplepets.brainsynder.api.wrappers.villager.VillagerInfo;
import simplepets.brainsynder.api.wrappers.villager.VillagerLevel;
import simplepets.brainsynder.api.wrappers.villager.VillagerType;
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
        dataAccess.define(VILLAGER_DATA, new VillagerData(
                BuiltInRegistries.VILLAGER_TYPE.getOrThrow(net.minecraft.world.entity.npc.VillagerType.PLAINS),
                BuiltInRegistries.VILLAGER_PROFESSION.getOrThrow(VillagerProfession.NONE),
                1
        ));
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
        net.minecraft.world.entity.npc.VillagerData raw = getRawData();
        return  new VillagerInfo(
                BiomeType.valueOf(CraftVillager.CraftType.minecraftHolderToBukkit(raw.type()).name()),
                VillagerType.valueOf(CraftVillager.CraftProfession.minecraftHolderToBukkit(raw.profession()).name()),
                VillagerLevel.getById(raw.level())
        );
    }

    @Override
    public void setVillagerData(VillagerInfo data) {
        entityData.set(VILLAGER_DATA, new VillagerData(
                CraftVillager.CraftType.bukkitToMinecraftHolder(Villager.Type.valueOf(data.getBiome().name())),
                CraftVillager.CraftProfession.bukkitToMinecraftHolder(Villager.Profession.valueOf(data.getType().name())),
                data.getLevel().ordinal()+1
        ));
    }
}
