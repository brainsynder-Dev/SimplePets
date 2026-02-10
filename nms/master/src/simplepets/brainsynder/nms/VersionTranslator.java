package simplepets.brainsynder.nms;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_21_R7.util.CraftNamespacedKey;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simplepets.brainsynder.nms.entity.EntityPet;

import java.lang.reflect.Field;
import java.util.Optional;

@Deprecated
public class VersionTranslator {
    public static org.bukkit.entity.Entity getBukkitEntity (Entity entity) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static Field getJumpField () {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static void setAttributes (EntityPet entityPet, double walkSpeed, double flySpeed) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static void setFollowAttributes (EntityPet entityPet, double value) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    // Only used for 1.20.5+
    public static void setScaleSize (EntityPet entityPet, double value) {}

    // Only used for 1.20.5+
    public static double getScaleSize (EntityPet entityPet) {
        return 1.0;
    }

    public static double getWalkSpeed (EntityPet entityPet) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static double getFlySpeed (EntityPet entityPet) {
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

    public static Packet<ClientGamePacketListener> getAddEntityPacket(LivingEntity livingEntity, EntityType<?> originalEntityType, BlockPos pos) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static Packet<ClientGamePacketListener> getAddEntityPacket(LivingEntity livingEntity, ServerEntity serverEntity, EntityType<?> originalEntityType, BlockPos pos) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static String getEntityTypeVariable() {
        return "c";
    }

    public static boolean useInteger() {
        return true;
    }


    // ADDED DURING 1.19.4 DEVELOPMENT
    public static final EntityDataSerializer<Optional<BlockState>> OPTIONAL_BLOCK_STATE = null;

    public static void calculateEntityAnimation (LivingEntity entity, boolean var) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }
    public static void setMapUpStep (Entity entity, float value) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }
    public static BlockPos getPosition (Entity entity) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    // ADDED DURING 1.20 DEVELOPMENT
    public static Level getEntityLevel (Entity entity) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    // ADDED DURING 1.20.5 DEVELOPMENT
    public static boolean isWalkable (EntityPet entity, BlockPos.MutableBlockPos blockposition) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static void overrideAttributeMap (EntityPet entityPet) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    // ADDED DURING 1.21.3 DEVELOPMENT
    public static <T> T getRegistryValue (Registry<T> registry, NamespacedKey key) {
        registry.get(CraftNamespacedKey.toMinecraft(key));
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static void killEntity (Entity entity, ServerLevel level) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    public static ClientboundTeleportEntityPacket getTeleportPacket (Entity entity) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }

    // ADDED DURING 1.21.4 DEVELOPMENT
    public static void setupFlyingNavigation (EntityPet entityPet, Level level, FlyingPathNavigation navigation) {
        // throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        // navigation.setCanPassDoors(true);
    }

    // ADDED DURING 1.21.5 DEVELOPMENT
    public static void moveTo (Entity entityPet, double x, double y, double z, float yaw, float pitch) {
        throw new UnsupportedOperationException ("Missing support for "+ ServerVersion.getVersion().name());
    }
}
