package simplepets.brainsynder.commands.list;

import com.google.common.collect.Lists;
import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.addon.AddonPermissions;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.commands.PetsCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@ICommand(
        name = "permissions",
        description = "Generates a file that contains all the permissions in the plugin"
)
@Permission(permission = "permissions", adminCommand = true)
public class PermissionsCommand extends PetSubCommand {
    private final PetsCommand parent;

    public PermissionsCommand(PetsCommand parent) {
        super(parent.getPlugin());
        this.parent = parent;
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            generatePluginPermissions(true);
        } else {
            generatePluginPermissions(Boolean.parseBoolean(args[0]));
        }

        sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+ ChatColor.GRAY+"Generated the permissions.yml file");
    }

    private void addPermission (StringBuilder builder, String permission, String comment, String defaultType) {
        builder.append("    ").append(permission).append(": ");

        if ((comment != null) && (!comment.isEmpty())) builder.append(" # ").append(comment);

        builder.append("\n        default: ").append(defaultType).append("\n");
    }

    private void generatePluginPermissions(boolean notDeveloper) {
        StringBuilder def = new StringBuilder();
        def.append("        default: false").append("\n").append("        children:").append("\n");


        StringBuilder master = new StringBuilder();
        addPermission(master, "pet.amount.bypass", "This permission bypasses the limit of how many pets can be spawned", "op");
        if (notDeveloper) addPermission(master, "pet.amount.<number>", "This permission sets how many pets the player can have spawned", "false");
        addPermission(master, "pet.name.bypass", "This permission bypasses any of the pet renaming checks", "op");
        addPermission(master, "pet.name.color", "This permission is to allow players to add color codes when renaming their pet", "true");
        addPermission(master, "pet.name.color.hex", "This permission is to allow players to add HEX color codes when renaming their pet (Eg: &#FFFFFF)", "op");
        master.append("\n\n");

        StringBuilder hostile = new StringBuilder();
        StringBuilder passive = new StringBuilder();
        StringBuilder allAllowData = new StringBuilder();
        StringBuilder commandBuilder = new StringBuilder();
        List<StringBuilder> pets = new ArrayList<>();
        List<StringBuilder> other = new ArrayList<>();
        PetCore core = parent.getPlugin();

        parent.getSubCommands().forEach(command -> {
            if (!command.getClass().isAnnotationPresent(Permission.class)) return;
            Permission data = command.getClass().getAnnotation(Permission.class);

            String permission = "pet.commands." + data.permission();
            if (permission.isEmpty()) return;
            commandBuilder.append("            ").append(permission).append(": true\n");
            List<String> additional = Lists.newArrayList(data.additionalPermissions());
            if (additional.isEmpty()) return;
            for (String addition : additional) {
                if (addition.isEmpty()) continue;
                String comment = "";
                if (addition.equals("all_other"))
                    comment = "  # Will allow the player spawn/change for all the selected player pets";
                if (addition.equals("all")) comment = "  # Will allow the player to run the command for all the pets";
                if (addition.equals("nbt"))
                    comment = "  # Will allow the player to input NBT data to modify the pet (Will bypass the data permissions for the pet)";
                if (addition.equals("other")) comment = "  # Will allow the player to spawn/change other players data";
                commandBuilder.append("            ").append(permission).append(".").append(addition).append(": true").append(comment).append("\n");
            }

            String allowDefault = "false # Not given to players by default";
            if (data.adminCommand()) {
                allowDefault = "op # Only Operators should be given this permission";
            }else if (data.defaultAllow()) {
                allowDefault = "true # Allows players to use them by default";
            }

            master.append("    ").append(permission).append(": # Grants access to the '/pet ").append(command.getCommand(command.getClass()).name()).append("' command\n")
                    .append("        default: ").append(allowDefault).append("\n\n");
        });
        master.append("\n")
                .append("    pet.commands.*:  # Grants the player to use all commands (NOT recommended)").append("\n")
                .append(def).append(commandBuilder).append("\n\n");


        if (notDeveloper) {
            // Addon permissions
            if ((!AddonPermissions.getPermissions().isEmpty()) || (!AddonPermissions.getParentPermissions().isEmpty()))
                master.append("    # Here is all the Addon permissions (if there are any)\n\n");
            AddonPermissions.getPermissions().forEach((addonName, list) -> {
                master.append("    # Permissions for the ").append(addonName).append(" addon\n");

                list.forEach(data -> {
                    String description = " # " + data.getDescription();
                    if (description.equals(" # ")) description = "";

                    master.append("    ").append(data.getPermission()).append(":").append(description).append("\n")
                            .append("        default: ").append(data.getType().toString()).append("\n");
                });
            });

            AddonPermissions.getParentPermissions().forEach((addonName, permissionMap) -> {
                if (!master.toString().contains("    # Permissions for the " + addonName + " addon"))
                    master.append("    # Permissions for the ").append(addonName).append(" addon\n");
                permissionMap.forEach((parent, children) -> {
                    String parentDescription = " # " + parent.getDescription();
                    if (parentDescription.equals(" # ")) parentDescription = "";
                    master.append("    ").append(parent.getPermission()).append(":  ").append(parentDescription).append("\n");

                    children.forEach(data -> {
                        String description = " # " + data.getDescription();
                        if (description.equals(" # ")) description = "";
                        master.append("        ").append(data.getPermission()).append(": ").append(description).append("\n");
                    });
                });
            });
            master.append("\n");
        }


        // Generate Pet Permissions
        for (PetType type : PetType.values()) {
            if (type == PetType.UNKNOWN) continue;
            String path = type.getEntityClass().getName();
            String permission = type.getPermission();
            if (path.contains("hostile")) {
                hostile.append("            ").append(permission).append(".*: true\n");
            } else if (path.contains("passive") || path.contains("ambient")) {
                passive.append("            ").append(permission).append(".*: true\n");
            }
            allAllowData.append("            ").append(permission).append(".data.*: true\n");

            StringBuilder builder = new StringBuilder();
            StringBuilder allData = new StringBuilder();

            allData.append("    ").append(permission).append(".data.*:  # Grants access to all the data permissions for this pet").append("\n");
            allData.append(def);

            builder.append("    ").append(permission).append(".*:  # Will grant access to spawn the pet as well as all the other permissions for this pet").append("\n");

            builder.append(def);
            builder.append("            ").append(permission).append(": true  # Will allow ").append(type.getName()).append(" to be spawned (if enabled)\n");
            builder.append("            ").append(permission).append(".fly: true  # Will allow ").append(type.getName()).append(" to fly (if enabled)\n");
            builder.append("            ").append(permission).append(".hat: true  # Will allow ").append(type.getName()).append(" to be a hat (if enabled)\n");
            builder.append("            ").append(permission).append(".mount: true  # Will allow ").append(type.getName()).append(" to be mounted (if enabled)\n");
            builder.append("            ").append(permission).append(".data.*").append(": true\n");
            type.getPetData().forEach(petData -> {
                String name = petData.getNamespace().namespace();
                allData.append("            ").append(permission).append(".data.").append(name).append(": true\n");
            });
            other.add(allData);
            pets.add(builder);
        }


        List<StringBuilder> fly = new ArrayList<>();
        fly.add(new StringBuilder()
                .append("    pet.type.*.fly:  # Will allow all pets to fly (if enabled)").append("\n")
                .append("        default: false").append("\n")
                .append("        children:").append("\n")
        );
        for (PetType type : PetType.values()) {
            if (type == PetType.UNKNOWN) continue;
            String permission = type.getPermission();
            fly.add(new StringBuilder()
                    .append("            ").append(permission).append(".fly: true").append("\n")
            );
        }


        List<StringBuilder> hat = new ArrayList<>();
        hat.add(new StringBuilder()
                .append("    pet.type.*.hat:  # Will allow all pets to be a hat (if enabled)").append("\n")
                .append("        default: false").append("\n")
                .append("        children:").append("\n")
        );
        for (PetType type : PetType.values()) {
            if (type == PetType.UNKNOWN) continue;
            String permission = type.getPermission();
            hat.add(new StringBuilder()
                    .append("            ").append(permission).append(".hat: true").append("\n")
            );
        }


        List<StringBuilder> savesBypass = new ArrayList<>();
        savesBypass.add(new StringBuilder()
                .append("    pet.saves.bypass:  # Will allow all player to bypass the saves limit (if enabled)").append("\n")
                .append("        default: false").append("\n")
                .append("        children:").append("\n")
        );
        for (PetType type : PetType.values()) {
            if (type == PetType.UNKNOWN) continue;
            savesBypass.add(new StringBuilder()
                    .append("            pet.saves.").append(type.name().toLowerCase().replace("_", "")).append(".bypass: true").append("\n")
            );
        }

        List<StringBuilder> mount = new ArrayList<>();
        mount.add(new StringBuilder()
                .append("    pet.type.*.mount:  # Will allow all pets to be mounted (if enabled)").append("\n")
                .append("        default: false").append("\n")
                .append("        children:").append("\n")
        );
        for (PetType type : PetType.values()) {
            if (type == PetType.UNKNOWN) continue;
            String permission = type.getPermission();
            mount.add(new StringBuilder()
                    .append("            ").append(permission).append(".mount: true").append("\n")
            );
        }

        savesBypass.forEach(master::append);

        master
                .append("\n")
                .append("    pet.type.*:  # Grants access to all pets").append("\n")
                .append(def)
                .append("            pet.type.passive: true").append("\n")
                .append("            pet.type.hostile: true").append("\n")
                .append("    pet.type.passive:  # Will grant access to all pets that would be passive in vanilla").append("\n")
                .append(def)
                .append(passive).append("\n\n")
                .append("    pet.type.hostile:  # Will grant access to all the pets that would be hostile in vanilla").append("\n")
                .append(def)
                .append(hostile).append("\n\n")
                .append("    pet.type.*.data.*:  # Will grant all data permissions for all pets").append("\n")
                .append(def)
                .append(allAllowData).append("\n\n");

        fly.forEach(master::append);
        master.append("\n\n");
        hat.forEach(master::append);
        master.append("\n\n");
        mount.forEach(master::append);
        master.append("\n\n");

        pets.forEach(stringBuilder -> master.append(stringBuilder).append("\n\n"));
        other.forEach(stringBuilder -> master.append(stringBuilder).append("\n\n"));

        log(new File(core.getDataFolder() + File.separator + "Generated Files" + File.separator), "permissions.yml", master.toString());
    }


    private void log(File folder, String fileName, String message) {
        try {
            if (!folder.exists()) folder.mkdirs();
            File saveTo = new File(folder, fileName);
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
