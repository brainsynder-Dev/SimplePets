package simplepets.brainsynder.nms;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lib.brainsynder.ServerVersion;
import lib.brainsynder.internal.nbtapi.nbtapi.NBTContainer;
import lib.brainsynder.internal.nbtapi.nbtapi.NBTReflectionUtil;
import lib.brainsynder.nbt.JsonToNBT;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.nbt.other.NBTException;
import lib.brainsynder.reflection.FieldAccessor;
import lib.brainsynder.storage.RandomCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R1.util.CraftNamespacedKey;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simplepets.brainsynder.api.entity.misc.IFlyableEntity;
import simplepets.brainsynder.nms.entity.EntityPet;
import simplepets.brainsynder.nms.utils.FieldUtils;
import simplepets.brainsynder.nms.utils.InvalidInputException;
import simplepets.brainsynder.utils.VersionFields;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

public class VersionTranslator {
    private static final FieldAccessor<AttributeMap> accessor;
    private static Field jumpingField = null;

    static {
        accessor = FieldAccessor.getField(LivingEntity.class, VersionFields.v1_21_1.getAttributesField(), AttributeMap.class);

        try {
            Field jumpingField = LivingEntity.class.getDeclaredField(VersionFields.v1_21_1.getEntityJumpField());
            jumpingField.setAccessible(true);
            VersionTranslator.jumpingField = jumpingField;
        } catch (Exception ex) {
            throw new UnsupportedOperationException("Unable to find the correct jumpingField name for " + ServerVersion.getVersion().name());
        }
    }

    public static Field getJumpField() {
        return jumpingField;
    }

    public static void overrideAttributeMap (EntityPet entityPet) {
        accessor.set(entityPet, new AttributeMap(createAttributes(entityPet).build()));
    }

    private static AttributeSupplier.Builder createAttributes (EntityPet entityPet) {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        if (entityPet instanceof IFlyableEntity) builder.add(Attributes.FLYING_SPEED, 1);
        builder.add(Attributes.SCALE, 1);
        return builder.add(Attributes.MOVEMENT_SPEED, 1);
    }

    public static org.bukkit.entity.Entity getBukkitEntity(Entity entity) {
        org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity craftEntity = entity.getBukkitEntity();
        return craftEntity;
    }

    public static void setAttributes(EntityPet entityPet, double walkSpeed, double flySpeed) {
        if (walkSpeed != -1) entityPet.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(walkSpeed);
        if ((flySpeed != -1) && (entityPet instanceof IFlyableEntity) && entityPet.getAttribute(Attributes.FLYING_SPEED) != null) {
            entityPet.getAttribute(Attributes.FLYING_SPEED).setBaseValue(flySpeed);
        }
    }

    public static void setFollowAttributes (EntityPet entityPet, double value) {
        entityPet.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(value);
    }

    public static void setScaleSize (EntityPet entityPet, double value) {
        entityPet.getAttribute(Attributes.SCALE).setBaseValue(value);
    }

    public static double getWalkSpeed (EntityPet entityPet) {
        return entityPet.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
    }

    public static double getFlySpeed (EntityPet entityPet) {
        return entityPet.getAttribute(Attributes.FLYING_SPEED).getValue();
    }

    public static double getScaleSize (EntityPet entityPet) {
        return entityPet.getAttribute(Attributes.SCALE).getValue();
    }

    public static void setItemSlot(ArmorStand stand, EquipmentSlot enumitemslot, ItemStack itemstack, boolean silent) {
        stand.setItemSlot(enumitemslot, itemstack, silent);
    }

    public static boolean addEntity(Level level, Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        return level.addFreshEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    public static <T extends Entity> T getEntityHandle(org.bukkit.entity.Entity entity) {
        return (T) ((CraftEntity) entity).getHandle();
    }

    public static <T extends Level> T getWorldHandle(World world) {
        return (T) ((CraftWorld) world).getHandle();
    }

    public static BlockState getBlockState(BlockData blockData) {
        return ((CraftBlockData) blockData).getState();
    }

    public static BlockData fromNMS(BlockState blockData) {
        return CraftBlockData.fromData(blockData);
    }

    public static ItemStack toNMSStack(org.bukkit.inventory.ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    public static org.bukkit.inventory.ItemStack toBukkit(ItemStack itemStack) {
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    public static BlockPos subtract(BlockPos blockPos, Vec3i vec) {
        return blockPos.subtract(vec);
    }

    public static BlockPos relative(BlockPos blockPos) {
        return blockPos.relative(RandomCollection.fromCollection(Arrays.asList(
                Direction.NORTH,
                Direction.EAST,
                Direction.SOUTH,
                Direction.WEST
        )).next());
    }

    public static void modifyGlowData(SynchedEntityData toCloneDataWatcher, SynchedEntityData newDataWatcher,
                                      boolean glow) throws IllegalAccessException {
        Int2ObjectMap<SynchedEntityData.DataItem<Byte>> newMap =
                (Int2ObjectMap<SynchedEntityData.DataItem<Byte>>) FieldUtils.readDeclaredField(toCloneDataWatcher,
                        VersionFields.v1_21_1.getEntityDataMapField(), true);

        SynchedEntityData.DataItem<Byte> item = newMap.get(0);
        byte initialBitMask = item.getValue();
        byte bitMaskIndex = (byte) 6;
        if (glow) {
            item.setValue((byte) (initialBitMask | 1 << bitMaskIndex));
        } else {
            item.setValue((byte) (initialBitMask & ~(1 << bitMaskIndex)));
        }
        FieldUtils.writeDeclaredField(newDataWatcher, VersionFields.v1_21_1.getEntityDataMapField(), newMap, true);
    }

    public static org.bukkit.inventory.ItemStack toItemStack(StorageTagCompound compound) {
        if (!compound.hasKey("id")) { // The ID MUST be set, otherwise it will be considered invalid and AIR
            return new org.bukkit.inventory.ItemStack(Material.AIR);
        } else {
            // Item has to be AT LEAST 1 otherwise it will be AIR
            if (!compound.hasKey("Count")) compound.setByte("Count", (byte) 1);

            try {
                CompoundTag compoundTag = TagParser.parseTag(compound.toString());
                ItemStack nmsItem = (ItemStack) NBTReflectionUtil.convertNBTCompoundtoNMSItem(new NBTContainer(compoundTag));
                return CraftItemStack.asBukkitCopy(nmsItem);
            } catch (CommandSyntaxException e) {
                throw new InvalidInputException("Failed to parse Item NBT", e);
            }
        }
    }

    public static StorageTagCompound fromItemStack(org.bukkit.inventory.ItemStack item) {
        CompoundTag compoundTag = new CompoundTag();
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        compoundTag = (CompoundTag) NBTReflectionUtil.convertNMSItemtoNBTCompound(nmsItem).getCompound();
        // compoundTag = nmsItem.save(((CraftServer) Bukkit.getServer()).getServer().registryAccess(), compoundTag);

        try {
            return JsonToNBT.getTagFromJson(compoundTag.toString());
        } catch (NBTException exception) {
            throw new InvalidInputException("Failed to convert item to NBT", exception);
        }
    }

    public static float cube(float f) {
        return f * f * f;
    }

    public static EntityType fetchEntityType(String name) {
        // The EntityType.byString() method requires the name to start with `minecraft:` and the name of the mob to
        // be lowercase
        Optional<EntityType<?>> optional = EntityType.byString("minecraft:" + name.toLowerCase());
        if (optional.isPresent()) return optional.get();

        // This is a simple placeholder mob that does not have any datawatchers just in case the code fails
        return EntityType.GIANT;
    }

    public static Packet<ClientGamePacketListener> getAddEntityPacket(LivingEntity livingEntity, ServerEntity serverEntity, EntityType<?> originalEntityType, BlockPos pos) {
        Packet<ClientGamePacketListener> packet;
        try {
            // y'all here sum'n?
            packet = new ClientboundAddEntityPacket(livingEntity, serverEntity);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ClientboundAddEntityPacket(livingEntity, 0, pos);
        }

        try {
            Field type = packet.getClass().getDeclaredField(VersionTranslator.getEntityTypeVariable());
            type.setAccessible(true);
            type.set(packet, VersionTranslator.useInteger() ? BuiltInRegistries.ENTITY_TYPE.getId(originalEntityType) : originalEntityType);
            return packet;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return new ClientboundAddEntityPacket(livingEntity, 0, pos);
    }

    // net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity
    // private final net.minecraft.world.entity.EntityTypes<?>
    public static String getEntityTypeVariable() {
        return "f";
    }

    public static boolean useInteger() {
        return false;
    }


    // ADDED DURING 1.20.1 DEVELOPMENT
    public static final EntityDataSerializer<Optional<BlockState>> OPTIONAL_BLOCK_STATE = EntityDataSerializers.OPTIONAL_BLOCK_STATE;

    public static void calculateEntityAnimation(LivingEntity entity, boolean var) {
        entity.calculateEntityAnimation(var);
    }

    public static void setMapUpStep(Entity entity, float value) {
        // Was this even needed?
        // entity.setMaxUpStep(value);
    }

    public static BlockPos getPosition(Entity entity) {
        return BlockPos.containing(new Vec3(entity.getX(), entity.getY(), entity.getZ()));
    }

    public static ResourceLocation toMinecraftResource(NamespacedKey key) {
        return CraftNamespacedKey.toMinecraft(key);
    }

    public static NamespacedKey toBukkitNamespace(ResourceLocation resource) {
        return CraftNamespacedKey.fromMinecraft(resource);
    }

    // ADDED DURING 1.20.1 DEVELOPMENT
    public static Level getEntityLevel(Entity entity) {
        return entity.level();
    }

    // ADDED DURING 1.20.5 DEVELOPMENT
    public static boolean isWalkable (EntityPet entity, BlockPos.MutableBlockPos blockposition) {
        return WalkNodeEvaluator.getPathTypeStatic(entity, blockposition) == PathType.WALKABLE;
    }

    // ADDED DURING 1.21.3 DEVELOPMENT
    public static <T> T getRegistryValue (Registry<T> registry, NamespacedKey key) {
        return registry.get(CraftNamespacedKey.toMinecraft(key));
    }

    public static void killEntity (Entity entity, ServerLevel level) {
        entity.kill();
    }

    public static ClientboundTeleportEntityPacket getTeleportPacket (Entity entity) {
        return new ClientboundTeleportEntityPacket(entity);
    }

    // ADDED DURING 1.21.4 DEVELOPMENT
    public static void setupFlyingNavigation (EntityPet entityPet, Level level, FlyingPathNavigation navigation) {
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        navigation.setCanPassDoors(true);
    }
}