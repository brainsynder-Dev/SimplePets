package simplepets.brainsynder.nms.entity.list;

import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.block.state.BlockState;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import simplepets.brainsynder.api.entity.hostile.IEntityEndermanPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import java.util.Optional;

import static simplepets.brainsynder.api.pet.PetDataRegistry.Enderman.SCREAM;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Enderman} // 26.3+
 * NMS: {@link net.minecraft.world.entity.monster.EnderMan} // 26.2 and below
 */
public class EntityEndermanPet extends EntityPetOverride implements IEntityEndermanPet {
    private static final EntityDataAccessor<Optional<BlockState>> CARRIED_BLOCK = SynchedEntityData.defineId(EntityEndermanPet.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE);
    private static final EntityDataAccessor<Boolean> SCREAMING = SynchedEntityData.defineId(EntityEndermanPet.class, EntityDataSerializers.BOOLEAN);

    public EntityEndermanPet(PetType type, PetUser user) {
        super(EntitySelector.ENDERMAN, type, user);
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
        object.setBoolean(SCREAM.namespace(), isScreaming());
        Optional<BlockState> data = entityData.get(CARRIED_BLOCK);
        data.ifPresent(iBlockData -> object.setString("carried_block", BlockStateParser.serialize(iBlockData)));
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(SCREAM.namespace())) setScreaming(object.getBoolean(SCREAM.namespace()));
        if (object.hasKey("carried_block")) {
            String raw = object.getString("carried_block", Material.STONE.createBlockData().getAsString());
            try {
                BlockData data = Bukkit.createBlockData(raw);
                if (data != null) {
                    if (data.getMaterial().name().contains("AIR")) {
                        entityData.set(CARRIED_BLOCK, Optional.empty());
                    }else{
                        BlockState blockData = ((CraftBlockData) data).getState();
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
        BlockState blockData = this.entityData.get(CARRIED_BLOCK).orElse(null);
        if (blockData == null) return Material.AIR.createBlockData();
        return Bukkit.createBlockData(BlockStateParser.serialize(blockData));
    }

    @Override
    public void setCarriedBlock(BlockData blockData) {
        entityData.set(CARRIED_BLOCK, Optional.ofNullable(((CraftBlockData) blockData).getState()));
    }
}
