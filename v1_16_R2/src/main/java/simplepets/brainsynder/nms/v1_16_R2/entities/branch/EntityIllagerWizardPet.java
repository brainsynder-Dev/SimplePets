package simplepets.brainsynder.nms.v1_16_R2.entities.branch;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.sounds.SoundMaker;
import net.minecraft.server.v1_16_R2.*;
import simplepets.brainsynder.api.entity.misc.IEntityWizard;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.utils.DataWatcherWrapper;
import simplepets.brainsynder.wrapper.WizardSpell;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityIllagerWizard}
 */
public abstract class EntityIllagerWizardPet extends EntityIllagerAbstractPet implements IEntityWizard {
    private static final DataWatcherObject<Byte> SPELL;

    static {
        SPELL = DataWatcher.a(EntityIllagerWizardPet.class, DataWatcherWrapper.BYTE);
    }

    private int counter;
    private WizardSpell spell;

    public EntityIllagerWizardPet(EntityTypes<? extends EntityInsentient> type, World var1) {
        super(type, var1);
        this.spell = WizardSpell.NONE;
    }

    public EntityIllagerWizardPet(EntityTypes<? extends EntityInsentient> type, World world, IPet pet) {
        super(type, world, pet);
        this.spell = WizardSpell.NONE;
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(SPELL, (byte) 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound json = super.asCompound();
        json.setString("spell", spell.name());
        return json;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("spell")) {
            WizardSpell var1 = WizardSpell.getByName(object.getString("spell"));
            this.spell = var1;
            this.datawatcher.set(SPELL, (byte) var1.getId());
        }
        super.applyCompound(object);
    }

    private boolean hasSpell() {
        return this.world.isClientSide ? this.datawatcher.get(SPELL) > 0 : this.counter > 0;
    }

    @Override
    public WizardSpell getSpell() {
        return !this.world.isClientSide ? this.spell : WizardSpell.fromID(this.datawatcher.get(SPELL));
    }

    @Override
    public void setSpell(WizardSpell var1) {
        this.spell = var1;
        this.datawatcher.set(SPELL, (byte) var1.getId());
        if (var1 == WizardSpell.WOLOLO) {
            SoundMaker.ENTITY_EVOCATION_ILLAGER_PREPARE_WOLOLO.playSound(getEntity());
        } else {
            SoundMaker.ENTITY_EVOCATION_ILLAGER_CAST_SPELL.playSound(getEntity());
        }
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (this.counter > 0) {
            --this.counter;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isClientSide && hasSpell()) {
            WizardSpell spell = this.getSpell();
            double var1 = spell.getArray()[0];
            double var3 = spell.getArray()[1];
            double var5 = spell.getArray()[2];
            float var7 = this.aK * 0.017453292F + MathHelper.cos((float)this.ticksLived * 0.6662F) * 0.25F;
            float var8 = MathHelper.cos(var7);
            float var9 = MathHelper.sin(var7);
            this.world.addParticle(Particles.ENTITY_EFFECT, this.locX() + (double)var8 * 0.6D, this.locY() + 1.8D, this.locZ() + (double)var9 * 0.6D, var1, var3, var5);
            this.world.addParticle(Particles.ENTITY_EFFECT, this.locX() - (double)var8 * 0.6D, this.locY() + 1.8D, this.locZ() - (double)var9 * 0.6D, var1, var3, var5);
        }

    }
}
