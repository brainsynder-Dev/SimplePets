package simplepets.brainsynder.commands;

import lib.brainsynder.commands.SubCommand;
import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.nms.Tellraw;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PetSubCommand extends SubCommand {
    private final PetCore plugin;

    public PetSubCommand(PetCore plugin) {
        this.plugin = plugin;
    }

    public PetCore getPlugin() {
        return plugin;
    }

    @Override
    public boolean canExecute(CommandSender sender) {
        if (!(sender instanceof Player)) return true;
        if (sender.isOp()) return true;
        if (needsPermission()) {
            //sender.sendMessage(MessageFile.getTranslation(MessageOption.NO_PERMISSION));
            return sender.hasPermission(getPermission());
        }
        return super.canExecute(sender);
    }


    @Override
    public List<String> handleCompletions(List<String> completions, CommandSender sender, int index, String[] args) {
        ICommand command = getCommand(getClass());
        if (command == null) return completions;
        String usage = command.usage();
        if ((usage == null) || usage.isEmpty()) return completions;

        String[] split = usage.split(" ");
        int current = 1;
        for (String value : split) {
            if (current == index) {
                if (value.contains("nbt") && sender.hasPermission(getPermission("nbt"))) completions.add("{}");
                if (value.contains("type")) completions.addAll(getPetTypes(sender));
                if (value.contains("player") && sender.hasPermission(getPermission("other"))) completions.addAll(getOnlinePlayers());
                break;
            }
            current++;
        }
        return completions;
    }

    public boolean needsPermission() {
        return getClass().isAnnotationPresent(Permission.class);
    }

    public String getPermission() {
        if (getClass().isAnnotationPresent(Permission.class)) {
            return "pet.commands." + getClass().getAnnotation(Permission.class).permission();
        }
        return "";
    }

    public String getPermission(String addition) {
        if (getClass().isAnnotationPresent(Permission.class)) {
            return "pet.commands." + getClass().getAnnotation(Permission.class).permission() + "." + addition;
        }
        return "";
    }

    public List<String> getOnlinePlayers() {
        List<String> list = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(o -> list.add(o.getName()));
        return list;
    }

    public List<String> getPetTypes() {
        List<String> list = new ArrayList<>();
        for (PetType type : PetType.values()) {
            SimplePets.getPetConfigManager().getPetConfig(type).ifPresent(config -> {
                if (!config.isEnabled()) return;
                if (!type.isSupported()) return;
                if (!SimplePets.getSpawnUtil().isRegistered(type)) return;
                list.add(type.getName());
            });
        }
        return list;
    }

    public List<String> getPetTypes(CommandSender sender) {
        List<String> list = new ArrayList<>();
        for (PetType type : PetType.values()) {
            SimplePets.getPetConfigManager().getPetConfig(type).ifPresent(config -> {
                if (!config.isEnabled()) return;
                if (!type.isSupported()) return;
                if (!SimplePets.getSpawnUtil().isRegistered(type)) return;

                if (!sender.hasPermission(type.getPermission())) return;
                list.add(type.getName());
            });
        }
        return list;
    }

    public String formatJson(String json) {
        // This should allow for spaces to be used in the NBT data
        Pattern pattern = Pattern.compile("\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(json);
        while (matcher.find()) {
            String string = matcher.group(1);
            if (string.contains(" ")) {
                json = json.replace(string, string.replace(" ", "○!◙"));
            }
        }
        json = json.replace(" ", ""); // removes all random spaces
        return json.replace("○!◙", " "); // replaces placholder with spaces for string data
    }


    @Override
    public void sendUsage(CommandSender sender) {
        ICommand command = this.getCommand(this.getClass());
        String usage = command.usage().trim();
        String description = command.description();


        //&r &r #e1eb5b[] #d1c9c9/pet modify #b35349<#de9790player#b35349> <#de9790type#b35349> #e3aa4f[#e3c79ajson#e3aa4f]
        Tellraw raw = Tellraw.getInstance("§r §r ");
        raw.then("[] ").color(hex2Rgb("#e1eb5b"));
        raw.then("/pet ").color(hex2Rgb("#d1c9c9"));
        raw.then(command.name()).color(hex2Rgb("#d1c9c9"));

        // handles adding alias to the tooltip
        List<String> tooltip = new ArrayList<>();
        if (!description.isEmpty())
            tooltip.add(ChatColor.GRAY + description);

        if (command.alias().length != 0) {
            if (!command.alias()[0].isEmpty()) {
                tooltip.add("&r");
                tooltip.add("&8Alias:");
                for (String alias : command.alias())
                    tooltip.add(ChatColor.GRAY + " - " + alias);

            }
        }
        if (!tooltip.isEmpty()) raw.tooltip(tooltip);

        StringBuilder builder = new StringBuilder();
        if (!usage.isEmpty()) {
            for (char c : usage.replace(" ", "").toCharArray()) {

                if ((c == '[') || (c == '<')) raw.then(" ");

                if ((c == '<') || ((c == '>'))) {
                    if (c == '>') {
                        raw.then(builder.toString()).color(hex2Rgb("#de9790")).tooltip(ChatColor.RED + "REQUIRED");
                        builder = new StringBuilder();
                    }

                    raw.then(c).color(hex2Rgb("#b35349"));
                } else if ((c == '[') || (c == ']')) {
                    if (c == ']') {
                        raw.then(builder.toString()).color(hex2Rgb("#e3c79a")).tooltip(ChatColor.YELLOW + "OPTIONAL");
                        builder = new StringBuilder();
                    }
                    raw.then(c).color(hex2Rgb("#e3aa4f"));
                } else {
                    builder.append(c);
                }
            }
        }

        raw.send(sender);
    }


    private org.bukkit.Color hex2Rgb(String hex) {
        if (hex.startsWith("#") && hex.length() == 7) {
            int rgb;
            try {
                rgb = Integer.parseInt(hex.substring(1), 16);
            } catch (NumberFormatException var4) {
                throw new IllegalArgumentException("Illegal hex string " + hex);
            }

            return org.bukkit.Color.fromRGB(rgb);
        } else {
            return org.bukkit.Color.RED;
        }
    }

    public boolean isUsername (String string) {
        return (string.length() < 17) && (string.length() > 2) && string.replace("_", "").matches("[A-Za-z0-9]+");
    }
}