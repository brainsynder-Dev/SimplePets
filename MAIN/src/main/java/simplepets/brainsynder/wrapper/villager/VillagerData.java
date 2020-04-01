package simplepets.brainsynder.wrapper.villager;

import lib.brainsynder.nbt.StorageTagCompound;

/**
 * Will store the Villager information (1.14+)
 */
public class VillagerData {
    private BiomeType biome = BiomeType.PLAINS;
    private VillagerType type = VillagerType.NONE;
    private int level = 1;

    public VillagerData(BiomeType biome, VillagerType type, int level) {
        this.biome = biome;
        this.type = type;
        this.level = Math.max(1, level);
    }

    public VillagerType getType() {
        return this.type;
    }

    public BiomeType getBiome() {
        return this.biome;
    }

    public int getLevel() {
        return this.level;
    }

    public VillagerData withBiome(BiomeType biome) {
        return new VillagerData(biome, this.type, this.level);
    }

    public VillagerData withType(VillagerType type) {
        return new VillagerData(this.biome, type, this.level);
    }

    public VillagerData withLevel(int level) {
        return new VillagerData(this.biome, this.type, level);
    }

    public StorageTagCompound toCompound () {
        StorageTagCompound compound = new StorageTagCompound();
        compound.setString("biome", biome.name());
        compound.setString("type", type.name());
        compound.setInteger("level", level);
        return compound;
    }

    public static VillagerData getDefault () {
        return new VillagerData(BiomeType.PLAINS, VillagerType.NONE, 1);
    }

    public static VillagerData fromCompound (StorageTagCompound compound) {
        BiomeType biome = BiomeType.PLAINS;
        if (compound.hasKey("biome")) biome = BiomeType.valueOf(compound.getString("biome"));

        VillagerType type = VillagerType.NONE;
        if (compound.hasKey("type")) type = VillagerType.valueOf(compound.getString("type"));

        int level = 1;
        if (compound.hasKey("level")) level = compound.getInteger("level");

        return new VillagerData(biome, type, level);
    }
}
