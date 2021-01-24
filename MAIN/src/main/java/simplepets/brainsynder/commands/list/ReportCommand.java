package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.utils.Base64Wrapper;
import lib.brainsynder.web.WebConnector;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.utils.DiscordHook;
import simplepets.brainsynder.utils.Keys;

import java.util.List;

@ICommand(
        name = "report",
        usage = "<issue_title>",
        description = "Used to report an issue with the plugin without opening discord/github"
)
public class ReportCommand extends PetSubCommand implements Listener {

    public ReportCommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You need to be a player to run this command.");
            sender.sendMessage(ChatColor.RED + "Or report the issue here:§r https://github.com/brainsynder-Dev/SimplePets/issues/new");
            return;
        }

        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED+"You must be an operator to edit this book");
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
    public void handleBook (PlayerEditBookEvent event) {

        // No changes to the meta
        if (event.getNewBookMeta() == event.getPreviousBookMeta()) return;

        // The book is an issue book
        if (event.getNewBookMeta().getPersistentDataContainer().has(Keys.BOOK_KEY, PersistentDataType.STRING)) {
            if (!event.getPlayer().isOp()) {
                event.setNewBookMeta(event.getPreviousBookMeta());
                event.getPlayer().sendMessage(ChatColor.RED+"You must be an operator to edit this book");
                return;
            }

            List<String> pages = event.getNewBookMeta().getPages();

            StringBuilder builder = new StringBuilder();
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
                if (meta.getPersistentDataContainer().has(Keys.BOOK_KEY, PersistentDataType.STRING)) {
                    inventory.setItem((slot-1), new ItemStack(Material.AIR));
                }
            }

            WebConnector.getInputStreamString("https://pluginwiki.us/pastebin/paste.php?title="+newTitle+"&text="+newText, getPlugin(), s -> {
                event.getPlayer().sendMessage("URL: "+s);
                if (s.startsWith("http")) {
                    DiscordHook hook = new DiscordHook(getPlugin(), "https://discord.com/api/webhooks/802758724820533248/QgvJd0vYLOl5UHyLNDLboYjijLPhIaYTlRakazofNZAvduGpoL6XKAIRs7BI584W67GO");
                    hook.setUsername(event.getPlayer().getName());
                    hook.setAvatarUrl("https://minotar.net/cube/"+event.getPlayer().getUniqueId()+"/100.png");
                    hook.setContent(s);
                    hook.send();
                }
            });
        }
    }
}
