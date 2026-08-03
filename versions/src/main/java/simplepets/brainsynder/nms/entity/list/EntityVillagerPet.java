package simplepets.brainsynder.nms.entity.list;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.npc.villager.VillagerData;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.entity.CraftVillager;
import simplepets.brainsynder.api.entity.passive.IEntityVillagerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.villager.BiomeType;
import simplepets.brainsynder.api.wrappers.villager.VillagerInfo;
import simplepets.brainsynder.api.wrappers.villager.VillagerLevel;
import simplepets.brainsynder.api.wrappers.villager.VillagerType;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import java.util.Locale;

/**
 * NMS: {@link net.minecraft.world.entity.npc.villager.Villager}
 */
public class EntityVillagerPet extends EntityAgeablePet implements IEntityVillagerPet {
    private static final EntityDataAccessor<Integer> HEAD_ROLLING_TIME_LEFT = SynchedEntityData.defineId(EntityVillagerPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<VillagerData> VILLAGER_DATA = SynchedEntityData.defineId(EntityVillagerPet.class, EntityDataSerializers.VILLAGER_DATA);
    private static final EntityDataAccessor<Boolean> DATA_VILLAGER_DATA_FINALIZED = SynchedEntityData.defineId(EntityVillagerPet.class, EntityDataSerializers.BOOLEAN);
    private boolean shaking = false;

    public EntityVillagerPet(PetType type, PetUser user) {
        super(EntitySelector.VILLAGER, type, user);
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
        dataAccess.define(VILLAGER_DATA, new VillagerData(
                BuiltInRegistries.VILLAGER_TYPE.getOrThrow(net.minecraft.world.entity.npc.villager.VillagerType.PLAINS),
                BuiltInRegistries.VILLAGER_PROFESSION.getOrThrow(VillagerProfession.NONE),
                1
        ));
        dataAccess.define(DATA_VILLAGER_DATA_FINALIZED, false);
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
        entityData.set(VILLAGER_DATA, new VillagerData(
                CraftVillager.CraftType.bukkitToMinecraftHolder(Registry.VILLAGER_TYPE.get(NamespacedKey.fromString(data.getBiome().name().toLowerCase(Locale.ROOT)))),
                CraftVillager.CraftProfession.bukkitToMinecraftHolder(Registry.VILLAGER_PROFESSION.get(NamespacedKey.fromString(data.getType().name().toLowerCase(Locale.ROOT)))),
                data.getLevel().ordinal()+1
        ));
    }

    @Override
    public VillagerInfo getVillagerData() {
        VillagerData raw = getRawData();
        return  new VillagerInfo(
                BiomeType.valueOf(CraftVillager.CraftType.minecraftHolderToBukkit(raw.type()).getKey().getKey().toUpperCase(Locale.ROOT)),
                VillagerType.valueOf(CraftVillager.CraftProfession.minecraftHolderToBukkit(raw.profession()).getKey().getKey().toUpperCase(Locale.ROOT)),
                VillagerLevel.getById(raw.level())
        );
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
