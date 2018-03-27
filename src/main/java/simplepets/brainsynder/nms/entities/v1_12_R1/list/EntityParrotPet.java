package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.entity.Player;
import simple.brainsynder.nbt.StorageTagCompound;
import simple.brainsynder.nms.IActionMessage;
import simple.brainsynder.utils.Reflection;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityParrotPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.CustomMoveFlying;
import simplepets.brainsynder.nms.entities.v1_12_R1.EntityTameablePet;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.wrapper.ParrotVariant;

@Size(width = 0.5F, length = 0.9F)
public class EntityParrotPet extends EntityTameablePet implements IEntityParrotPet {
    private static final DataWatcherObject<Integer> TYPE;

    static {
        TYPE = DataWatcher.a(EntityParrotPet.class, DataWatcherRegistry.b);
    }

    private boolean rainbow = false;
    private int toggle = 0;
    private int r = 0;
    private boolean didClick = false;
    private int l = 0;

    public EntityParrotPet(World world) {
        super(world);
    }
    public EntityParrotPet(World world, IPet pet) {
        super(world, pet);
        moveController = new CustomMoveFlying(this);
        this.setSize(0.5F, 0.9F);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(TYPE, 0);
    }

    @Override
    protected NavigationAbstract b(World var1) {
        NavigationFlying var2 = new NavigationFlying(this, var1);
        var2.a(false);
        var2.c(false);
        var2.b(true);
        return var2;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (damagesource instanceof EntityDamageSource) {
            EntityDamageSource entityDamageSource = (EntityDamageSource) damagesource;
            if (entityDamageSource.getEntity() instanceof EntityHuman) {
                EntityHuman human = (EntityHuman) entityDamageSource.getEntity();
                if (human.getName().equals(getOwner().getName())) {
                    if (getOwner().isSneaking()) {
                        if (l == 0) {
                            l = 1;
                            return false;
                        }
                        l = 0;
                        r = 0;
                    }
                    if (rainbow) {
                        l = 0;
                        r = 0;
                        rainbow = false;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void repeatTask() {
        super.repeatTask();
        if (!rainbow) {
            if (l == 1) {
                if (r == 1) {
                    IActionMessage message = Reflection.getActionMessage();
                    message.sendMessage(getOwner(), "§4§lR§c§la§e§li§6§ln§a§lb§2§lo§b§lw §f§lmode Activated. L/R Click to cancel");
                    rainbow = true;
                    if (l != 0) l = 0;
                    if (r != 0) r = 0;
                }
            }
        }
        if (rainbow) {
            if (toggle == 4) {
                setVariant(ParrotVariant.getNext(getVariant()));
                toggle = 0;
            }
            toggle++;
        }
    }

    @Override
    public EnumInteractionResult a(EntityHuman human, Vec3D vec3d, EnumHand enumhand) {
        return this.onInteract((Player) human.getBukkitEntity()) ? EnumInteractionResult.SUCCESS : EnumInteractionResult.FAIL;
    }

    public boolean onInteract(Player p) {
        if (getPet() != null) {
            if (p.getName().equals(getPet().getOwner().getName())) {
                if (!didClick) {
                    if (p.isSneaking()) {
                        if (l == 1) {
                            if (r == 0) {
                                r = 1;
                                return false;
                            }
                        }
                        l = 0;
                        r = 0;
                    }
                    if (rainbow) {
                        l = 0;
                        r = 0;
                        rainbow = false;
                    }
                    PetCore.get().getInvLoaders().PET_DATA.open(PetOwner.getPetOwner(getOwner()));
                }
                didClick = (!didClick);
            }
        }
        return false;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("parrotcolor"))
            setVariant(ParrotVariant.getByName(object.getString("parrotcolor")));
        if (object.hasKey("rainbow"))
            rainbow = object.getBoolean("rainbow");
        super.applyCompound(object);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setString("parrotcolor", getVariant().name());
        object.setBoolean("rainbow", rainbow);
        return object;
    }

    @Override
    public ParrotVariant getVariant() {
        return ParrotVariant.getById(getDataWatcher().get(TYPE));
    }

    @Override
    public void setVariant(ParrotVariant variant) {
        this.datawatcher.set(TYPE, variant.ordinal());
    }
}
