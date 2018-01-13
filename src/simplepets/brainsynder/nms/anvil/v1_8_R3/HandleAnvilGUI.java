package simplepets.brainsynder.nms.anvil.v1_8_R3;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.nms.anvil.AnvilClickEvent;
import simplepets.brainsynder.nms.anvil.AnvilSlot;
import simplepets.brainsynder.nms.anvil.IAnvilClickEvent;
import simplepets.brainsynder.nms.anvil.IAnvilGUI;
import simplepets.brainsynder.utils.Valid;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class HandleAnvilGUI implements IAnvilGUI {
    private Player player;
    private HashMap<AnvilSlot, ItemStack> items = new HashMap();
    private Inventory inv;
    private HandleAnvilGUI.LIST listener;
    private int levels = 0;
    private float exp = 0.0F;
    private Plugin plugin;
    private IAnvilClickEvent handler;

    public HandleAnvilGUI(Plugin plugin, Player player, IAnvilClickEvent handler) {
        this.player = player;
        this.plugin = plugin;
        this.handler = handler;
        this.listener = new HandleAnvilGUI.LIST();
        Bukkit.getPluginManager().registerEvents(this.listener, plugin);
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setSlot(AnvilSlot slot, ItemStack item) {
        this.items.put(slot, item);
    }

    public void open() {
        this.levels = this.player.getLevel();
        this.exp = this.player.getExp();
        EntityPlayer p = ((CraftPlayer) this.player).getHandle();
        HandleAnvilGUI.AnvilContainer container = new HandleAnvilGUI.AnvilContainer(p);
        this.inv = container.getBukkitView().getTopInventory();
        Iterator var3 = this.items.keySet().iterator();

        while (var3.hasNext()) {
            AnvilSlot slot = (AnvilSlot) var3.next();
            this.inv.setItem(slot.getSlot(), this.items.get(slot));
        }

        int c = p.nextContainerCounter();
        p.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("Anvil"), 0));
        p.activeContainer = container;
        p.activeContainer.windowId = c;
        p.activeContainer.addSlotListener(p);
        this.player.setLevel(50);
    }

    public void destroy() {
        Valid.notNull(this.player, "Player is null");
        Valid.notNull(this.exp, "exp is null");
        Valid.notNull(this.levels, "levels is null");
        this.player.setExp(this.exp);
        this.player.setLevel(this.levels);
        this.items = null;
        HandlerList.unregisterAll(this.listener);
        this.listener = null;
    }

    public static class AnvilContainer extends ContainerAnvil {
        public AnvilContainer(EntityHuman entity) {
            super(entity.inventory, entity.world, new BlockPosition(0, 0, 0), entity);
        }

        public boolean a(EntityHuman entityhuman) {
            return true;
        }
    }

    private class LIST implements Listener {
        private LIST() {
        }

        @EventHandler(
                ignoreCancelled = true
        )
        public void onInventoryClick(InventoryClickEvent event) {
            if (event.getWhoClicked() instanceof Player && event.getInventory().equals(HandleAnvilGUI.this.inv)) {
                ItemStack item = event.getCurrentItem();
                int slot = event.getRawSlot();
                String name = "";
                if (item != null && item.hasItemMeta()) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta.hasDisplayName()) {
                        name = meta.getDisplayName();
                    }
                }

                if (HandleAnvilGUI.this.player != null && (HandleAnvilGUI.this.player.getGameMode() == GameMode.ADVENTURE || HandleAnvilGUI.this.player.getGameMode() == GameMode.SURVIVAL && HandleAnvilGUI.this.player.getLevel() > 0)) {
                    (new BukkitRunnable() {
                        public void run() {
                            if (HandleAnvilGUI.this.player != null) {
                                HandleAnvilGUI.this.player.setLevel(HandleAnvilGUI.this.player.getLevel());
                                HandleAnvilGUI.this.player.setExp(HandleAnvilGUI.this.player.getExp());
                            }
                        }
                    }).runTaskLater(HandleAnvilGUI.this.plugin, 2L);
                }

                AnvilClickEvent clickEvent = new AnvilClickEvent(event.getInventory(), AnvilSlot.bySlot(slot), name, event.getInventory().getItem(2));
                HandleAnvilGUI.this.handler.onAnvilClick(clickEvent);
                Bukkit.getServer().getPluginManager().callEvent(clickEvent);
                event.setCancelled(clickEvent.isCanceled());
                if (clickEvent.getWillClose()) {
                    event.getWhoClicked().closeInventory();
                }

                if (clickEvent.getWillDestroy()) {
                    HandleAnvilGUI.this.destroy();
                }
            }

        }

        @EventHandler(
                ignoreCancelled = true
        )
        public void onInventoryClose(InventoryCloseEvent event) {
            if (event.getPlayer() instanceof Player) {
                Inventory inv = event.getInventory();
                if (inv.equals(HandleAnvilGUI.this.inv)) {
                    inv.clear();
                    HandleAnvilGUI.this.destroy();
                }
            }

        }

        @EventHandler(
                ignoreCancelled = true
        )
        public void onPlayerQuit(PlayerQuitEvent event) {
            if (event.getPlayer().equals(HandleAnvilGUI.this.getPlayer())) {
                HandleAnvilGUI.this.destroy();
            }

        }
    }
}
