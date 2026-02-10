package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import simplepets.brainsynder.api.entity.passive.IEntityPandaPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.PandaGene;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import java.util.List;

/**
 * NMS: {@link net.minecraft.world.entity.animal.Panda}
 */
public class EntityPandaPet extends EntityAgeablePet implements IEntityPandaPet {
    private static final EntityDataAccessor<Integer> ASK_FOR_BAMBOO_TICKS = SynchedEntityData.defineId(EntityPandaPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SNEEZE_PROGRESS = SynchedEntityData.defineId(EntityPandaPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> EATING_TICKS = SynchedEntityData.defineId(EntityPandaPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> MAIN_GENE = SynchedEntityData.defineId(EntityPandaPet.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> HIDDEN_GENE = SynchedEntityData.defineId(EntityPandaPet.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> PANDA_FLAGS = SynchedEntityData.defineId(EntityPandaPet.class, EntityDataSerializers.BYTE);

    public EntityPandaPet(PetType type, PetUser user) {
        super(EntityType.PANDA, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("type", getGene().name());
        data.add("sitting", isSitting());
        data.add("sleeping", isLyingOnBack());
        data.add("sneeze", isSneezing());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(ASK_FOR_BAMBOO_TICKS, 0);
        dataAccess.define(SNEEZE_PROGRESS, 0);
        dataAccess.define(MAIN_GENE, (byte)0);
        dataAccess.define(HIDDEN_GENE, (byte)0);
        dataAccess.define(PANDA_FLAGS, (byte)0);
        dataAccess.define(EATING_TICKS, 0);
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
        return PandaGene.byId(this.entityData.get(MAIN_GENE));
    }

    @Override
    public void setGene(PandaGene gene) {
        this.entityData.set(MAIN_GENE, (byte)gene.ordinal());

        // Makes sure that "brown" and "weak" pandas can be seen (not as regular pandas)
        PandaGene hidden = PandaGene.byId(entityData.get(HIDDEN_GENE));
        if ((gene == PandaGene.BROWN) || (gene == PandaGene.WEAK)) {
            this.entityData.set(HIDDEN_GENE, (byte)gene.ordinal());
        }else if ((hidden != PandaGene.BROWN) && (hidden != PandaGene.WEAK)) {
            if (hidden != PandaGene.NORMAL) entityData.set(HIDDEN_GENE, (byte)0);
        }
    }

    @Override
    public void setSneezeProgress(int progress) {
        this.entityData.set(SNEEZE_PROGRESS, progress);
    }

    @Override
    public int getSneezeProgress() {
        return this.entityData.get(SNEEZE_PROGRESS);
    }

    @Override
    public void setSpecialFlag(int flag, boolean value) {
        byte byte_1 = entityData.get(PANDA_FLAGS);
        if (value) {
            entityData.set(PANDA_FLAGS, (byte)(byte_1 | flag));
        } else {
            entityData.set(PANDA_FLAGS, (byte)(byte_1 & ~flag));
        }
        getPetUser().updateDataMenu();
    }

    @Override
    public boolean getSpecialFlag(int flag) {
        return (entityData.get(PANDA_FLAGS) & flag) != 0x0;
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
                this.playSound(SoundEvents.PANDA_PRE_SNEEZE, 1.0F, 1.0F);
            }
        }
    }

    private void handleSneeze() {
        Vec3 var0 = this.getDeltaMovement();
        double x = (this.getX() - (double)(this.getBbWidth() + 1.0F) * 0.5D * (double) Mth.sin(this.yBodyRot * 0.017453292F));
        double y = (this.getY() + this.getEyeY() - 0.10000000149011612D);
        double z = (this.getZ() + (double)(this.getBbHeight() + 1.0F) * 0.5D * (double)Mth.cos(this.yBodyRot * 0.017453292F));
        VersionTranslator.getEntityLevel(this).addParticle(ParticleTypes.SNEEZE, x, y, z, var0.x, 0.0D, var0.z);
        this.playSound(SoundEvents.PANDA_SNEEZE, 1.0F, 1.0F);

        List<EntityPandaPet> nearby = VersionTranslator.getEntityLevel(this).getEntitiesOfClass(EntityPandaPet.class, this.getBoundingBox().inflate(10.0D));
        nearby.forEach(panda -> {
            if (panda.onGround && !panda.isInWater() && panda.isSpookedBySneeze()) {
                panda.jumpFromGround();
            }
        });
    }

    private boolean isSpookedBySneeze () {
        if (isLyingOnBack()) return false;
        return !isPlaying();
    }
}
