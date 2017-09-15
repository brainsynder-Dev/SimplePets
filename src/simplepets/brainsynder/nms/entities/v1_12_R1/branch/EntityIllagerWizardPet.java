package simplepets.brainsynder.nms.entities.v1_12_R1.branch;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Sound;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.wrapper.WizardSpell;

public abstract class EntityIllagerWizardPet extends EntityIllagerAbstractPet {
    private static final DataWatcherObject<Byte> c;

    static {
        c = DataWatcher.a(EntityIllagerWizardPet.class, DataWatcherRegistry.a);
    }

    protected int b;
    private WizardSpell spell = WizardSpell.NONE;

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
        this.datawatcher.register(c, (byte) 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        System.out.println("asCompound");
        StorageTagCompound json = super.asCompound();
        json.setInteger("Spell", spell.getId());
        return json;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Spell")) {
            WizardSpell var1 = WizardSpell.fromID(object.getInteger("Spell"));
            this.spell = var1;
            this.datawatcher.set(c, (byte) var1.getId());
        }
        super.applyCompound(object);
    }

    private boolean dl() {
        return this.world.isClientSide ? this.datawatcher.get(c) > 0 : this.b > 0;
    }

    public WizardSpell getSpell() {
        return !this.world.isClientSide ? this.spell : WizardSpell.fromID(this.datawatcher.get(c));
    }

    public void setSpell(WizardSpell var1) {
        System.out.println("Spell: " + var1.toString());
        if (var1 == WizardSpell.WOLOLO) {
            getEntity().getWorld().playSound(getEntity().getLocation(), Sound.ENTITY_EVOCATION_ILLAGER_PREPARE_WOLOLO, 1.0F, 1.0F);
        } else {
            getEntity().getWorld().playSound(getEntity().getLocation(), Sound.ENTITY_EVOCATION_ILLAGER_CAST_SPELL, 1.0F, 1.0F);
        }
        this.spell = var1;
        this.datawatcher.set(c, (byte) var1.getId());
    }

    protected void M() {
        super.M();
        if (this.b > 0) {
            --this.b;
        }
    }

    public void B_() {
        super.B_();
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