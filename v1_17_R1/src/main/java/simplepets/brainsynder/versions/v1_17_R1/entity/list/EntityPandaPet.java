package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.*;
import simplepets.brainsynder.api.entity.passive.IEntityPandaPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.PandaGene;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityAgeablePet;
import simplepets.brainsynder.versions.v1_17_R1.utils.DataWatcherWrapper;

import java.util.List;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityPanda}
 */
public class EntityPandaPet extends EntityAgeablePet implements IEntityPandaPet {
    private static final DataWatcherObject<Integer> ASK_FOR_BAMBOO_TICKS;
    private static final DataWatcherObject<Integer> SNEEZE_PROGRESS;
    private static final DataWatcherObject<Integer> EATING_TICKS;
    private static final DataWatcherObject<Byte> MAIN_GENE;
    private static final DataWatcherObject<Byte> HIDDEN_GENE;
    private static final DataWatcherObject<Byte> PANDA_FLAGS;

    public EntityPandaPet(PetType type, PetUser user) {
        super(EntityTypes.PANDA, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        datawatcher.register(ASK_FOR_BAMBOO_TICKS, 0);
        datawatcher.register(SNEEZE_PROGRESS, 0);
        datawatcher.register(MAIN_GENE, (byte)0);
        datawatcher.register(HIDDEN_GENE, (byte)0);
        datawatcher.register(PANDA_FLAGS, (byte)0);
        datawatcher.register(EATING_TICKS, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setEnum("type", getGene());
        object.setBoolean("sitting", isSitting());
        object.setBoolean("sleeping", isLyingOnBack());
        object.setBoolean("sneeze", isSneezing());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("type")) setGene(object.getEnum("type", PandaGene.class, PandaGene.NORMAL));
        if (object.hasKey("sitting")) setSitting(object.getBoolean("sitting", false));
        if (object.hasKey("sleeping")) setLyingOnBack(object.getBoolean("sleeping", false));
        if (object.hasKey("sneeze")) setSneezing(object.getBoolean("sneeze", false));
        super.applyCompound(object);
    }

    @Override
    public PandaGene getGene() {
        return PandaGene.byId(this.datawatcher.get(MAIN_GENE));
    }

    @Override
    public void setGene(PandaGene gene) {
        this.datawatcher.set(MAIN_GENE, (byte)gene.ordinal());

        // Makes sure that "brown" and "weak" pandas can be seen (not as regular pandas)
        PandaGene hidden = PandaGene.byId(datawatcher.get(HIDDEN_GENE));
        if ((gene == PandaGene.BROWN) || (gene == PandaGene.WEAK)) {
            this.datawatcher.set(HIDDEN_GENE, (byte)gene.ordinal());
        }else if ((hidden != PandaGene.BROWN) && (hidden != PandaGene.WEAK)) {
            if (hidden != PandaGene.NORMAL) datawatcher.set(HIDDEN_GENE, (byte)0);
        }
    }

    @Override
    public void setSneezeProgress(int progress) {
        this.datawatcher.set(SNEEZE_PROGRESS, progress);
    }

    @Override
    public int getSneezeProgress() {
        return this.datawatcher.get(SNEEZE_PROGRESS);
    }

    @Override
    public void setSpecialFlag(int flag, boolean value) {
        byte byte_1 = datawatcher.get(PANDA_FLAGS);
        if (value) {
            datawatcher.set(PANDA_FLAGS, (byte)(byte_1 | flag));
        } else {
            datawatcher.set(PANDA_FLAGS, (byte)(byte_1 & ~flag));
        }
        getPetUser().updateDataMenu();
    }

    @Override
    public boolean getSpecialFlag(int flag) {
        return (datawatcher.get(PANDA_FLAGS) & flag) != 0x0;
    }



    @Override
    public void tick() {
        super.tick();
        if (isSneezing()) {
            int progress = getSneezeProgress();
            setSneezeProgress(progress+1);
            if (progress > 20) {
                setSneezing(false);
                handleSneeze();
            }else if (progress == 1) {
                this.playSound(SoundEffects.ENTITY_PANDA_PRE_SNEEZE, 1.0F, 1.0F);
            }
        }
    }

    private void handleSneeze() {
        Vec3D var0 = this.getMot();
        double x = (this.locX() - (double)(this.getWidth() + 1.0F) * 0.5D * (double)MathHelper.sin(this.aK * 0.017453292F));
        double y = (this.locY() + (double)this.getHeadHeight() - 0.10000000149011612D);
        double z = (this.locZ() + (double)(this.getWidth() + 1.0F) * 0.5D * (double)MathHelper.cos(this.aK * 0.017453292F));
        this.world.addParticle(Particles.SNEEZE, x, y, z, var0.x, 0.0D, var0.z);
        this.playSound(SoundEffects.ENTITY_PANDA_SNEEZE, 1.0F, 1.0F);

        List<EntityPandaPet> nearby = this.world.a(EntityPandaPet.class, this.getBoundingBox().g(10.0D));
        nearby.forEach(panda -> {
            if (panda.onGround && !panda.isInWater() && panda.isSpookedBySneeze()) {
                panda.jump();
            }
        });
    }

    private boolean isSpookedBySneeze () {
        if (isLyingOnBack()) return false;
        return !isPlaying();
    }

    static {
        ASK_FOR_BAMBOO_TICKS = DataWatcher.a(EntityPandaPet.class, DataWatcherWrapper.INT);
        SNEEZE_PROGRESS = DataWatcher.a(EntityPandaPet.class, DataWatcherWrapper.INT);
        EATING_TICKS = DataWatcher.a(EntityPandaPet.class, DataWatcherWrapper.INT);
        MAIN_GENE = DataWatcher.a(EntityPandaPet.class, DataWatcherWrapper.BYTE);
        HIDDEN_GENE = DataWatcher.a(EntityPandaPet.class, DataWatcherWrapper.BYTE);
        PANDA_FLAGS = DataWatcher.a(EntityPandaPet.class, DataWatcherWrapper.BYTE);
    }
}
