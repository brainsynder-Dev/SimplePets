package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.json.WriterConfig;
import lib.brainsynder.utils.Base64Wrapper;
import lib.brainsynder.utils.Callback;
import lib.brainsynder.web.DiscordHook;
import lib.brainsynder.web.WebConnector;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.utils.Keys;

import java.util.List;

@ICommand(
        name = "report",
        usage = "<issue_title>",
        description = "Used to report an issue with the plugin without opening discord/github"
)
@Permission(permission = "issue_report", adminCommand = true)
public class ReportCommand extends PetSubCommand implements Listener {

    public ReportCommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You need to be a player to run this command.");
            sender.sendMessage(ChatColor.RED + "Or report the issue here:§r https://github.com/brainsynder-Dev/SimplePets/issues/new/choose");
            return;
        }

        if (args.length == 0) {
            sendUsage(sender);
            return;
        }

        String title = messageMaker(args, 0);

        ItemBuilder builder = new ItemBuilder(Material.WRITABLE_BOOK);
        builder.withName("Issue: "+title).addLore(
                "Open the book to report an issue",
                "Include all details to reproduce",
                "Include a way to get in contact if we need more info"
        ).getMetaValue(BookMeta.class, bookMeta -> {
            bookMeta.setTitle(title);
            bookMeta.setPages("Steps to reproduce:\n1:\n2:\n3:", "What actually happens:\n1:\n2:\n3:", "Additional Info:");
            bookMeta.setAuthor(sender.getName());
            bookMeta.getPersistentDataContainer().set(Keys.BOOK_KEY, PersistentDataType.STRING, sender.getName());
            return bookMeta;
        });

        ((Player)sender).getWorld().dropItem(((Player) sender).getLocation(), builder.build());
    }

    @EventHandler
    public void onDrop (PlayerDropItemEvent event) {
        ItemStack stack = event.getItemDrop().getItemStack();
        if (!stack.hasItemMeta()) return;
        if (stack.getType().name().contains("BOOK")) return;
        if (!stack.getItemMeta().getPersistentDataContainer().has(Keys.BOOK_KEY, PersistentDataType.STRING)) return;
        event.getItemDrop().remove();
    }

    @EventHandler
    public void handleBook (PlayerEditBookEvent event) {

        // No changes to the meta
        if (event.getNewBookMeta() == event.getPreviousBookMeta()) return;

        // The book is an issue book
        if (event.getNewBookMeta().getPersistentDataContainer().has(Keys.BOOK_KEY, PersistentDataType.STRING)) {
            if (!event.getPlayer().hasPermission(getPermission())) {
                event.setNewBookMeta(event.getPreviousBookMeta());
                event.getPlayer().sendMessage(MessageFile.getTranslation(MessageOption.NO_PERMISSION));
                return;
            }
            event.setCancelled(true);
            event.setSigning(false);

            List<String> pages = event.getNewBookMeta().getPages();

            StringBuilder builder = new StringBuilder();
            builder.append("Title: ").append(event.getPreviousBookMeta().getTitle()).append(" | ").append(event.getNewBookMeta().getTitle()).append("\n");

            pages.forEach(s -> builder.append(s).append("\n\n"));

            String newTitle = Base64Wrapper.encodeString(event.getNewBookMeta().getTitle());
            String newText = Base64Wrapper.encodeString(builder.toString());

            PlayerInventory inventory = event.getPlayer().getInventory();
            for (int slot = 1; slot <= inventory.getSize(); slot++) {
                ItemStack stack = inventory.getItem((slot-1));
                if (stack == null) continue;
                if (!stack.getType().name().contains("BOOK")) continue;
                if (!stack.hasItemMeta()) continue;
                ItemMeta meta = stack.getItemMeta();
                if (meta.getPersistentDataContainer().has(Keys.BOOK_KEY, PersistentDataType.STRING)
                        || (meta == event.getNewBookMeta())
                        || (meta == event.getPreviousBookMeta())) {
                    inventory.setItem((slot-1), new ItemStack(Material.AIR));
                }
            }

            DebugCommand.fetchDebug(object -> {
                builder.append("\n\n").append(object.toString(WriterConfig.PRETTY_PRINT));
                DiscordHook hook = new DiscordHook("https://discord.com/api/webhooks/802758724820533248/QgvJd0vYLOl5UHyLNDLboYjijLPhIaYTlRakazofNZAvduGpoL6XKAIRs7BI584W67GO");
                hook.setUsername(event.getPlayer().getName());
                hook.setAvatarUrl("https://minotar.net/cube/"+event.getPlayer().getUniqueId()+"/100.png");

                WebConnector.uploadPaste(getPlugin(), builder.toString(), new Callback<String, String>() {
                    @Override
                    public void success(String s) {
                        hook.setContent(s);
                        hook.send();
                    }

                    @Override
                    public void fail(String value) {
                        WebConnector.getInputStreamString("https://pluginwiki.us/pastebin/paste.php?title="+newTitle+"&text="+newText, getPlugin(), s -> {
                            if (s.startsWith("http")) {
                                hook.setContent(s);
                                hook.send();
                            }
                        });
                    }
                });
            }, false);
        }
    }
}
