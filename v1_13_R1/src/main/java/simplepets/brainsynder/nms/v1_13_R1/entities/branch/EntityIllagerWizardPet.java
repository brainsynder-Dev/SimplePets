package simplepets.brainsynder.nms.v1_13_R1.entities.branch;

import net.minecraft.server.v1_13_R1.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R1.utils.DataWatcherWrapper;
import simplepets.brainsynder.wrapper.WizardSpell;

public abstract class EntityIllagerWizardPet extends EntityIllagerAbstractPet {
    private static final DataWatcherObject<Byte> SPELL;

    static {
        SPELL = DataWatcher.a(EntityIllagerWizardPet.class, DataWatcherWrapper.BYTE);
    }

    protected int b;
    private WizardSpell spell = WizardSpell.NONE;

    public EntityIllagerWizardPet(EntityTypes<?> type, World var1) {
        super(type, var1);
        this.spell = WizardSpell.NONE;
    }

    public EntityIllagerWizardPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
        this.spell = WizardSpell.NONE;
    }

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

    private boolean dA() {
        return this.world.isClientSide ? this.datawatcher.get(SPELL) > 0 : this.b > 0;
    }

    public WizardSpell getSpell() {
        return !this.world.isClientSide ? this.spell : WizardSpell.fromID(this.datawatcher.get(SPELL));
    }

    public void setSpell(WizardSpell var1) {
        if (var1 == WizardSpell.WOLOLO) {
            SoundMaker.ENTITY_EVOCATION_ILLAGER_PREPARE_WOLOLO.playSound(getEntity());
        } else {
            SoundMaker.ENTITY_EVOCATION_ILLAGER_CAST_SPELL.playSound(getEntity());
        }
        this.spell = var1;
        this.datawatcher.set(SPELL, (byte) var1.getId());
    }

    /**
     * Handles the Ambient Sound playing
     *
     * Search for: Method is empty
     * Class: EntityInsentient
     */
    protected void mobTick() {
        super.mobTick();
        if (this.b > 0) {
            --this.b;
        }
    }

    /**
     * Handles the Ambient Sound playing
     *
     * Search for: EnumItemSlot enumitemslot = aenumitemslot[
     * Class: EntityLiving
     */
    public void tick() {
        super.tick();
        if (this.world.isClientSide && this.dA()) {
            WizardSpell var1 = getSpell();
            double var2 = var1.getArray()[0];
            double var4 = var1.getArray()[1];
            double var6 = var1.getArray()[2];
            float var8 = this.aN * 0.017453292F + MathHelper.cos((float) this.ticksLived * 0.6662F) * 0.25F;
            float var9 = MathHelper.cos(var8);
            float var10 = MathHelper.sin(var8);
            this.world.addParticle(Particles.s, this.locX + (double) var9 * 0.6D, this.locY + 1.8D, this.locZ + (double) var10 * 0.6D, var2, var4, var6);
            this.world.addParticle(Particles.s, this.locX - (double) var9 * 0.6D, this.locY + 1.8D, this.locZ - (double) var10 * 0.6D, var2, var4, var6);
        }

    }
}