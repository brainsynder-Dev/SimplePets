package simplepets.brainsynder.nms.v1_15_R1.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_15_R1.*;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityZombieVillagerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_15_R1.utils.DataWatcherWrapper;
import simplepets.brainsynder.nms.v1_15_R1.utils.EntityUtils;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.wrapper.ProfessionWrapper;
import simplepets.brainsynder.wrapper.villager.BiomeType;
import simplepets.brainsynder.wrapper.villager.VillagerType;

/**
 * NMS: {@link net.minecraft.server.v1_15_R1.EntityZombieVillager}
 */
@Size(width = 0.6F, length = 1.8F)
public class EntityZombieVillagerPet extends EntityZombiePet implements IEntityZombieVillagerPet {
    private static final DataWatcherObject<Boolean> CONVERTING;
    private static final DataWatcherObject<VillagerData> VILLAGER_DATA;

    static {
        CONVERTING = DataWatcher.a(EntityZombieVillagerPet.class, DataWatcherWrapper.BOOLEAN);
        VILLAGER_DATA = DataWatcher.a(EntityZombieVillagerPet.class, DataWatcherWrapper.DATA);
    }

    public EntityZombieVillagerPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
    public EntityZombieVillagerPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        datawatcher.register(CONVERTING, false);
        datawatcher.register(VILLAGER_DATA, new net.minecraft.server.v1_15_R1.VillagerData(EntityUtils.getTypeFromBiome(BiomeType.PLAINS), VillagerProfession.NONE, 1));

    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("arms_raised", isArmsRaised());
        object.setBoolean("shaking", isShaking());
        object.setTag("data", getVillagerData().toCompound());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("profession")) {
            ProfessionWrapper profession = ProfessionWrapper.getProfession(object.getString("profession"));
            setVillagerData(getVillagerData().withType(VillagerType.fromProfession(profession)));
        }

        if (object.hasKey("data"))
            setVillagerData(simplepets.brainsynder.wrapper.villager.VillagerData.fromCompound(object.getCompoundTag("data")));
        if (object.hasKey("arms_raised")) setArmsRaised(object.getBoolean("arms_raised"));
        super.applyCompound(object);
    }

    @Override
    public boolean isShaking() {
        return datawatcher.get(CONVERTING);
    }

    @Override
    public void setShaking(boolean value) {
        datawatcher.set(CONVERTING, value);
    }

    private net.minecraft.server.v1_15_R1.VillagerData getRawData () {
        return datawatcher.get(VILLAGER_DATA);
    }

    @Override
    public simplepets.brainsynder.wrapper.villager.VillagerData getVillagerData() {
        return  new simplepets.brainsynder.wrapper.villager.VillagerData(EntityUtils.getBiomeFromType(getRawData().getType()), VillagerType.getVillagerType(getRawData().getProfession().toString()), getRawData().getLevel());
    }

    @Override
    public void setVillagerData(simplepets.brainsynder.wrapper.villager.VillagerData data) {
        net.minecraft.server.v1_15_R1.VillagerType biome = EntityUtils.getTypeFromBiome(data.getBiome());

        datawatcher.set(VILLAGER_DATA, new net.minecraft.server.v1_15_R1.VillagerData(biome, EntityUtils.getProfession(data.getType()), data.getLevel()));
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
    }
}
