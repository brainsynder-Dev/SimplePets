package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.IBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;
import simplepets.brainsynder.api.entity.hostile.IEntityEndermanPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityPet;
import simplepets.brainsynder.versions.v1_17_R1.utils.DataWatcherWrapper;

import java.util.Optional;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityEnderman}
 */
public class EntityEndermanPet extends EntityPet implements IEntityEndermanPet {
    private static final DataWatcherObject<Optional<IBlockData>> CARRIED_BLOCK;
    private static final DataWatcherObject<Boolean> SCREAMING;

    public EntityEndermanPet(PetType type, PetUser user) {
        super(EntityTypes.ENDERMAN, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(CARRIED_BLOCK, Optional.empty());
        this.datawatcher.register(SCREAMING, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("screaming", isScreaming());
        Optional<IBlockData> data = datawatcher.get(CARRIED_BLOCK);
        data.ifPresent(iBlockData -> object.setString("carried_block", CraftBlockData.fromData(iBlockData).getAsString()));
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("screaming")) setScreaming(object.getBoolean("screaming"));
        if (object.hasKey("carried_block")) {
            String raw = object.getString("carried_block", Material.STONE.createBlockData().getAsString());
            try {
                BlockData data = Bukkit.createBlockData(raw);
                if (data != null) {
                    if (data.getMaterial().name().contains("AIR")) {
                        datawatcher.set(CARRIED_BLOCK, Optional.empty());
                    }else{
                        IBlockData blockData = ((CraftBlockData)data).getState();
                        datawatcher.set(CARRIED_BLOCK, Optional.of(blockData));
                    }
                }
            }catch (Exception e) {
                SimplePets.getDebugLogger().debug(DebugBuilder.build(getClass()).setLevel(DebugLevel.ERROR).setMessages(
                        "An error occured when trying to set the block in the enderman pets hand.",
                        "Result: "+raw,
                        "Error Message: "+e.getMessage()
                ));
            }
        }
        super.applyCompound(object);
    }

    @Override
    public boolean isScreaming() {
        return this.datawatcher.get(SCREAMING);
    }

    @Override
    public void setScreaming(boolean flag) {
        this.datawatcher.set(SCREAMING, flag);
    }

    @Override
    public BlockData getCarriedBlock() {
        IBlockData blockData = (IBlockData)((Optional)this.datawatcher.get(CARRIED_BLOCK)).orElse(null);
        return CraftBlockData.fromData(blockData);
    }

    @Override
    public void setCarriedBlock(BlockData blockData) {
        datawatcher.set(CARRIED_BLOCK, Optional.ofNullable(((CraftBlockData)blockData).getState()));
    }


    static {
        CARRIED_BLOCK = DataWatcher.a(EntityEndermanPet.class, DataWatcherWrapper.BLOCK);
        SCREAMING = DataWatcher.a(EntityEndermanPet.class, DataWatcherWrapper.BOOLEAN);
    }
}
