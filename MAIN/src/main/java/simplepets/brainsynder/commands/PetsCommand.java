package simplepets.brainsynder.commands;

import lib.brainsynder.commands.ParentCommand;
import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.list.*;

@ICommand(
        name = "pet",
        alias = {"pets", "simplepets", "simplepet"},
        description = "Main SimplePets Command/Opens The Pet Selection GUI"
)
public class PetsCommand extends ParentCommand<PetSubCommand> {
    private final PetCore plugin;

    public PetsCommand(PetCore plugin) {
        this.plugin = plugin;
        registerSub(new SummonCommand(plugin));
        registerSub(new ModifyCommand(plugin));
        registerSub(new RemoveCommand(plugin));
        registerSub(new ListCommand(plugin));
        registerSub(new GUICommand(plugin));
        registerSub(new RenameCommand(plugin));
        registerSub(new PurchasedCommand (plugin));

        ReportCommand reportCommand = new ReportCommand(plugin);
        plugin.getServer().getPluginManager().registerEvents(reportCommand, plugin);
        registerSub(reportCommand);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        getSubCommands().forEach(petSubCommand -> petSubCommand.sendUsage(sender));
    }
}
