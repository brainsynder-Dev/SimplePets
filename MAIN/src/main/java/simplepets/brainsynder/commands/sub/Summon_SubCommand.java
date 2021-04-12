package simplepets.brainsynder.commands.sub;

import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.event.inventory.PetCommandSummonEvent;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.pet.TypeManager;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.files.Config;
import simplepets.brainsynder.utils.AdditionalData;

import java.util.List;

@AdditionalData(otherPermissions = "Pet.commands.summon.other")
@ICommand(
        name = "summon",
        usage = "&r &r &6[] &7/pet summon <pet> [player]",
        alias = {"spawn"},
        description = "Spawns a pet for the player or selected player",
        style = "{usage}"
)
public class Summon_SubCommand extends PetSubCommand {
    public Summon_SubCommand() {
        registerCompletion(1, getPetTypes());
        registerCompletion(2, (commandSender, list, s) -> {
            list.addAll(getOnlinePlayers());
            return commandSender.hasPermission("Pet.commands.summon.other");
        });
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return;
        }
        TypeManager manager = PetCore.get().getTypeManager();
        PetType type = manager.getType(args[0]);
        if (type == null) {
            sender.sendMessage(PetCore.get().getMessages().getString("Invalid-PetType", true));
            return;
        }
        if (!type.isSupported()) {
            sender.sendMessage(PetCore.get().getMessages().getString("Type-Not-Supported", true));
            return;
        }
        if (!type.isEnabled()) {
            sender.sendMessage(PetCore.get().getMessages().getString("Type-Not-Enabled", true));
            return;
        }

        if (args.length == 1) {
            if (sender instanceof Player) {
                Player p = (Player)sender;
                if (!type.hasPermission(p)) {
                    p.sendMessage(PetCore.get().getMessages().getString("No-Permission", true));
                    return;
                }
                if (PetCore.get().getConfiguration().getBoolean(Config.ECONOMY_TOGGLE) && !p.hasPermission("Pet.economy.bypass")) {
                    PetOwner petOwner = PetOwner.getPetOwner(p);
                    List<PetType> petArray = petOwner.getOwnedPets();
                    if (!petArray.contains(type)) {
                        sender.sendMessage(PetCore.get().getMessages().getString("Pet-Not-Purchased", true));
                        return;
                    }
                }
                PetCommandSummonEvent event = new PetCommandSummonEvent(type, p);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) return;
                p.sendMessage(PetCore.get().getMessages().getString("Select-Pet", true).replace("%pet%", type.getDisplayName()));
                type.setPet(p);
            }else{
                sendUsage(sender);
            }
            return;
        }

        if (args[1].equalsIgnoreCase(sender.getName())) {
            if (sender instanceof Player) {
                Player p = (Player)sender;
                if (!type.hasPermission(p)) {
                    p.sendMessage(PetCore.get().getMessages().getString("No-Permission", true));
                    return;
                }
                if (PetCore.get().getConfiguration().getBoolean(Config.ECONOMY_TOGGLE) && !p.hasPermission("Pet.economy.bypass")) {
                    PetOwner petOwner = PetOwner.getPetOwner(p);
                    List<PetType> petArray = petOwner.getOwnedPets();
                    if (!petArray.contains(type)) {
                        sender.sendMessage(PetCore.get().getMessages().getString("Pet-Not-Purchased", true));
                        return;
                    }
                }
                PetCommandSummonEvent event = new PetCommandSummonEvent(type, p);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) return;
                p.sendMessage(PetCore.get().getMessages().getString("Select-Pet", true).replace("%pet%", type.getDisplayName()));
                type.setPet(p);
            }else{
                sendUsage(sender);
            }
            return;
        }

        if (!sender.hasPermission("Pet.commands.summon.other")) {
            sender.sendMessage(PetCore.get().getMessages().getString("No-Permission", true));
            return;
        }

        Player tp = Bukkit.getPlayer(args[1]);
        if (tp == null) {
            sender.sendMessage(PetCore.get().getMessages().getString("Player-Not-Found", true).replace("%player%", args[1]));
            return;
        }

        if (!type.hasPermission(tp)) {
            sender.sendMessage(PetCore.get().getMessages().getString("Other-No-Pet-Permission", true)
                    .replace("%player%", tp.getName()));
            return;
        }

        if (sender instanceof Player) {
            PetCommandSummonEvent event = new PetCommandSummonEvent(type, (Player) sender); // Change to tp if needed, reason why is in case of the sender wanting to buy the pet for the player, or in case of abuse.
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
        }

        sender.sendMessage(PetCore.get().getMessages().getString("Select-Pet-Sender", true)
                .replace("%pet%", type.getDisplayName())
                .replace("%player%", tp.getName()));
        tp.sendMessage(PetCore.get().getMessages().getString("Select-Pet-Other", true)
                .replace("%pet%", type.getDisplayName())
                .replace("%player%", sender.getName()));
        type.setPet(tp);

    }
}
