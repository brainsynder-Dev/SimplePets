package simplepets.brainsynder.nms.v1_14_R1.entities.list;

import net.minecraft.server.v1_14_R1.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityVillagerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_14_R1.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_14_R1.utils.DataWatcherWrapper;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.wrapper.ProfessionWrapper;
import simplepets.brainsynder.wrapper.villager.BiomeType;
import simplepets.brainsynder.wrapper.villager.VillagerData;
import simplepets.brainsynder.wrapper.villager.VillagerType;

/**
 * NMS: {@link net.minecraft.server.v1_14_R1.EntityVillager}
 */
@Size(width = 0.6F, length = 1.8F)
public class EntityVillagerPet extends AgeableEntityPet implements IEntityVillagerPet {
    private static final DataWatcherObject<net.minecraft.server.v1_14_R1.VillagerData> VILLAGER_DATA;

    static {
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
        this.datawatcher.register(VILLAGER_DATA, new net.minecraft.server.v1_14_R1.VillagerData(net.minecraft.server.v1_14_R1.VillagerType.PLAINS, VillagerProfession.NONE, 1));
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

    private net.minecraft.server.v1_14_R1.VillagerData getRawData () {
        return datawatcher.get(VILLAGER_DATA);
    }

    @Override
    public VillagerData getVillagerData() {
        return  new VillagerData(getBiomeFromType(getRawData().getType()), VillagerType.getVillagerType(getRawData().getProfession().toString()), getRawData().getLevel());
    }

    @Override
    public void setVillagerData(VillagerData data) {
        net.minecraft.server.v1_14_R1.VillagerType biome = getTypeFromBiome(data.getBiome());

        datawatcher.set(VILLAGER_DATA, new net.minecraft.server.v1_14_R1.VillagerData(biome, getProfession(data.getType()), data.getLevel()));
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
    }

    private BiomeType getBiomeFromType (net.minecraft.server.v1_14_R1.VillagerType type) {
        BiomeType biome = BiomeType.PLAINS;
        if (type == net.minecraft.server.v1_14_R1.VillagerType.DESERT) {
            biome = BiomeType.DESERT;
        }else if (type == net.minecraft.server.v1_14_R1.VillagerType.JUNGLE) {
            biome = BiomeType.JUNGLE;
        }else if (type == net.minecraft.server.v1_14_R1.VillagerType.SAVANNA) {
            biome = BiomeType.SAVANNA;
        }else if (type == net.minecraft.server.v1_14_R1.VillagerType.SNOW) {
            biome = BiomeType.SNOW;
        }else if (type == net.minecraft.server.v1_14_R1.VillagerType.SWAMP) {
            biome = BiomeType.SWAMP;
        }else if (type == net.minecraft.server.v1_14_R1.VillagerType.TAIGA) {
            biome = BiomeType.TAIGA;
        }
        return biome;
    }
    private net.minecraft.server.v1_14_R1.VillagerType getTypeFromBiome (BiomeType type) {
        net.minecraft.server.v1_14_R1.VillagerType biome = net.minecraft.server.v1_14_R1.VillagerType.PLAINS;
        if (type == BiomeType.DESERT) {
            biome = net.minecraft.server.v1_14_R1.VillagerType.DESERT;
        }else if (type == BiomeType.JUNGLE) {
            biome = net.minecraft.server.v1_14_R1.VillagerType.JUNGLE;
        }else if (type == BiomeType.SAVANNA) {
            biome = net.minecraft.server.v1_14_R1.VillagerType.SAVANNA;
        }else if (type == BiomeType.SNOW) {
            biome = net.minecraft.server.v1_14_R1.VillagerType.SNOW;
        }else if (type == BiomeType.SWAMP) {
            biome = net.minecraft.server.v1_14_R1.VillagerType.SWAMP;
        }else if (type == BiomeType.TAIGA) {
            biome = net.minecraft.server.v1_14_R1.VillagerType.TAIGA;
        }
        return biome;
    }
    private net.minecraft.server.v1_14_R1.VillagerProfession getProfession (VillagerType type) {

        net.minecraft.server.v1_14_R1.VillagerProfession profession = VillagerProfession.NONE;
        switch (type) {
            case ARMORER:
                profession = VillagerProfession.ARMORER;
                break;
            case BUTCHER:
                profession = VillagerProfession.BUTCHER;
                break;
            case CARTOGRAPHER:
                profession = VillagerProfession.CARTOGRAPHER;
                break;
            case CLERIC:
                profession = VillagerProfession.CLERIC;
                break;
            case FARMER:
                profession = VillagerProfession.FARMER;
                break;
            case FISHERMAN:
                profession = VillagerProfession.FISHERMAN;
                break;
            case FLETCHER:
                profession = VillagerProfession.FLETCHER;
                break;
            case LEATHERWORKER:
                profession = VillagerProfession.LEATHERWORKER;
                break;
            case LIBRARIAN:
                profession = VillagerProfession.LIBRARIAN;
                break;
            case MASON:
                profession = VillagerProfession.MASON;
                break;
            case NITWIT:
                profession = VillagerProfession.NITWIT;
                break;
            case SHEPHERD:
                profession = VillagerProfession.SHEPHERD;
                break;
            case TOOLSMITH:
                profession = VillagerProfession.TOOLSMITH;
                break;
            case WEAPONSMITH:
                profession = VillagerProfession.WEAPONSMITH;
                break;
        }
        return profession;
    }
}