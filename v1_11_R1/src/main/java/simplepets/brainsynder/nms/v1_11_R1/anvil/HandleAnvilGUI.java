package simplepets.brainsynder.nms.v1_11_R1.anvil;

import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import lib.brainsynder.utils.Valid;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.nms.anvil.AnvilClickEvent;
import simplepets.brainsynder.nms.anvil.AnvilSlot;
import simplepets.brainsynder.nms.anvil.IAnvilClickEvent;
import simplepets.brainsynder.nms.anvil.IAnvilGUI;

import java.util.HashMap;
import java.util.Map;

public class HandleAnvilGUI implements IAnvilGUI {
    private Player player;
    private Map<AnvilSlot, ItemStack> items = new HashMap<AnvilSlot, ItemStack>();
    private Inventory inv;
    private HandleAnvilGUI.LIST listener;
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
        EntityPlayer p = ((CraftPlayer) this.player).getHandle();
        HandleAnvilGUI.AnvilContainer container = new HandleAnvilGUI.AnvilContainer(p);
        this.inv = container.getBukkitView().getTopInventory();

        for (AnvilSlot slot : this.items.keySet()) {
            this.inv.setItem(slot.getSlot(), this.items.get(slot));
        }

        int c = p.nextContainerCounter();
        p.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage(PetCore.get().getMessages().getString("Anvil-Rename.Name")), 0));
        p.activeContainer = container;
        p.activeContainer.windowId = c;
        p.activeContainer.addSlotListener(p);
    }

    private void destroy() {
        Valid.notNull(this.player, "Player is null");
        this.items = null;
        HandlerList.unregisterAll(this.listener);
        this.listener = null;
    }

    public static class AnvilContainer extends ContainerAnvil {
        AnvilContainer(EntityHuman entity) {
            super(entity.inventory, entity.world, new BlockPosition(0, 0, 0), entity);
        }

        public boolean a(EntityHuman entityhuman) {
            return true;
        }

        @Override
        public void e() {
            super.e();
            this.a = 0;
        }
    }

    private class LIST implements Listener {
        private LIST() {
        }

        @EventHandler(ignoreCancelled = true)
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

        @EventHandler(ignoreCancelled = true)
        public void onPlayerKick(PlayerKickEvent event) {
            if (event.getPlayer().equals(getPlayer())) {
                destroy();
            }
        }

        @EventHandler(ignoreCancelled = true)
        public void onPlayerDeath(PlayerDeathEvent event) {
            if (event.getEntity().equals(getPlayer())) {
                destroy();
            }
        }
    }
}
