package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.json.Json;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.web.WebConnector;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.addon.AddonCloudData;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.managers.AddonManager;
import simplepets.brainsynder.menu.inventory.AddonMenu;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@ICommand(
        name = "addon",
        usage = "[install|reload|update] [addon]",
        description = "Opens a GUI to download/toggle addons for the plugin"
)
@Permission(permission = "addon", adminCommand = true, additionalPermissions = {"install", "reload", "update"})
public class AddonCommand extends PetSubCommand {

    public AddonCommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public List<String> handleCompletions(List<String> completions, CommandSender sender, int index, String[] args) {
        if (!canExecute(sender)) return super.handleCompletions(completions, sender, index, args);
        if ((index == 1)) {
            if (sender.hasPermission(getPermission("reload"))) completions.add("reload");
            if (sender.hasPermission(getPermission("update"))) completions.add("update");
            if (sender.hasPermission(getPermission("install"))) completions.add("install");
        }

        if (index == 2) {
            if (args[0].equalsIgnoreCase("update")) {
                PetCore.getInstance().getAddonManager().getLocalDataMap().keySet().forEach(localData ->  {
                    completions.add(localData.getName());
                });
            }
            if (args[0].equalsIgnoreCase("install")) {
                PetCore.getInstance().getAddonManager().getCloudAddons().forEach(cloudData ->  {
                    completions.add(cloudData.getName());
                });
            }
        }
        return super.handleCompletions(completions, sender, index, args);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("update") && sender.hasPermission(getPermission("update"))) {
                AddonManager manager = PetCore.getInstance().getAddonManager();

                if (args.length == 1) {
                    sendUsage(sender);
                    return;
                }
                String target = args[1];
                manager.fetchAddon(target).ifPresent(localData -> {
                    String name = localData.getName();

                    WebConnector.getInputStreamString("https://bsdevelopment.org/addons/addons.json", getPlugin(), result -> {
                        JsonObject json = (JsonObject) Json.parse(result);
                        if (!json.names().contains(name)) {
                            sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+" §c"+name+" is not in the addon database: https://pluginwiki.us/addons/");
                            return;
                        }

                        String url = ((JsonObject)json.get(name)).getString("url", null);
                        if (url == null) {
                            sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+" §c"+name+" seems to be missing the download URL (Contact brainsynder)");
                            return;
                        }

                        manager.update(localData, url, () -> {
                            sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+" §7"+name+" has been successfully updated!");
                        });
                    });
                });

                return;
            }
            if (args[0].equalsIgnoreCase("install") && sender.hasPermission(getPermission("install"))) {
                AddonManager manager = PetCore.getInstance().getAddonManager();

                if (args.length == 1) {
                    sendUsage(sender);
                    return;
                }
                String target = args[1];
                Optional<AddonCloudData> cloudOptional = manager.fetchCloudData(target);
                if (cloudOptional.isEmpty()) {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+" §c"+target+" is not a valid addon in our database.");
                    return;
                }
                AddonCloudData cloudData = cloudOptional.get();

                if (manager.fetchAddon(cloudData.getName()).isPresent()) {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+" §c"+target+" is already installed, Looking to update it try: §7/pet addon update "+target);
                    return;
                }

                sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+" §7Attempting to install: '"+target+"'");
                manager.downloadViaName(cloudData.getName(), cloudData.getDownloadURL(), () -> {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+" §7"+target+" has been successfully installed!");
                });
                return;
            }
            if (args[0].equalsIgnoreCase("reload") && sender.hasPermission(getPermission("reload"))) {
                AddonManager manager = PetCore.getInstance().getAddonManager();
                manager.cleanup();
                File folder = manager.getFolder();
                getPlugin().getScheduler().getImpl().runLater(() -> {
                    if (!folder.exists()) return;
                    for (File file : Objects.requireNonNull(folder.listFiles())) {
                        manager.loadAddon(file);
                    }
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+ ChatColor.GRAY+"All Addons have been reloaded");
                }, 50L, TimeUnit.MILLISECONDS);
                return;
            }
        }

        if (!(sender instanceof Player)) {
            sendUsage(sender);
            return;
        }

        SimplePets.getUserManager().getPetUser((Player) sender).ifPresent(user -> {
            SimplePets.getGUIHandler().getInventory(AddonMenu.class).ifPresent(selectionMenu -> selectionMenu.open(user));
        });
    }
}
