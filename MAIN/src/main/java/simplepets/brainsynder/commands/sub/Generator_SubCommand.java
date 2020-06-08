package simplepets.brainsynder.commands.sub;

import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.commands.annotations.Permission;
import simplepets.brainsynder.menu.menuItems.base.MenuItem;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.reflection.ReflectionUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

@ICommand(
        name = "generator",
        usage = "&r &r &6[] &7/pet generator <permissions|types>",
        description = "Generates a file that can contain Permissions, Pet Types, ETC..."
)
@Permission(permission = "generator")
public class Generator_SubCommand extends PetSubCommand {
    private final PetCommand parent;
    public Generator_SubCommand(PetCommand parent) {
        this.parent = parent;
        registerCompletion(1, Arrays.asList("permissions","types"));
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return;
        }
        PetCore plugin = PetCore.get();

        if (args[0].equalsIgnoreCase("permissions")) {
            generatePermissions ();
            sender.sendMessage("Permissions have been generated into a file");
            return;
        }

        if (args[0].equalsIgnoreCase("types")) {
            StringBuilder builder = new StringBuilder();
            plugin.getTypeManager().getTypes().forEach(type -> {
                builder.append(type.getConfigName()).append("\n");
                builder.append("   Enabled: ").append(type.isEnabled()).append("\n");
                builder.append("   Supported: ").append(type.isSupported()).append("\n");
                builder.append("   Supported Version: ").append(type.getAllowedVersion()).append("\n");
                builder.append("   Ride Speed: ").append(type.getRideSpeed()).append("\n");
                builder.append("   Walk Speed: ").append(type.getSpeed()).append("\n");
                builder.append("   Default Sound: ").append(type.getDefaultSound().name()).append("\n");
                builder.append("   Sound: ").append(type.getSound().name()).append("\n");
                builder.append("   Display Name:").append(type.getDisplayName()).append("\n");
            });
            new Logger("Pet Types").log(new File(plugin.getDataFolder().toString() + File.separator + "Generated Files" + File.separator), builder.toString());
            sender.sendMessage("Pet Types have been generated into a file");
            return;
        }
        sendUsage(sender);
    }

    private class Logger {
        private final String fileName;

        public Logger(String fileName) {
            this.fileName = fileName;
        }

        public void log(File folder, String message) {
            try {
                if (!folder.exists()) folder.mkdirs();
                File saveTo = new File(folder, fileName + ".txt");
                if (saveTo.exists()) saveTo.delete();
                saveTo.createNewFile();

                FileWriter fw = new FileWriter(saveTo, true);
                PrintWriter pw = new PrintWriter(fw);
                pw.println(message);
                pw.flush();
                pw.close();
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }
    }

    private MenuItem getItem(PetType type, Class<? extends MenuItem> clazz) {
        try {
            return ReflectionUtil.initiateClass(ReflectionUtil.fillConstructor(clazz, PetType.class), type);
        } catch (Exception e) {
            return null;
        }
    }

    private void generatePermissions () {
        PetCore plugin = PetCore.get();
        StringBuilder builder = new StringBuilder();

        builder.append("This list is not final More stuff might be added: ").append("\n\n");
        builder.append("All Permissions: ").append("\n");
        Arrays.asList(
                "Pet.*", "Pet.PetToMount", "Pet.PetToHat",
                "Pet.type.*", "Pet.type.passive", "Pet.type.hostile"
        ).forEach(permission -> builder.append("  - ").append(permission).append("\n"));


        builder.append("Pet Renaming Permissions: ").append("\n");
        Arrays.asList(
                "Pet.name",
                "Pet.name.color", "Pet.name.magic",
                "Pet.name.bypass", "Pet.name.bypassLimit",
                "Pet.name.*"
        ).forEach(permission -> builder.append("  - ").append(permission).append("\n"));

        builder.append("Other Permissions: ").append("\n");
        Arrays.asList(
                "Pet.economy.bypass"
        ).forEach(permission -> builder.append("  - ").append(permission).append("\n"));

        builder.append("Command Permissions: ").append("\n");
        builder.append("  - Pet.commands.* \n");
        builder.append("  - Pet.commands.summon.other \n");
        builder.append("  - Pet.commands.remove.other \n");
        builder.append("  - Pet.commands.inv.other \n");
        builder.append("  - Pet.commands.info.debug \n");
        parent.getSubCommands().forEach(command -> {
                if (command.needsPermission())
                    builder.append("  - ").append(command.getPermission()).append("\n");
        });


        builder.append("Pet Permissions: ").append("\n");
        plugin.getTypeManager().getTypes().forEach(type -> {
            builder.append("  - ").append(type.getPermission()).append("\n");
            builder.append("     • ").append(type.getPermission()).append(".fly").append("\n");
            builder.append("     • ").append(type.getPermission()).append(".hat").append("\n");
            builder.append("     • ").append(type.getPermission()).append(".mount").append("\n");
            if (type.getPetData() != null) {
                builder.append("      → ").append(type.getPermission()).append(".data.*").append("\n");
                type.getPetData().getItemClasses().forEach(clazz -> {
                    MenuItem item = getItem(type, clazz);
                    if (item != null) builder.append("      → ").append(item.getPermission()).append("\n");
                });
            }
        });

        new Logger("Permissions").log(new File(plugin.getDataFolder().toString() + File.separator + "Generated Files" + File.separator), builder.toString());

    }
}
