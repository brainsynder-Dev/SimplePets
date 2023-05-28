package simplepets.brainsynder.nms.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.MinecraftKey;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.bukkit.entity.Player;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class provided by: https://github.com/dev-hydrogen/debugmarker/
 *
 * Mostly used by me for testing pathfinding changes
 */
public class DebugMarker {
    private static final PacketContainer STOP_ALL_MARKERS = new PacketContainer(PacketType.Play.Server.CUSTOM_PAYLOAD);
    private FriendlyByteBuf data;
    private final PacketContainer marker;
    private org.bukkit.Location location;
    private Color color;
    private String name;
    private int duration;
    private int distanceSquared;
    private final List<Player> seen;
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public DebugMarker(org.bukkit.Location location, Color color, String name, int duration, List<Player> showTo) throws InvocationTargetException {
        this(location, color, name, duration);
        for (Player player : showTo) {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, marker);
        }
    }
    public DebugMarker(org.bukkit.Location location, Color color, String name, int duration) {
        this.location = location;
        this.color = color;
        this.name = name;
        this.duration = duration;
        seen = new ArrayList<>();
        marker = new PacketContainer(PacketType.Play.Server.CUSTOM_PAYLOAD);
        data = new FriendlyByteBuf(Unpooled.buffer());
        data.writeBlockPos(new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ())); // location
        data.writeInt(color.getRGB()); // color
        data.writeUtf(name); // name
        data.writeInt(duration); // lifetime of marker

        marker.getMinecraftKeys().write(0, new MinecraftKey("debug/game_test_add_marker"));
        marker.getSpecificModifier(FriendlyByteBuf.class).write(0, data);
    }
    public void start(int distance) {
        start(distance, () -> {
        }); // do-nothing runnable
    }
    public void start(int distance, Runnable callback) {
        if (!executorService.isShutdown()) {
            stop();
        }
        distanceSquared = distance < 0 ? -1 : distance * distance;
        long endTime = System.currentTimeMillis() + duration;
        executorService = Executors.newScheduledThreadPool(1);
        // probably not the most efficient way of doing this
        Runnable run = () -> {
            if (System.currentTimeMillis() > endTime) {
                seen.clear();
                callback.run();
                executorService.shutdown();
                return;
            }
            for (Player p : location.getWorld().getPlayers()) {
                if (isCloseEnough(p.getLocation()) && !seen.contains(p)) {
                    setData(location, color, name, (int) (endTime - System.currentTimeMillis())); // make sure death time is the same for all players
                    ProtocolLibrary.getProtocolManager().sendServerPacket(p, marker);
                    seen.add(p);
                } else if (!isCloseEnough(p.getLocation()) && seen.contains(p)) {
                    setData(location, new Color(0, 0, 0, 0), "", 0);
                    ProtocolLibrary.getProtocolManager().sendServerPacket(p, marker);
                    seen.remove(p);
                }
            }
        };
        executorService.scheduleAtFixedRate(run, 0, 200, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        setData(location, new Color(0, 0, 0, 0), "", 0);
        for (Player p : location.getWorld().getPlayers()) {
            if (distanceSquared == -1 || this.location.distanceSquared(p.getLocation()) <= distanceSquared) {
                ProtocolLibrary.getProtocolManager().sendServerPacket(p, marker);
            }
        }
        seen.clear();
        executorService.shutdownNow();
    }

    public void stopAll(int distance) throws InvocationTargetException {
        int distanceSquared = distance < 0 ? -1 : distance * distance;
        // probably not the most efficient way of doing this
        for (Player p : location.getWorld().getPlayers()) {
            if (distanceSquared == -1 || this.location.distanceSquared(p.getLocation()) <= distanceSquared) {
                ProtocolLibrary.getProtocolManager().sendServerPacket(p, STOP_ALL_MARKERS);
            }
        }
    }
    public static void stopAll(List<Player> stopTo) throws InvocationTargetException {
        for (Player player : stopTo) {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, STOP_ALL_MARKERS);
        }
    }
    public static void stopAll(org.bukkit.Location location, int distance) throws InvocationTargetException {
        int distanceSquared = distance < 0 ? -1 : distance * distance;
        // probably not the most efficient way of doing this
        for (Player p : location.getWorld().getPlayers()) {
            if (distanceSquared == -1 || location.distanceSquared(p.getLocation()) <= distanceSquared) {
                ProtocolLibrary.getProtocolManager().sendServerPacket(p, STOP_ALL_MARKERS);
            }
        }
    }

    private void setData(org.bukkit.Location location, Color color, String name, int duration) {
        data = new FriendlyByteBuf(Unpooled.buffer());
        data.writeBlockPos(new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        data.writeInt(color.getRGB());
        data.writeUtf(name);
        data.writeInt(duration);
        marker.getSpecificModifier(FriendlyByteBuf.class).write(0, data);
    }

    public void setLocation(org.bukkit.Location location) {
        this.location = location;
        setData(this.location, this.color, this.name, this.duration);
    }
    public void setColor(Color color) {
        this.color = color;
        setData(this.location, this.color, this.name, this.duration);
    }
    public void setName(String name) {
        this.name = name;
        setData(this.location, this.color, this.name, this.duration);
    }
    public void setDuration(int duration) {
        this.duration = duration;
        setData(this.location, this.color, this.name, this.duration);
    }

    public org.bukkit.Location getLocation() {
        return location;
    }
    public Color getColor() {
        return color;
    }
    public String getName() {
        return name;
    }
    public int getDuration() {
        return duration;
    }

    private boolean isCloseEnough(org.bukkit.Location location) {
        return distanceSquared == -1 ||
                this.location.distanceSquared(location) <= distanceSquared;
    }
    static {
        STOP_ALL_MARKERS.getMinecraftKeys().write(0, new MinecraftKey("debug/game_test_clear"));
        STOP_ALL_MARKERS.getSpecificModifier(FriendlyByteBuf.class).write(0, new FriendlyByteBuf(Unpooled.buffer()));
    }
}
