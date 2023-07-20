package simplepets.brainsynder.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import lib.brainsynder.utils.Colorize;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.PetCore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;

/**
 * This updated class is from this post on spigot:
 * https://www.spigotmc.org/threads/signmenu-1-16-5-get-player-sign-input.249381/page-6#post-4331569
 */
public final class SignMenuFactory {

    private final Plugin plugin;

    private final Map<Player, Menu> inputReceivers;

    public SignMenuFactory(Plugin plugin) {
        this.plugin = plugin;
        this.inputReceivers = new HashMap<>();
        this.listen();
    }

    public Menu newMenu(List<String> text) {
        Objects.requireNonNull(text, "text");
        return new Menu(text);
    }

    private void listen() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.plugin, PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();

                Menu menu = inputReceivers.remove(player);

                if (menu == null) {
                    return;
                }
                event.setCancelled(true);

                boolean success = menu.response.test(player, event.getPacket().getStringArrays().read(0));

                if (!success && menu.reopenIfFail && !menu.forceClose) {
                    PetCore.getInstance().getScheduler().getImpl().runAtEntityLater(player, () -> menu.open(player), 100L, TimeUnit.MILLISECONDS);
                }
                PetCore.getInstance().getScheduler().getImpl().runAtEntityLater(player, () -> {
                    if (player.isOnline()) {
                        player.sendBlockChange(menu.location, menu.location.getBlock().getBlockData());
                    }
                }, 100L, TimeUnit.MILLISECONDS);
            }
        });
    }

    public final class Menu {

        private final List<String> text;

        private BiPredicate<Player, String[]> response;
        private boolean reopenIfFail;

        private Location location;

        private boolean forceClose;

        Menu(List<String> text) {
            this.text = text;
        }

        public Menu reopenIfFail() {
            this.reopenIfFail = true;
            return this;
        }

        public Menu response(BiPredicate<Player, String[]> response) {
            this.response = response;
            return this;
        }

        public void open(Player player) {
            Objects.requireNonNull(player, "player");
            if (!player.isOnline()) {
                return;
            }
            location = player.getLocation();
            location.setY(location.getBlockY() - 4);

            player.sendBlockChange(location, Material.OAK_SIGN.createBlockData());
            player.sendSignChange(
                    location,
                    text.stream().map(this::color).toList().toArray(new String[4])
            );

            PacketContainer openSign = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
            BlockPosition position = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            openSign.getBlockPositionModifier().write(0, position);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, openSign);
            inputReceivers.put(player, this);
        }

        /**
         * closes the menu. if force is true, the menu will close and will ignore the reopen
         * functionality. false by default.
         *
         * @param player the player
         * @param force  decides whether it will reopen if reopen is enabled
         */
        public void close(Player player, boolean force) {
            this.forceClose = force;
            if (player.isOnline()) {
                player.closeInventory();
            }
        }

        public void close(Player player) {
            close(player, false);
        }

        private String color(String input) {
            return Colorize.translateBungeeHex(input);
        }
    }
}