package simplepets.brainsynder.commands.sub;

import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.commands.annotations.Permission;
import simplepets.brainsynder.menu.menuItems.Armor;
import simplepets.brainsynder.menu.menuItems.base.MenuItem;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.reflection.ReflectionUtil;
import simplepets.brainsynder.utils.AdditionalData;
import simplepets.brainsynder.utils.DebugLevel;
import simplepets.brainsynder.utils.ValueType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ICommand(
        name = "generator",
        usage = "&r &r &6[] &7/pet generator <permissions|types|values>",
        description = "Generates a file that can contain Permissions, Pet Types, ETC...",
        style = "{usage}"
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
            if (args.length == 1) {
                generatePermissions ();
            }else{
                if (args[1].equalsIgnoreCase("git")) {
                    generateGitHubPermissions();
                }else if (args[1].equalsIgnoreCase("plugin")) {
                    generatePluginPerms(sender);
                }else{
                    generatePermissions ();
                }
            }
            sender.sendMessage("Permissions have been generated into a file");
            return;
        }

        if (args[0].equalsIgnoreCase("values")) {
            StringBuilder builder = new StringBuilder();
            plugin.getTypeManager().getRawTypes().forEach(type -> {
                builder.append("# ").append(type.getConfigName()).append("\n");
                if (type.getPetData() != null) {
                    type.getPetData().getItemClasses().forEach(clazz -> {
                        MenuItem item = getItem(type, clazz);
                        if (item == null) return;
                        if (!(item instanceof Armor)) builder.append(">      → \"").append(item.getTargetName()).append("\"");
                        if (item.getClass().isAnnotationPresent(ValueType.class)) {
                            ValueType value = item.getClass().getAnnotation(ValueType.class);
                            builder.append(" - type: ").append(value.type()).append(" - default: ").append(value.def()).append("\n");
                            if (!value.target().isEmpty()) {
                                builder.append(">         target: ").append(value.target()).append("\n");
                            }
                        }else {
                            if (item.getClass().getSimpleName().contains("Size")) {
                                builder.append(" - type: Integer").append(" - default: 2").append("\n");
                            }
                        }
                    });
                    builder.append("\n");
                }
            });
            new Logger("Pet Values").log(new File(plugin.getDataFolder().toString() + File.separator + "Generated Files" + File.separator), builder.toString());
            sender.sendMessage("Pet Values have been generated into a file");
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

    /*
    Pet.type.passive:
        default: false
        children:
            Pet.type.armorstand: true
    Pet.type.armorstand.*:
        default: false
        children:
            pet.type.armorstand.armor: true
     */
    public void generatePluginPerms (CommandSender sender) {
        StringBuilder def = new StringBuilder();
        def.append("        default: false").append("\n").append("        children:").append("\n");

        StringBuilder master = new StringBuilder();
        StringBuilder hostile = new StringBuilder();
        StringBuilder passive = new StringBuilder();
        StringBuilder allAllowData = new StringBuilder();
        StringBuilder commandBuilder = new StringBuilder();
        List<StringBuilder> pets = new ArrayList<>();
        List<StringBuilder> other = new ArrayList<>();
        PetCore core = PetCore.get();

        parent.getSubCommands().forEach(command -> {
            if (command.needsPermission()) commandBuilder.append("            ").append(command.getPermission()).append(": true\n");
            if (command.getClass().isAnnotationPresent(AdditionalData.class)) {
                AdditionalData data = command.getClass().getAnnotation(AdditionalData.class);
                for (String permission : data.otherPermissions()) {
                    commandBuilder.append("            ").append(permission).append(": true\n");
                }
            }
        });
        master
                .append("    pet.commands.*:").append("\n")
                .append(def).append(commandBuilder);

        // Generate Pet Permissions
        core.getTypeManager().getRawTypes().forEach(type -> {
            PetCore.get().debug(DebugLevel.DEBUG, "Type: "+type.getConfigName());
            boolean isPassive = ((type.getAdditionalData() == null) || type.getAdditionalData().passive());
            if (isPassive) {
                passive.append("            ").append(type.getPermission()).append(".*: true\n");
            }else{
                hostile.append("            ").append(type.getPermission()).append(".*: true\n");
            }
            allAllowData.append("            ").append(type.getPermission()).append(".data.*: true\n");

            StringBuilder builder = new StringBuilder();
            StringBuilder allData = new StringBuilder();

            allData.append("    ").append(type.getPermission()).append(".data.*:").append("\n");
            allData.append(def);

            builder.append("    ").append(type.getPermission()).append(".*:");
            if (!type.isSupported()) builder.append("  # Only Supported from ").append(type.toSupportString());
            builder.append("\n");

            builder.append(def);
            builder.append("            ").append(type.getPermission()).append(".fly: true\n");
            builder.append("            ").append(type.getPermission()).append(".hat: true\n");
            builder.append("            ").append(type.getPermission()).append(".mount: true\n");
            for (Class<? extends MenuItem> item : type.getPetData().getItemClasses()) {
                MenuItem item1 = getItem(type, item);
                if (item1 != null) {
                    allData.append("            ").append(item1.getPermission()).append(": true\n");
                    builder.append("            ").append(item1.getPermission()).append(": true\n");
                }
            }
            other.add(allData);
            pets.add(builder);
        });


        master
                .append("    Pet.type.*:").append("\n")
                .append(def)
                .append("            Pet.type.passive: true").append("\n")
                .append("            Pet.type.hostile: true").append("\n")
                .append("    Pet.type.passive:").append("\n")
                .append(def)
                .append(passive)
                .append("    Pet.type.hostile:").append("\n")
                .append(def)
                .append(hostile)
                .append("    Pet.type.*.data:").append("\n")
                .append(def)
                .append(allAllowData);

        pets.forEach(master::append);
        other.forEach(master::append);

        new Logger("PluginYAML Permissions").log(new File(core.getDataFolder().toString() + File.separator + "Generated Files" + File.separator), master.toString().toLowerCase());
        sender.sendMessage("Pet Types have been generated into a file");

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

    private void generateGitHubPermissions () {
        PetCore plugin = PetCore.get();
        StringBuilder builder = new StringBuilder();

        builder.append("This list is not final More stuff might be added: ").append("\n\n");
        builder.append("## All Permissions: ").append("\n");
        Arrays.asList(
                "Pet.*", "Pet.PetToMount", "Pet.PetToHat",
                "Pet.type.*", "Pet.type.passive", "Pet.type.hostile"
        ).forEach(permission -> builder.append("  - ").append(permission).append("\n"));
        builder.append("***\n");

        builder.append("## Pet Renaming Permissions: ").append("\n");
        Arrays.asList(
                "Pet.name",
                "Pet.name.color", "Pet.name.magic",
                "Pet.name.bypass", "Pet.name.bypassLimit",
                "Pet.name.*"
        ).forEach(permission -> builder.append("  - ").append(permission).append("\n"));
        builder.append("***\n");

        builder.append("## Other Permissions: ").append("\n");
        Arrays.asList(
                "Pet.economy.bypass",
                "Pet.itemstorage"
        ).forEach(permission -> builder.append("  - ").append(permission).append("\n"));
        builder.append("***\n");

        builder.append("## Command Permissions: ").append("\n");
        builder.append("  - Pet.commands.* \n");
        builder.append("  - Pet.commands.summon.other \n");
        builder.append("  - Pet.commands.remove.other \n");
        builder.append("  - Pet.commands.inv.other \n");
        builder.append("  - Pet.commands.info.debug \n");
        parent.getSubCommands().forEach(command -> {
            if (command.needsPermission())
                builder.append("  - ").append(command.getPermission()).append("\n");
        });
        builder.append("***\n");


        builder.append("## Pet Permissions: ").append("\n");
        plugin.getTypeManager().getRawTypes().forEach(type -> {
            builder.append("<span style=\"padding-left=4px\"/> ").append(type.getPermission());
            String support = type.toSupportString();
            if (support.equals("UNKNOWN")) support = "1.8 -> LATEST";
            builder.append("  `(").append(support
                        .replace("v1_", "1.").replace("_R1", "").replace("_R3", "")).append(")`");
            builder.append("\n");

            builder.append(">      * ").append(type.getPermission()).append(".fly").append("\n");
            builder.append(">      * ").append(type.getPermission()).append(".hat").append("\n");
            builder.append(">      * ").append(type.getPermission()).append(".mount").append("\n");
            if (type.getPetData() != null) {
                builder.append(">       ~ ").append(type.getPermission()).append(".data.*").append("\n");
                type.getPetData().getItemClasses().forEach(clazz -> {
                    MenuItem item = getItem(type, clazz);
                    if (item != null) builder.append(">       ~ ").append(item.getPermission()).append("\n");
                });
            }
        });

        new Logger("GitHubPermissions").log(new File(plugin.getDataFolder().toString() + File.separator + "Generated Files" + File.separator), builder.toString());

    }
}
