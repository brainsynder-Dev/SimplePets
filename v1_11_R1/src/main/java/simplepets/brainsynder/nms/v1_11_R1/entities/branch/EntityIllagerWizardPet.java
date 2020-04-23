package simplepets.brainsynder.nms.v1_11_R1.entities.branch;

import net.minecraft.server.v1_11_R1.*;
import lib.brainsynder.sounds.SoundMaker;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_11_R1.entities.EntityPet;
import simplepets.brainsynder.wrapper.WizardSpell;

public abstract class EntityIllagerWizardPet extends EntityPet {
    private static final DataWatcherObject<Byte> SPELL;

    static {
        SPELL = DataWatcher.a(EntityIllagerWizardPet.class, DataWatcherRegistry.a);
    }

    protected int b;
    private WizardSpell spell;

    public EntityIllagerWizardPet(World var1) {
        super(var1);
        this.spell = WizardSpell.NONE;
    }

    public EntityIllagerWizardPet(World world, IPet pet) {
        super(world, pet);
        this.spell = WizardSpell.NONE;
    }

    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(SPELL, (byte) 0);
    }

    private boolean dl() {
        return this.world.isClientSide ? this.datawatcher.get(SPELL) > 0 : this.b > 0;
    }

    public WizardSpell getSpell() {
        return !this.world.isClientSide ? this.spell : WizardSpell.fromID(this.datawatcher.get(SPELL));
    }

    public void setSpell(WizardSpell var1) {
        if (var1.getId() == 0 || var1.getId() == 1 || var1.getId() == 2 || var1.getId() == 3) {
            if (var1 == WizardSpell.WOLOLO) {
                SoundMaker.ENTITY_EVOCATION_ILLAGER_PREPARE_WOLOLO.playSound(getEntity());
            } else {
                SoundMaker.ENTITY_EVOCATION_ILLAGER_CAST_SPELL.playSound(getEntity());
            }
            this.spell = var1;
            this.datawatcher.set(SPELL, (byte) var1.getId());
        }
    }

    protected void M() {
        super.M();
        if (this.b > 0) {
            --this.b;
        }
    }

    public void A_() {
        super.A_();
        if (this.world.isClientSide && this.dl()) {
            WizardSpell var1 = getSpell();
            double var2 = var1.getArray()[0];
            double var4 = var1.getArray()[1];
            double var6 = var1.getArray()[2];
            float var8 = this.aN * 0.017453292F + MathHelper.cos((float) this.ticksLived * 0.6662F) * 0.25F;
            float var9 = MathHelper.cos(var8);
            float var10 = MathHelper.sin(var8);
            this.world.addParticle(EnumParticle.SPELL_MOB, this.locX + (double) var9 * 0.6D, this.locY + 1.8D, this.locZ + (double) var10 * 0.6D, var2, var4, var6);
            this.world.addParticle(EnumParticle.SPELL_MOB, this.locX - (double) var9 * 0.6D, this.locY + 1.8D, this.locZ - (double) var10 * 0.6D, var2, var4, var6);
        }
    }
}