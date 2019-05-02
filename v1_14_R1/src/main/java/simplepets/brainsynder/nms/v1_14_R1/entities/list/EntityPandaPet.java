package simplepets.brainsynder.nms.v1_14_R1.entities.list;

import net.minecraft.server.v1_14_R1.*;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityPandaPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_14_R1.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_14_R1.utils.DataWatcherWrapper;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.wrapper.PandaGene;

import java.util.List;

/**
 * NMS: {@link net.minecraft.server.v1_14_R1.EntityPanda}
 */
@Size(width = 0.6F, length = 1.95F)
public class EntityPandaPet extends AgeableEntityPet implements IEntityPandaPet {
    private static final DataWatcherObject<Integer> ASK_FOR_BAMBOO_TICKS;
    private static final DataWatcherObject<Integer> SNEEZE_PROGRESS;
    private static final DataWatcherObject<Integer> EATING_TICKS;
    private static final DataWatcherObject<Byte> MAIN_GENE;
    private static final DataWatcherObject<Byte> HIDDEN_GENE;
    private static final DataWatcherObject<Byte> PANDA_FLAGS;

    static {
        ASK_FOR_BAMBOO_TICKS = DataWatcher.a(EntityPandaPet.class, DataWatcherWrapper.INT);
        SNEEZE_PROGRESS = DataWatcher.a(EntityPandaPet.class, DataWatcherWrapper.INT);
        EATING_TICKS = DataWatcher.a(EntityPandaPet.class, DataWatcherWrapper.INT);
        MAIN_GENE = DataWatcher.a(EntityPandaPet.class, DataWatcherWrapper.BYTE);
        HIDDEN_GENE = DataWatcher.a(EntityPandaPet.class, DataWatcherWrapper.BYTE);
        PANDA_FLAGS = DataWatcher.a(EntityPandaPet.class, DataWatcherWrapper.BYTE);
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

    public EntityPandaPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
    public EntityPandaPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
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
    public void setEating(boolean value) {
        datawatcher.set(EATING_TICKS, value ? 1 : 0);
    }

    @Override
    public boolean isEating() {
        return datawatcher.get(EATING_TICKS) > 0;
    }

    @Override
    public void setSneezing(boolean value) {
        this.setFlag(2, value);
        if (!value) {
            this.setSneezeProgress(0);
            PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
        }
    }

    public int getSneezeProgress() {
        return this.datawatcher.get(SNEEZE_PROGRESS);
    }
    public void setSneezeProgress(int int_1) {
        this.datawatcher.set(SNEEZE_PROGRESS, int_1);
    }

    @Override
    public boolean getFlag(int flag) {
        return (datawatcher.get(PANDA_FLAGS) & flag) != 0x0;
    }

    @Override
    public void setFlag(int i, boolean flag) {
        byte byte_1 = datawatcher.get(PANDA_FLAGS);
        if (flag) {
            datawatcher.set(PANDA_FLAGS, (byte)(byte_1 | i));
        } else {
            datawatcher.set(PANDA_FLAGS, (byte)(byte_1 & ~i));
        }
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
    }

    @Override
    public void repeatTask() {
        super.repeatTask();
        if (isSneezing()) {
            int progress = getSneezeProgress();
            setSneezeProgress(progress+1);
            if (progress > 20) {
                setSneezing(false);
                handleSneeze();
            }else if (progress == 1) {
                this.a(SoundEffects.ENTITY_PANDA_PRE_SNEEZE, 1.0F, 1.0F);
            }
        }
    }

    private void handleSneeze() {
        Vec3D var0 = this.getMot();
        double x = (this.locX - (double)(this.getWidth() + 1.0F) * 0.5D * (double)MathHelper.sin(this.aK * 0.017453292F));
        double y = (this.locY + (double)this.getHeadHeight() - 0.10000000149011612D);
        double z = (this.locZ + (double)(this.getWidth() + 1.0F) * 0.5D * (double)MathHelper.cos(this.aK * 0.017453292F));
        this.world.addParticle(Particles.SNEEZE, x, y, z, var0.x, 0.0D, var0.z);
        this.a(SoundEffects.ENTITY_PANDA_SNEEZE, 1.0F, 1.0F);

        List<EntityPandaPet> nearby = this.world.a(EntityPandaPet.class, this.getBoundingBox().g(10.0D));
        nearby.forEach(panda -> {
            if (!panda.isBaby() && panda.onGround && !panda.isInWater() && panda.isSpookedBySneeze()) {
                panda.jump();
            }
        });
    }

    private boolean isSpookedBySneeze () {
        if (isLyingOnBack()) return false;
        return !isPlaying();
    }
}
