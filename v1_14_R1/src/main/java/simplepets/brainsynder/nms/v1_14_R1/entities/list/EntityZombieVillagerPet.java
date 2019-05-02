package simplepets.brainsynder.nms.v1_14_R1.entities.list;

import net.minecraft.server.v1_14_R1.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityZombieVillagerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_14_R1.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_14_R1.utils.DataWatcherWrapper;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.wrapper.ProfessionWrapper;
import simplepets.brainsynder.wrapper.villager.BiomeType;
import simplepets.brainsynder.wrapper.villager.VillagerType;

/**
 * NMS: {@link net.minecraft.server.v1_14_R1.EntityZombieVillager}
 */
@Size(width = 0.6F, length = 1.8F)
public class EntityZombieVillagerPet extends AgeableEntityPet implements IEntityZombieVillagerPet {
    private static final DataWatcherObject<Boolean> ARMS_RAISED;
    private static final DataWatcherObject<Integer> UNKNOWN; // Is this even needed?
    private static final DataWatcherObject<Boolean> CONVERTING;
    private static final DataWatcherObject<VillagerData> VILLAGER_DATA;

    static {
        ARMS_RAISED = DataWatcher.a(EntityZombieVillagerPet.class, DataWatcherWrapper.BOOLEAN);
        UNKNOWN = DataWatcher.a(EntityZombieVillagerPet.class, DataWatcherWrapper.INT);
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
        datawatcher.register(ARMS_RAISED, false);
        datawatcher.register(UNKNOWN, 0);
        datawatcher.register(CONVERTING, false);
        datawatcher.register(VILLAGER_DATA, new net.minecraft.server.v1_14_R1.VillagerData(net.minecraft.server.v1_14_R1.VillagerType.c, VillagerProfession.NONE, 1));
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

    private net.minecraft.server.v1_14_R1.VillagerData getRawData () {
        return datawatcher.get(VILLAGER_DATA);
    }

    @Override
    public simplepets.brainsynder.wrapper.villager.VillagerData getVillagerData() {
        return  new simplepets.brainsynder.wrapper.villager.VillagerData(getBiomeFromType(getRawData().getType()), VillagerType.getVillagerType(getRawData().getProfession().toString()), getRawData().getLevel());
    }

    @Override
    public void setVillagerData(simplepets.brainsynder.wrapper.villager.VillagerData data) {
        net.minecraft.server.v1_14_R1.VillagerType biome = getTypeFromBiome(data.getBiome());

        datawatcher.set(VILLAGER_DATA, new net.minecraft.server.v1_14_R1.VillagerData(biome, getProfession(data.getType()), data.getLevel()));
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
    }

    private BiomeType getBiomeFromType (net.minecraft.server.v1_14_R1.VillagerType type) {
        BiomeType biome = BiomeType.PLAINS;
        if (type == net.minecraft.server.v1_14_R1.VillagerType.a) {
            biome = BiomeType.DESERT;
        }else if (type == net.minecraft.server.v1_14_R1.VillagerType.b) {
            biome = BiomeType.JUNGLE;
        }else if (type == net.minecraft.server.v1_14_R1.VillagerType.d) {
            biome = BiomeType.SAVANNA;
        }else if (type == net.minecraft.server.v1_14_R1.VillagerType.e) {
            biome = BiomeType.SNOW;
        }else if (type == net.minecraft.server.v1_14_R1.VillagerType.f) {
            biome = BiomeType.SWAMP;
        }else if (type == net.minecraft.server.v1_14_R1.VillagerType.g) {
            biome = BiomeType.TAIGA;
        }
        return biome;
    }
    private net.minecraft.server.v1_14_R1.VillagerType getTypeFromBiome (BiomeType type) {
        net.minecraft.server.v1_14_R1.VillagerType biome = net.minecraft.server.v1_14_R1.VillagerType.c;
        if (type == BiomeType.DESERT) {
            biome = net.minecraft.server.v1_14_R1.VillagerType.a;
        }else if (type == BiomeType.JUNGLE) {
            biome = net.minecraft.server.v1_14_R1.VillagerType.b;
        }else if (type == BiomeType.SAVANNA) {
            biome = net.minecraft.server.v1_14_R1.VillagerType.d;
        }else if (type == BiomeType.SNOW) {
            biome = net.minecraft.server.v1_14_R1.VillagerType.e;
        }else if (type == BiomeType.SWAMP) {
            biome = net.minecraft.server.v1_14_R1.VillagerType.f;
        }else if (type == BiomeType.TAIGA) {
            biome = net.minecraft.server.v1_14_R1.VillagerType.g;
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

    @Override
    public boolean isArmsRaised() {
        return datawatcher.get(ARMS_RAISED);
    }

    @Override
    public void setArmsRaised(boolean flag) {
        datawatcher.set(ARMS_RAISED, flag);
    }
}
