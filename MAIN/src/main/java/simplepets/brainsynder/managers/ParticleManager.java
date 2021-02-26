package simplepets.brainsynder.managers;

import lib.brainsynder.files.JsonFile;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagTools;
import lib.brainsynder.particle.Particle;
import lib.brainsynder.particle.ParticleMaker;
import simplepets.brainsynder.PetCore;

import java.io.File;

public class ParticleManager {
    private final File folder;
    private final ParticleMaker spawnParticle;
    private final ParticleMaker removeParticle;

    public ParticleManager (PetCore plugin) {
        folder = new File(plugin.getDataFolder()+File.separator+"Particles");
        if (!folder.exists()) folder.mkdirs();

        spawnParticle = getCustomizedParticle(new ParticleMaker(Particle.VILLAGER_HAPPY, 10, 1.3), "PetSpawnParticle");
        removeParticle = getCustomizedParticle(new ParticleMaker(Particle.LAVA, 20, 1), "PetRemoveParticle");


    }

    private ParticleMaker getCustomizedParticle (ParticleMaker defaultParticle, String name) {
        try {
            JsonFile file = new JsonFile(new File(folder, name+".json")) {
                @Override
                public void loadDefaults() {
                    setDefault("particle", StorageTagTools.toJsonObject(defaultParticle.toCompound()));
                }
            };
            if (!file.hasKey("particle")) return defaultParticle;

            return new ParticleMaker(StorageTagTools.fromJsonObject((JsonObject) file.getValue("particle")));
        }catch (Exception e) {
            return defaultParticle;
        }
    }

    public ParticleMaker getSpawnParticle() {
        return spawnParticle;
    }

    public ParticleMaker getRemoveParticle() {
        return removeParticle;
    }
}
