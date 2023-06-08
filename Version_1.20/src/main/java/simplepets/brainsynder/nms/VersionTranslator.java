package simplepets.brainsynder.nms;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lib.brainsynder.ServerVersion;
import lib.brainsynder.nbt.JsonToNBT;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.nbt.other.NBTException;
import lib.brainsynder.storage.RandomCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftNamespacedKey;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simplepets.brainsynder.api.entity.misc.IFlyableEntity;
import simplepets.brainsynder.nms.entity.EntityPet;
import simplepets.brainsynder.nms.utils.FieldUtils;
import simplepets.brainsynder.nms.utils.InvalidInputException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

public class VersionTranslator {
    // net.minecraft.network.syncher.DataWatcher
    // private static final it.unimi.dsi.fastutil.objects.Object2IntMap<java.lang.Class<? extends net.minecraft.world.entity.Entity>>
    public static final String ENTITY_DATA_MAP = "b";
    // net.minecraft.world.entity.EntityTypes
    // private final net.minecraft.world.entity.EntityTypes.b<T>
    public static final String ENTITY_FACTORY_FIELD = "bA";

    // net.minecraft.core.RegistryMaterials
    // private boolean
    public static final String REGISTRY_FROZEN_FIELD = "l";
    // net.minecraft.core.RegistryMaterials
    // @Nullable private java.util.Map<T, net.minecraft.core.Holder.c<T>>
    public static final String REGISTRY_ENTRY_MAP_FIELD = "m";

    private static Field jumpingField = null;

    public static Field getJumpField() {
        if (jumpingField != null) return jumpingField;

        try {
            /*
                net.minecraft.world.entity.EntityLiving

                protected int bg
                public float bh
                protected boolean bi  <---- This one
                public float bj
                public float bk
                public float bl
                protected int bm
             */
            Field jumpingField = LivingEntity.class.getDeclaredField("bi"); // For 1.20
            jumpingField.setAccessible(true);
            return VersionTranslator.jumpingField = jumpingField;
        } catch (Exception ex) {
            throw new UnsupportedOperationException("Unable to find the correct jumpingField name for " + ServerVersion.getVersion().name());
        }
    }

    public static void setAttributes(EntityPet entityPet, double walkSpeed, double flySpeed) {
        if (walkSpeed != -1) entityPet.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(walkSpeed);
        if ((flySpeed != -1) && (entityPet instanceof IFlyableEntity) && entityPet.getAttribute(Attributes.FLYING_SPEED) != null) {
            entityPet.getAttribute(Attributes.FLYING_SPEED).setBaseValue(flySpeed);
        }
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
                        ENTITY_DATA_MAP, true);

        SynchedEntityData.DataItem<Byte> item = newMap.get(0);
        byte initialBitMask = item.getValue();
        byte bitMaskIndex = (byte) 6;
        if (glow) {
            item.setValue((byte) (initialBitMask | 1 << bitMaskIndex));
        } else {
            item.setValue((byte) (initialBitMask & ~(1 << bitMaskIndex)));
        }
        FieldUtils.writeDeclaredField(newDataWatcher, ENTITY_DATA_MAP, newMap, true);
    }

    public static org.bukkit.inventory.ItemStack toItemStack(StorageTagCompound compound) {
        if (!compound.hasKey("id")) { // The ID MUST be set, otherwise it will be considered invalid and AIR
            return new org.bukkit.inventory.ItemStack(Material.AIR);
        } else {
            // Item has to be AT LEAST 1 otherwise it will be AIR
            if (!compound.hasKey("Count")) compound.setByte("Count", (byte) 1);

            try {
                CompoundTag compoundTag = TagParser.parseTag(compound.toString());
                ItemStack nmsItem = ItemStack.of(compoundTag);
                return CraftItemStack.asBukkitCopy(nmsItem);
            } catch (CommandSyntaxException e) {
                throw new InvalidInputException("Failed to parse Item NBT", e);
            }
        }
    }

    public static StorageTagCompound fromItemStack(org.bukkit.inventory.ItemStack item) {
        CompoundTag compoundTag = new CompoundTag();
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        compoundTag = nmsItem.save(compoundTag);

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

    public static Packet<ClientGamePacketListener> getAddEntityPacket(LivingEntity livingEntity, EntityType<?> originalEntityType, BlockPos pos) {
        Packet<ClientGamePacketListener> packet;
        try {
            // y'all here sum'n?
            packet = new ClientboundAddEntityPacket(livingEntity);
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
        return "e";
    }

    public static boolean useInteger() {
        return false;
    }


    // ADDED DURING 1.20 DEVELOPMENT
    public static final EntityDataSerializer<Optional<BlockState>> OPTIONAL_BLOCK_STATE = EntityDataSerializers.OPTIONAL_BLOCK_STATE;

    public static void calculateEntityAnimation (LivingEntity entity, boolean var) {
        entity.calculateEntityAnimation(var);
    }

    public static void setMapUpStep (Entity entity, float value) {
        entity.setMaxUpStep(value);
    }
    public static BlockPos getPosition (Entity entity) {
        return BlockPos.containing(new Vec3(entity.getX(), entity.getY(), entity.getZ()));
    }

    public static ResourceLocation toMinecraftResource (NamespacedKey key) {
        return CraftNamespacedKey.toMinecraft(key);
    }

    public static NamespacedKey toBukkitNamespace (ResourceLocation resource) {
        return CraftNamespacedKey.fromMinecraft(resource);
    }
}