package simplepets.brainsynder.nms.utils;

import lib.brainsynder.utils.AdvString;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.nms.VersionTranslator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class is originally from https://www.spigotmc.org/threads/546391/
 * It has been extensively modified to fix the need of SimplePets
 */
public class GlowAPI {
    private static Map<ChatColor, String> COLOR_KEY;
    private static Map<UUID, ChatColor> COLOR_MAP;
    private static Scoreboard SCOREBOARD;


    public static void init () {
        COLOR_KEY = new HashMap<>();
        COLOR_MAP = new HashMap<>();
        SCOREBOARD = new Scoreboard();
        try {
            for (ChatColor color : ChatColor.values()) {
                if (!color.isColor()) continue;
                ChatFormatting formatting = ChatFormatting.getByName(color.name());
                if (formatting == null) formatting = ChatFormatting.getByCode(color.toString().toCharArray()[1]);
                try {
                    if (formatting == null) formatting = ChatFormatting.valueOf(color.name());
                } catch (Exception ignored) {
                }


                String name = color.name() + AdvString.scramble(AdvString.scramble("123"));
                COLOR_KEY.put(color, name);
                SCOREBOARD.addPlayerTeam(name).setColor(formatting);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setRawColor (Entity entity, ChatColor color) {
        COLOR_MAP.put(entity.getUniqueId(), color);
    }

    public static void setColor (Entity entity, Player player, ChatColor color) {
        try {
            PlayerTeam team = SCOREBOARD.getPlayerTeam(COLOR_KEY.get(color));
            if (entity instanceof Player) {
                SCOREBOARD.addPlayerToTeam(entity.getName(), team);
            } else {
                SCOREBOARD.addPlayerToTeam(String.valueOf(entity.getUniqueId()), team);
            }
            ClientboundSetPlayerTeamPacket packet = ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true);
            VersionTranslator.<ServerPlayer>getEntityHandle(player).connection.send(packet);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public static void removeColor (Entity entity, Player player) {
        if (!COLOR_MAP.containsKey(entity.getUniqueId())) return;
        try {
            PlayerTeam team = SCOREBOARD.getPlayerTeam(COLOR_KEY.get(COLOR_MAP.get(entity.getUniqueId())));
            ClientboundSetPlayerTeamPacket packet = ClientboundSetPlayerTeamPacket.createPlayerPacket(team, String.valueOf(entity.getUniqueId()), ClientboundSetPlayerTeamPacket.Action.REMOVE);
            VersionTranslator.<ServerPlayer>getEntityHandle(player).connection.send(packet);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }


    public static void setGlowing(Entity entity, Player player, boolean glow) {
        if (!ConfigOption.INSTANCE.PET_TOGGLES_GLOW_VANISH.getValue()) return;
//        Disabled till we can fix the issue with Scoreboard Teams & Bungee
//        if (glow) {
//            setColor(entity, player, COLOR_MAP.getOrDefault(entity.getUniqueId(), ChatColor.WHITE));
//        }else{
//            removeColor(entity, player);
//        }

        try {
            net.minecraft.world.entity.Entity gEntity = VersionTranslator.getEntityHandle(entity);
            SynchedEntityData toCloneDataWatcher = gEntity.getEntityData();
            SynchedEntityData newDataWatcher = new SynchedEntityData(gEntity);

            Int2ObjectMap<SynchedEntityData.DataItem<Byte>> newMap = (Int2ObjectMap<SynchedEntityData.DataItem<Byte>>) FieldUtils.readDeclaredField(toCloneDataWatcher, VersionTranslator.ENTITY_DATA_MAP, true);

            SynchedEntityData.DataItem<Byte> item = newMap.get(0);
            byte initialBitMask = item.getValue();
            byte bitMaskIndex = (byte) 6;
            if (glow) {
                item.setValue((byte) (initialBitMask | 1 << bitMaskIndex));
            } else {
                item.setValue((byte) (initialBitMask & ~(1 << bitMaskIndex)));
            }
            FieldUtils.writeDeclaredField(newDataWatcher, VersionTranslator.ENTITY_DATA_MAP, newMap, true);
            ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(entity.getEntityId(), newDataWatcher, true);
            VersionTranslator.<ServerPlayer>getEntityHandle(player).connection.send(packet);
        } catch (Exception ignored) {
            // I Ignore this in case the server uses ArcLight as that had issues in the past with glowing pets...
        }
    }
}