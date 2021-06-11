package simplepets.brainsynder.versions.v1_17_R1.entity.branch;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.sounds.SoundMaker;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import simplepets.brainsynder.api.entity.misc.IEntityWizard;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.WizardSpell;

public class EntityIllagerWizardPet extends EntityIllagerAbstractPet implements IEntityWizard {
    private static final EntityDataAccessor<Byte> SPELL;

    public EntityIllagerWizardPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setEnum("spell", getSpell());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("spell")) setSpell(object.getEnum("spell", WizardSpell.class, WizardSpell.NONE));
        super.applyCompound(object);
    }

    @Override
    public WizardSpell getSpell() {
        return WizardSpell.fromID(this.entityData.get(SPELL));
    }

    @Override
    public void setSpell(WizardSpell spell) {
        this.entityData.set(SPELL, (byte) spell.getId());
        if (spell == WizardSpell.WOLOLO) {
            SoundMaker.ENTITY_EVOCATION_ILLAGER_PREPARE_WOLOLO.playSound(getEntity());
        } else {
            SoundMaker.ENTITY_EVOCATION_ILLAGER_CAST_SPELL.playSound(getEntity());
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.entityData.get(SPELL) > 0) {
            WizardSpell spell = this.getSpell();
            double velocityX = spell.getArray()[0];
            double velocityY = spell.getArray()[1];
            double velocityZ = spell.getArray()[2];

            // Translation: aA = bodyYaw
            float value = this.yBodyRot * 0.017453292F + Mth.cos(this.tickCount * 0.6662F) * 0.25F;
            float offsetX = Mth.cos(value);
            float offsetZ = Mth.sin(value);
            this.level.addParticle(ParticleTypes.ENTITY_EFFECT, getX() + offsetX * 0.6D, getY() + 1.8D, getZ() + offsetZ * 0.6D, velocityX, velocityY, velocityZ);
            this.level.addParticle(ParticleTypes.ENTITY_EFFECT, getX() - offsetX * 0.6D, getY() + 1.8D, getZ() - offsetZ * 0.6D, velocityX, velocityY, velocityZ);
        }

    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(SPELL, (byte) 0);
    }

    static {
        SPELL = SynchedEntityData.defineId(EntityIllagerWizardPet.class, EntityDataSerializers.BYTE);
    }
}
