package simplepets.brainsynder.nms.v1_12_R1.entities.list;

import net.minecraft.server.v1_12_R1.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityParrotPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_12_R1.entities.EntityTameablePet;
import simplepets.brainsynder.nms.v1_12_R1.entities.movements.ParrotController;
import simplepets.brainsynder.wrapper.ParrotVariant;

@Size(width = 0.5F, length = 0.9F)
public class EntityParrotPet extends EntityTameablePet implements IEntityParrotPet {
    private static final DataWatcherObject<Integer> TYPE;
    private boolean rainbow = false;
    private int toggle = 0;

    static {
        TYPE = DataWatcher.a(EntityParrotPet.class, DataWatcherRegistry.b);
    }


    public EntityParrotPet(World world) {
        super(world);
    }
    public EntityParrotPet(World world, IPet pet) {
        super(world, pet);
        moveController = new ParrotController(this);
        this.setSize(0.5F, 0.9F);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(TYPE, 0);
    }

    /**
     * NMS translates (Thanks Forge):
     * <p>
     * NavigationFlying#a() = setCanOpenDoors();
     * NavigationFlying#c() = setCanFloat();
     * NavigationFlying#b() = setCanEnterDoors();
     */
    @Override
    protected NavigationAbstract b(World var1) {
        NavigationFlying var2 = new NavigationFlying(this, var1);
        var2.a(false);
        var2.c(true);
        var2.b(true);
        return var2;
    }

    @Override
    public void repeatTask() {
        super.repeatTask();
        if (rainbow) {
            if (toggle == 4) {
                setVariant(ParrotVariant.getNext(getVariant()));
                toggle = 0;
            }
            toggle++;
        }
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
        if (!rainbow)
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

    @Override
    public boolean isRainbow() {
        return rainbow;
    }

    @Override
    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }
}
