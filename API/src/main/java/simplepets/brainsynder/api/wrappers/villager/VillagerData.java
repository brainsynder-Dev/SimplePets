package simplepets.brainsynder.api.wrappers.villager;

import lib.brainsynder.nbt.StorageTagCompound;

/**
 * Will store the Villager information (1.14+)
 */
public class VillagerData {
    private BiomeType biome = BiomeType.PLAINS;
    private VillagerType type = VillagerType.NONE;
    private VillagerLevel level = VillagerLevel.NOVICE;

    public VillagerData(BiomeType biome, VillagerType type, VillagerLevel level) {
        this.biome = biome;
        this.type = type;
        this.level = level;
    }

    public VillagerType getType() {
        return this.type;
    }

    public BiomeType getBiome() {
        return this.biome;
    }

    public VillagerLevel getLevel() {
        return this.level;
    }

    public VillagerData withBiome(BiomeType biome) {
        return new VillagerData(biome, this.type, this.level);
    }

    public VillagerData withType(VillagerType type) {
        return new VillagerData(this.biome, type, this.level);
    }

    public VillagerData withLevel(VillagerLevel level) {
        return new VillagerData(this.biome, this.type, level);
    }

    public StorageTagCompound toCompound () {
        StorageTagCompound compound = new StorageTagCompound();
        compound.setEnum("biome", biome);
        compound.setEnum("type", type);
        compound.setEnum("level", level);
        return compound;
    }

    public static VillagerData getDefault () {
        return new VillagerData(BiomeType.PLAINS, VillagerType.NONE, VillagerLevel.NOVICE);
    }

    public static VillagerData fromCompound (StorageTagCompound compound) {
        BiomeType biome = BiomeType.PLAINS;
        if (compound.hasKey("biome")) biome = compound.getEnum("biome", BiomeType.class, BiomeType.PLAINS);

        VillagerType type = VillagerType.NONE;
        if (compound.hasKey("type")) type = compound.getEnum("type", VillagerType.class, VillagerType.NONE);

        VillagerLevel level = VillagerLevel.NOVICE;
        if (compound.hasKey("level")) level = compound.getEnum("level", VillagerLevel.class, VillagerLevel.NOVICE);

        return new VillagerData(biome, type, level);
    }
}