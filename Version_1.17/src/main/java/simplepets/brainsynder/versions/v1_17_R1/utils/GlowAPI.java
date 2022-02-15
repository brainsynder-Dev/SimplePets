package simplepets.brainsynder.versions.v1_17_R1.utils;

import lib.brainsynder.utils.DyeColorWrapper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class is originally from https://www.spigotmc.org/threads/546391/
 * It has been extensively modified to fix the need of SimplePets
 */
public class GlowAPI {
    private static final Map<UUID, DyeColorWrapper> COLOR_MAP;
    private static final Scoreboard SCOREBOARD;

    static {
        COLOR_MAP = new HashMap<>();
        SCOREBOARD = new Scoreboard();
        for (DyeColorWrapper color : DyeColorWrapper.values()) {
            SCOREBOARD.addPlayerTeam(String.valueOf(color.getChatChar())).setColor(ChatFormatting.getByCode(color.getChatChar()));
        }
    }

    public static void setColor (Entity entity, Player player, DyeColorWrapper color) {
        COLOR_MAP.put(entity.getUniqueId(), color);
        try {
            PlayerTeam team = SCOREBOARD.getPlayersTeam(String.valueOf(color.getChatChar()));
            if (entity instanceof Player) {
                SCOREBOARD.addPlayerToTeam(entity.getName(), team);
            } else {
                SCOREBOARD.addPlayerToTeam(String.valueOf(entity.getUniqueId()), team);
            }
            ClientboundSetPlayerTeamPacket packet = ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true);
            ((CraftPlayer) player).getHandle().connection.send(packet);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public static void removeColor (Entity entity, Player player) {
        if (!COLOR_MAP.containsKey(entity.getUniqueId())) return;
        try {
            PlayerTeam team = SCOREBOARD.getPlayersTeam(String.valueOf(COLOR_MAP.get(entity.getUniqueId()).getChatChar()));
            ClientboundSetPlayerTeamPacket packet = ClientboundSetPlayerTeamPacket.createPlayerPacket(team, String.valueOf(entity.getUniqueId()), ClientboundSetPlayerTeamPacket.Action.REMOVE);
            ((CraftPlayer) player).getHandle().connection.send(packet);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }


    public static void setGlowing(Entity entity, Player player, boolean glow) {
        try {
            net.minecraft.world.entity.Entity gEntity = ((CraftEntity) entity).getHandle();
            SynchedEntityData toCloneDataWatcher = gEntity.getEntityData();
            SynchedEntityData newDataWatcher = new SynchedEntityData(gEntity);

            Int2ObjectMap<SynchedEntityData.DataItem<Byte>> newMap = (Int2ObjectMap<SynchedEntityData.DataItem<Byte>>) FieldUtils.readDeclaredField(toCloneDataWatcher, "f", true);

            SynchedEntityData.DataItem<Byte> item = newMap.get(0);
            byte initialBitMask = item.getValue();
            byte bitMaskIndex = (byte) 6;
            if (glow) {
                item.setValue((byte) (initialBitMask | 1 << bitMaskIndex));
            } else {
                item.setValue((byte) (initialBitMask & ~(1 << bitMaskIndex)));
            }
            FieldUtils.writeDeclaredField(newDataWatcher, "f", newMap, true);
            ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(entity.getEntityId(), newDataWatcher, true);
            ((CraftPlayer) player).getHandle().connection.send(packet);
        } catch (Exception ignored) {
            // I Ignore this in case the server uses ArcLight as that had issues in the past with glowing pets...
        }
    }
}