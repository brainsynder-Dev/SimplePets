package simplepets.brainsynder.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandPermission;
import simplepets.brainsynder.commands.annotations.CommandUsage;
import simplepets.brainsynder.commands.annotations.Console;

public abstract class PetCommand<T extends CommandSender> {
    public void onCommand(T sender, String[] args) {}

    protected void sendUsage(T sender) {
        String name = "", usage = "";
        if (getClass().isAnnotationPresent(CommandName.class)) {
            name = getClass().getAnnotation(CommandName.class).name();
        }
        if (getClass().isAnnotationPresent(CommandUsage.class)) {
            usage = getClass().getAnnotation(CommandUsage.class).usage();
        }
        if (getClass().isAnnotationPresent(Console.class)) {
            sender.sendMessage("- pet " + name + ' ' + usage);
        } else {
            sender.sendMessage("§eSimplePets §6>> §7/pet " + name + ' ' + usage);
        }
    }

    protected String buildMultiArg(String[] args, int start) {
        StringBuilder msgBuilder = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            msgBuilder.append(args[i]).append(' ');
        }
        return msgBuilder.toString().trim();
    }

    public String getName () {
        String name = "";
        if (getClass().isAnnotationPresent(CommandName.class)) {
            name = getClass().getAnnotation(CommandName.class).name();
        }
        return name;
    }

    public boolean hasPermission (Player player) {
        if (getClass().isAnnotationPresent(CommandPermission.class)){
            return player.hasPermission("Pet.commands." + getClass().getAnnotation(CommandPermission.class).permission());
        }
        return true;
    }

    public String getPermission () {
        if (getClass().isAnnotationPresent(CommandPermission.class)){
            return "Pet.commands." + getClass().getAnnotation(CommandPermission.class).permission();
        }
        return null;
    }

    public boolean needsPermission () {
        return getClass().isAnnotationPresent(CommandPermission.class);
    }
}
