package simplepets.brainsynder.nms.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.NamespacedKey;
import simplepets.brainsynder.utils.VersionFields;

import java.lang.reflect.Field;

public interface VersionTranslator {
    String getVersionIdentifier();

    default <T> T getRegistryValue (Registry<T> registry, NamespacedKey key) {
        return registry.getValue(Identifier.fromNamespaceAndPath(key.getNamespace(), key.getKey()));
    }

    default void setInvulnerable (Entity entity, boolean invulnerable) {
        entity.setPermanentlyInvulnerable(invulnerable);
    }

    default Packet<ClientGamePacketListener> getAddEntityPacket(LivingEntity livingEntity, ServerEntity serverEntity, EntityType<?> originalEntityType, BlockPos pos) {
        Packet<ClientGamePacketListener> packet;
        try {
            // y'all here sum'n?
            packet = new ClientboundAddEntityPacket(livingEntity, serverEntity);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ClientboundAddEntityPacket(livingEntity, 0, pos);
        }

        try {
            // TODO: Refactor this mess, as it still uses the obfuscated field name
            Field type = packet.getClass().getDeclaredField(VersionFields.current().getPacketEntityType());
            type.setAccessible(true);
            type.set(packet, originalEntityType);
            return packet;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return new ClientboundAddEntityPacket(livingEntity, 0, pos);
    }
}
