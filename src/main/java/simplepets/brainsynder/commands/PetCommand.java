package simplepets.brainsynder.commands;

import org.bukkit.command.CommandSender;
import simplepets.brainsynder.commands.annotations.CommandName;
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
}
