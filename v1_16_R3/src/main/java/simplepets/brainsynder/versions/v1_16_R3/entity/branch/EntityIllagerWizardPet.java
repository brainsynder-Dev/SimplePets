package simplepets.brainsynder.versions.v1_16_R3.entity.branch;

import lib.brainsynder.sounds.SoundMaker;
import net.minecraft.server.v1_16_R3.*;
import simplepets.brainsynder.api.entity.misc.IEntityWizard;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.WizardSpell;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

public class EntityIllagerWizardPet extends EntityIllagerAbstractPet implements IEntityWizard {
    private static final DataWatcherObject<Byte> SPELL;

    public EntityIllagerWizardPet(EntityTypes<? extends EntityInsentient> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    public WizardSpell getSpell() {
        return WizardSpell.fromID(this.datawatcher.get(SPELL));
    }

    @Override
    public void setSpell(WizardSpell spell) {
        this.datawatcher.set(SPELL, (byte) spell.getId());
        if (spell == WizardSpell.WOLOLO) {
            SoundMaker.ENTITY_EVOCATION_ILLAGER_PREPARE_WOLOLO.playSound(getEntity());
        } else {
            SoundMaker.ENTITY_EVOCATION_ILLAGER_CAST_SPELL.playSound(getEntity());
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.datawatcher.get(SPELL) > 0) {
            WizardSpell spell = this.getSpell();
            double velocityX = spell.getArray()[0];
            double velocityY = spell.getArray()[1];
            double velocityZ = spell.getArray()[2];

            // Translation: aA = bodyYaw
            float value = this.aA * 0.017453292F + MathHelper.cos((float)this.ticksLived * 0.6662F) * 0.25F;
            float offsetX = MathHelper.cos(value);
            float offsetZ = MathHelper.sin(value);
            this.world.addParticle(Particles.ENTITY_EFFECT, this.locX() + (double)offsetX * 0.6D, this.locY() + 1.8D, this.locZ() + (double)offsetZ * 0.6D, velocityX, velocityY, velocityZ);
            this.world.addParticle(Particles.ENTITY_EFFECT, this.locX() - (double)offsetX * 0.6D, this.locY() + 1.8D, this.locZ() - (double)offsetZ * 0.6D, velocityX, velocityY, velocityZ);
        }

    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(SPELL, (byte) 0);
    }

    static {
        SPELL = DataWatcher.a(EntityIllagerWizardPet.class, DataWatcherWrapper.BYTE);
    }
}
