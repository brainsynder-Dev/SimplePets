package simplepets.brainsynder.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.annotations.*;
import simplepets.brainsynder.commands.list.Console.*;
import simplepets.brainsynder.commands.list.Player.*;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.player.PetOwner;

import java.util.ArrayList;
import java.util.Collections;

public class SPCommand implements CommandExecutor {
    public ArrayList<PetCommand> commands;
    private PetCore plugin;

    public SPCommand(PetCore core) {
        plugin = core;
        commands = new ArrayList<>();
        commands.add(new Console_Help());
        commands.add(new Console_Remove());
        commands.add(new Console_Summon());
        commands.add(new Console_List());
        commands.add(new Console_Modify());
        commands.add(new Console_Info());

        commands.add(new CMD_AdminHelp());
        commands.add(new CMD_Menu());
        commands.add(new CMD_Help());
        commands.add(new CMD_Remove());
        commands.add(new CMD_Summon());
        commands.add(new CMD_List());
        commands.add(new CMD_Hat());
        commands.add(new CMD_Ride());
        commands.add(new CMD_Reload());
        commands.add(new CMD_Name());
        commands.add(new CMD_Modify());
        commands.add(new CMD_Info());
        if (core.getConfiguration().getBoolean("PetItemStorage.Enable")) commands.add(new CMD_Inv());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("pet")) {
            if (!(commandSender instanceof Player)) {
                if (args.length == 0) {
                    for (PetCommand gcmd : commands) {
                        String name = "", description = "", usage = "";
                        if (gcmd.getClass().isAnnotationPresent(CommandName.class)) {
                            name = gcmd.getClass().getAnnotation(CommandName.class).name();
                        }
                        if (gcmd.getClass().isAnnotationPresent(CommandUsage.class)) {
                            usage = gcmd.getClass().getAnnotation(CommandUsage.class).usage();
                        }
                        if (gcmd.getClass().isAnnotationPresent(CommandDescription.class)) {
                            description = gcmd.getClass().getAnnotation(CommandDescription.class).description();
                        }
                        if (gcmd.getClass().isAnnotationPresent(Console.class)) {
                            commandSender.sendMessage("§6-§e pet " + name + ' ' + usage + " - §7" + description);
                        }
                    }
                } else {
                    PetCommand base = null;
                    for (PetCommand gcmd : commands) {
                        String name = "";
                        if (gcmd.getClass().isAnnotationPresent(CommandName.class)) {
                            name = gcmd.getClass().getAnnotation(CommandName.class).name();
                        }
                        if (name.equalsIgnoreCase(args[0])) {
                            if (gcmd.getClass().isAnnotationPresent(Console.class)) {
                                base = gcmd;
                                break;
                            }
                        }
                    }

                    if (base == null) {
                        commandSender.sendMessage(PetCore.get().getMessages().getString("Unknown-commands", true));
                        return true;
                    }

                    ArrayList<String> newArgs = new ArrayList<>();
                    Collections.addAll(newArgs, args);
                    newArgs.remove(0);
                    args = newArgs.toArray(new String[newArgs.size()]);

                    base.onCommand(commandSender, args);

                }
                return true;
            } else {
                Player p = (Player) commandSender;
                PetOwner owner = PetOwner.getPetOwner(p);

                if (args.length == 0) {
                    PetCore.get().getInvLoaders().SELECTION.open(owner);
                    return true;
                } else {
                    PetCommand base = null;
                    for (PetCommand gcmd : commands) {
                        String name = "";
                        if (gcmd.getClass().isAnnotationPresent(CommandName.class)) {
                            name = gcmd.getClass().getAnnotation(CommandName.class).name();
                        }
                        if (name.equalsIgnoreCase(args[0])) {
                            if (!gcmd.getClass().isAnnotationPresent(Console.class)) {
                                base = gcmd;
                                break;
                            }
                        }
                    }

                    if (base == null) {
                        PetDefault type = plugin.getTypeManager().getItem(args[0]);
                        if (type == null) {
                            p.sendMessage(PetCore.get().getMessages().getString("Unknown-commands", true));
                            return true;
                        }
                        p.performCommand("pet summon " + args[0]);
                        return true;
                    }

                    if ((base.getClass().isAnnotationPresent(CommandPermission.class)) &&
                            (!p.hasPermission(base.getPermission()))) {
                        p.sendMessage(PetCore.get().getMessages().getString("No-Permission", true));
                        return true;
                    }

                    ArrayList<String> newArgs = new ArrayList<>();
                    Collections.addAll(newArgs, args);
                    newArgs.remove(0);
                    args = newArgs.toArray(new String[newArgs.size()]);

                    base.onCommand(p, args);

                }
            }
        }

        return true;
    }
}
