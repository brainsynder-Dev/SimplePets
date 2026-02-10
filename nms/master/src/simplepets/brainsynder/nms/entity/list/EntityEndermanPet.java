package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import simplepets.brainsynder.api.entity.hostile.IEntityEndermanPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import java.util.Optional;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Enderman}
 */
public class EntityEndermanPet extends EntityPetOverride implements IEntityEndermanPet {
    private static final EntityDataAccessor<Optional<BlockState>> CARRIED_BLOCK = SynchedEntityData.defineId(EntityEndermanPet.class, VersionTranslator.OPTIONAL_BLOCK_STATE);
    private static final EntityDataAccessor<Boolean> SCREAMING = SynchedEntityData.defineId(EntityEndermanPet.class, EntityDataSerializers.BOOLEAN);

    public EntityEndermanPet(PetType type, PetUser user) {
        super(EntityType.ENDERMAN, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        data.add("screaming", isScreaming());
        data.add("carried-block", getCarriedBlock().getAsString());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(CARRIED_BLOCK, Optional.empty());
        dataAccess.define(SCREAMING, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("screaming", isScreaming());
        Optional<BlockState> data = entityData.get(CARRIED_BLOCK);
        data.ifPresent(iBlockData -> object.setString("carried_block", VersionTranslator.fromNMS(iBlockData).getAsString()));
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
                        entityData.set(CARRIED_BLOCK, Optional.empty());
                    }else{
                        BlockState blockData = VersionTranslator.getBlockState(data);
                        entityData.set(CARRIED_BLOCK, Optional.of(blockData));
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
        return this.entityData.get(SCREAMING);
    }

    @Override
    public void setScreaming(boolean flag) {
        this.entityData.set(SCREAMING, flag);
    }

    @Override
    public BlockData getCarriedBlock() {
        BlockState blockData = (BlockState)((Optional)this.entityData.get(CARRIED_BLOCK)).orElse(null);
        return VersionTranslator.fromNMS(blockData);
    }

    @Override
    public void setCarriedBlock(BlockData blockData) {
        entityData.set(CARRIED_BLOCK, Optional.ofNullable(VersionTranslator.getBlockState(blockData)));
    }
}
