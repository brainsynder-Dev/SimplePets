package simplepets.brainsynder.nms;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simplepets.brainsynder.nms.entity.EntityPet;

import java.lang.reflect.Field;
import java.util.Optional;

@Deprecated
public class VersionTranslator {
    public static final String ENTITY_DATA_MAP = "f";
    public static final String ENTITY_FACTORY_FIELD = "bm";

    public static Field getJumpField () {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static void setAttributes (EntityPet entityPet, double walkSpeed, double flySpeed) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static void setItemSlot(ArmorStand stand, EquipmentSlot enumitemslot, ItemStack itemstack, boolean silent) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static boolean addEntity (Level level, Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static <T extends Entity> T getEntityHandle(org.bukkit.entity.Entity entity) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static <T extends Level> T getWorldHandle(World world) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static BlockState getBlockState(BlockData blockData) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static BlockData fromNMS(BlockState blockData) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static ItemStack toNMSStack(org.bukkit.inventory.ItemStack itemStack) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static org.bukkit.inventory.ItemStack toBukkit(ItemStack itemStack) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static BlockPos subtract (BlockPos blockPos, Vec3i vec) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static BlockPos relative (BlockPos blockPos) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static void modifyGlowData (SynchedEntityData toCloneDataWatcher, SynchedEntityData newDataWatcher, boolean glow) throws IllegalAccessException {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static org.bukkit.inventory.ItemStack toItemStack(StorageTagCompound compound) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static StorageTagCompound fromItemStack(org.bukkit.inventory.ItemStack item) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static float cube(float f) {
        return f * f * f;
    }

    public static EntityType fetchEntityType (String name) {
        // The EntityType.byString() method requires the name to start with `minecraft:` and the name of the mob to be lowercase
        Optional<EntityType<?>> optional = EntityType.byString("minecraft:"+name.toLowerCase());
        if (optional.isPresent()) return optional.get();

        // This is a simple placeholder mob that does not have any datawatchers just in case the code fails
        return EntityType.GIANT;
    }

    public static String getEntityTypeVariable() {
        return "c";
    }

    public static boolean useInteger() {
        return true;
    }
}
