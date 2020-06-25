package simplepets.brainsynder.nms.v1_16_R1.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R1.*;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityVillagerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R1.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_16_R1.utils.DataWatcherWrapper;
import simplepets.brainsynder.nms.v1_16_R1.utils.EntityUtils;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.wrapper.ProfessionWrapper;
import simplepets.brainsynder.wrapper.villager.BiomeType;
import simplepets.brainsynder.wrapper.villager.VillagerData;
import simplepets.brainsynder.wrapper.villager.VillagerType;

/**
 * NMS: {@link net.minecraft.server.v1_16_R1.EntityVillager}
 */
@Size(width = 0.6F, length = 1.8F)
public class EntityVillagerPet extends AgeableEntityPet implements IEntityVillagerPet {

    private static final DataWatcherObject<Integer> SHAKE_TIMER_UNUSED; 
    private static final DataWatcherObject<net.minecraft.server.v1_16_R1.VillagerData> VILLAGER_DATA;

    static {
        SHAKE_TIMER_UNUSED = DataWatcher.a(EntityVillagerPet.class, DataWatcherWrapper.INT);
        VILLAGER_DATA = DataWatcher.a(EntityVillagerPet.class, DataWatcherWrapper.DATA);
    }

    public EntityVillagerPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    public EntityVillagerPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(VILLAGER_DATA, new net.minecraft.server.v1_16_R1.VillagerData(EntityUtils.getTypeFromBiome(BiomeType.PLAINS), VillagerProfession.NONE, 1));
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setTag("data", getVillagerData().toCompound());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("data"))
            setVillagerData(VillagerData.fromCompound(object.getCompoundTag("data")));
        if (object.hasKey("profession")) {
            ProfessionWrapper profession = ProfessionWrapper.getProfession(object.getString("profession"));
            setVillagerData(getVillagerData().withType(VillagerType.fromProfession(profession)));
        }
        super.applyCompound(object);
    }

    private net.minecraft.server.v1_16_R1.VillagerData getRawData() {
        return datawatcher.get(VILLAGER_DATA);
    }

    @Override
    public VillagerData getVillagerData() {
        net.minecraft.server.v1_16_R1.VillagerData raw = getRawData();
        return  new VillagerData(EntityUtils.getBiomeFromType(raw.getType()), VillagerType.getVillagerType(raw.getProfession().toString()), raw.getLevel());
    }

    @Override
    public void setVillagerData(VillagerData data) {
        net.minecraft.server.v1_16_R1.VillagerType biome = EntityUtils.getTypeFromBiome(data.getBiome());
        datawatcher.set(VILLAGER_DATA, new net.minecraft.server.v1_16_R1.VillagerData(biome, EntityUtils.getProfession(data.getType()), data.getLevel()));
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
    }


}