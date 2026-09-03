package simplepets.brainsynder.utils;

import org.apache.commons.io.FileUtils;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.PluginUtilities;
import org.bsdevelopment.pluginutils.chat.TellrawMessage;
import org.bsdevelopment.pluginutils.files.YamlFile;
import org.bsdevelopment.pluginutils.reflection.FieldAccessor;
import org.bsdevelopment.pluginutils.reflection.Reflection;
import org.bsdevelopment.pluginutils.version.ServerVersion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.SpawnResult;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.pet.CommandReason;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.plugin.config.MessageOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.managers.ParticleManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Utilities {
    public static List<Material> getBlacklistedMaterials() {
        List<Material> materials = new ArrayList<>();
        for (Material material : Material.values()) {
            String name = material.name();

            if (name.contains("GLASS_PANE")) materials.add(material);
            if (name.contains("FENCE")) materials.add(material);
            if (name.contains("DOOR") && (!name.contains("TRAP"))) materials.add(material);
            if (name.contains("FENCE_GATE")) materials.add(material);
            if (name.contains("THIN")) materials.add(material);
        }

        return materials;
    }

    public static boolean handlePetSpawning(PetUser user, PetType type, StorageTagCompound compound, boolean checkDataPermissions) {
        Player player = user.getPlayer();

        if (user.isOnPetChangeCooldown()) {
            player.sendMessage(PetCore.getInstance().getMessageFile()
                    .getTranslation(MessageOption.PET_ON_COOLDOWN)
                    .replace("{seconds}", String.valueOf(user.getRemainingCooldownSeconds())));
            return false;
        }

        if (type.isInDevelopment() && (!ConfigOption.PET_TOGGLES_DEV_MOBS.get())) {
            player.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.PET_IN_DEVELOPMENT).replace("{type}", type.getName()));
            return false;
        }

        if (!type.isSupported()) {
            player.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.PET_NOT_SUPPORTED).replace("{type}", type.getName()));
            return false;
        }

        ISpawnUtil spawner = SimplePets.getSpawnUtil();
        if (spawner == null) return false;

        if (!spawner.isRegistered(type)) {
            player.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.PET_NOT_REGISTERED).replace("{type}", type.getName()));
            return false;
        }

        if (!Utilities.hasPermission(player, type.getPermission())
                && ((!user.getOwnedPets().contains(type)) && ConfigOption.UTILIZE_PURCHASED_PETS.get())) {
            return false;
        }

        applyPetDataDefaults(type, compound);

        SpawnResult<IEntityPet> result = spawner.spawnEntityPet(type, user, compound);

        return switch (result.state()) {
            case SUCCESS -> {
                user.recordPetChange();
                player.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.SUMMONED_PET).replace("{type}", type.getName()));
                yield true;
            }

            case FAILURE -> {
                SimplePets.getParticleHandler().sendParticle(ParticleManager.Reason.FAILED, player, player.getLocation());
                TellrawMessage.of(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.FAILED_SUMMON, false).replace("{type}", type.getName()))
                        .tooltip(result.failMessage().split("\n"))
                        .send(player);

                yield false;
            }

            case EMPTY -> {
                SimplePets.getParticleHandler().sendParticle(ParticleManager.Reason.FAILED, player, player.getLocation());
                player.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.FAILED_SUMMON).replace("{type}", type.getName()));
                yield false;
            }
        };
    }

    public static void applyPetDataDefaults(PetType type, StorageTagCompound compound) {
        if (type == null || compound == null) return;

        for (PetData<?> petData : type.getPetData()) {
            String key = petData.namespace();
            if (compound.hasKey(key)) continue;

            Object value = petData.getDefault(type).orElseGet(petData::defaultValue);
            if (value != null) {
                compound.set(key, value);
            }
        }
    }

    public static void runPetCommands(CommandReason reason, PetUser owner, PetType type) {
        SimplePets.getPetUtilities().runPetCommands(reason, owner, type, null);
    }

    public static void runPetCommands(CommandReason reason, PetUser owner, PetType type, Location location) {
        SimplePets.getPetUtilities().runPetCommands(reason, owner, type, location);
    }

    /**
     * This method replaces all the placeholders with the correct replacements
     * <p>
     * List of Placeholders
     * <ul>
     *     <li>{petX} - The pets X coordinate</li>
     *     <li>{petY} - The pets Y coordinate</li>
     *     <li>{petZ} - The pets Z coordinate</li>
     *     <li>{ownerX} - The pet owners X coordinate</li>
     *     <li>{ownerY} - The pet owners Y coordinate</li>
     *     <li>{ownerZ} - The pet owners Z coordinate</li>
     *     <li>{ownerName} - The pet owners name</li>
     *     <li>{petName} - The pets current name</li>
     *     <li>{petType} - The type of pet</li>
     * </ul>
     *
     * @param owner   The owner of the pet
     * @param entity  The pet the commands are run for
     * @param command The command that is being run
     * @return | Returns the command with all the replaced placeholders
     */
    public static String handleCommandPlaceholders(PetUser owner, IEntityPet entity, Location petLoc, String command) {
        return SimplePets.getPetUtilities().handlePlaceholders(owner, entity, petLoc, command);
    }

    public static void setPassenger(Player player, Entity entity, Entity passenger) {
        try {
            SimplePets.getDebugLogger().debug(DebugLevel.HIDDEN, "Set passenger: " + entity.addPassenger(passenger) + " - " + entity.getClass().getName() + ", " + passenger.getClass().getName());
        } catch (Exception e) {
            SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Could not run method IEntityPet#setPassenger");
            e.printStackTrace();
        }
    }

    public static boolean hasPermission(CommandSender sender, String permission) {
        return hasPermission(sender, permission, false);
    }

    public static boolean hasPermission(CommandSender sender, String permission, boolean strict) {
        if (sender instanceof ConsoleCommandSender) return true;
        if ((permission == null) || (permission.isEmpty())) return true;
        //if (sender.isOp()) return true;

        int value = getPermission(sender, permission, strict);
        if (ConfigOption.PERMISSIONS_IGNORE_LIST.get().contains(permission))
            return true;
        return value == 1;
    }

    public static int getPermission(CommandSender sender, String permission, boolean strict) {
        if (ConfigOption.PERMISSIONS_ENABLED.get()) {
            if (strict) {
                for (PermissionAttachmentInfo info : sender.getEffectivePermissions()) {
                    if (info.getPermission().equalsIgnoreCase(permission)) {
                        return info.getValue() ? 1 : 0;
                    }
                }
                return -1;
            } else {
                return sender.hasPermission(permission) ? 1 : 0;
            }
        } else {
            return 1;
        }
    }

    public static int parseTypeSaveLimit(PetType type) {
        for (String line : ConfigOption.PET_SAVES_TYPE_LIMIT.get()) {
            if (!line.contains("-")) continue;
            String[] args = line.split("-");
            if (args.length != 2) continue;

            PetType target = PetType.getPetType(args[0]).orElse(null);
            if (target == null) continue;

            if (type != target) continue;
            try {
                return Integer.parseInt(args[1].trim());
            } catch (NumberFormatException e) {
                SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Unable to parse pet-type-limit for '" + args[0] + "', " + args[1] + " is not a valid number.");
                return -1;
            }
        }

        return -1;
    }

    public static int getPermissionAmount(Player player, int defaultValue, String partialPermission) {
        int amount = defaultValue;
        if (!partialPermission.endsWith(".")) return defaultValue;

        for (PermissionAttachmentInfo permission : player.getEffectivePermissions()) {
            if (!permission.getValue()) continue;
            if (!permission.getPermission().startsWith(partialPermission)) continue;

            String strAmount = permission.getPermission().substring((partialPermission.lastIndexOf(".") + 1));
            int permAmount = Integer.parseInt(strAmount);
            if (permAmount >= amount) amount = permAmount;
        }
        return amount;
    }

    public static void removePassenger(Entity entity, Entity passenger) {
        try {
            entity.eject();
            if (entity instanceof Player) {
                resetRideCooldown(passenger);
            }
        } catch (Exception e) {
            SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Could not run method IEntityPet#removePassenger");
            e.printStackTrace();
        }
    }

    public static void resetRideCooldown(Entity entity) {
        FieldAccessor<Integer> field = FieldAccessor.getField(
            Reflection.resolveMinecraftClass("Entity", "world.entity"),
            VersionFields.fromServerVersion(ServerVersion.getVersion()).getRideCooldownField(),
            Integer.TYPE
        );

        field.set(Reflection.fetchEntityHandle(entity), 0);
    }

    public static boolean isSimilar(ItemStack main, ItemStack check) {
        List<Boolean> values = new ArrayList<>();
        if ((main == null) || (check == null)) return false;
        //if (main.isSimilar(check)) return true;

        if (main.getType() == check.getType()) {
            if (main.hasItemMeta() && check.hasItemMeta()) {
                ItemMeta mainMeta = main.getItemMeta();
                ItemMeta checkMeta = check.getItemMeta();
                if (mainMeta.hasDisplayName() && checkMeta.hasDisplayName()) {
                    values.add(mainMeta.getDisplayName().equals(checkMeta.getDisplayName()));
                }

                if (mainMeta.hasLore() && checkMeta.hasLore()) {
                    values.add(mainMeta.getLore().equals(checkMeta.getLore()));
                }

                if (mainMeta.hasEnchants() && checkMeta.hasEnchants()) {
                    values.add(mainMeta.getEnchants().equals(checkMeta.getEnchants()));
                }

                if (!values.isEmpty()) return !values.contains(false);
            }
        }

        return main.isSimilar(check);
    }

    public static void makeBackup(YamlFile yamlFile, File backup) {
        File file = yamlFile.getFile();
        try {
            if (!backup.exists()) backup.createNewFile();
            FileUtils.copyFile(file, backup);
            SimplePets.getDebugLogger().debug(DebugBuilder.build(yamlFile.getClass()).setLevel(DebugLevel.NORMAL).setMessages(
                "A new major config change was detected",
                "Saving the old config to 'plugins/SimplePets/" + backup.getParentFile().getName() + "/" + backup.getName() + "'"
            ));
        } catch (IOException e) {
            SimplePets.getDebugLogger().debug(DebugBuilder.build(yamlFile.getClass()).setLevel(DebugLevel.ERROR).setMessages(
                "Failed to create file backup for: " + file.getName(),
                "Error: " + e.getMessage()
            ));
            e.printStackTrace();
        }

    }

    public static void getInputStreamString(String link, Consumer<String> stringReturn) {
        CompletableFuture.runAsync(() -> {
            try {
                System.setProperty("http.agent", "Chrome");
                URL url = new URL(link);
                URLConnection connection = url.openConnection();
                connection.addRequestProperty("User-Agent", "Mozilla/5.0");
                connection.addRequestProperty("Content-Encoding", "gzip");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                final InputStream stream = connection.getInputStream();
                PluginUtilities.getScheduler().runTask(() -> {
                    try {
                        stringReturn.accept(new String(stream.readAllBytes(), StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception ignored) {
            }
        });
    }

    public static String encodeBase64(String s) {
        return Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeBase64(String s) {
        return new String(Base64.getDecoder().decode(s), StandardCharsets.UTF_8);
    }

    public static long toTicks(long time, TimeUnit unit) {
        long ticks = 20;
        if (unit == TimeUnit.DAYS) {
            ticks = ticks * 60 * 60 * 24 * time;
        } else if (unit == TimeUnit.HOURS) {
            ticks = ticks * 60 * 60 * time;
        } else if (unit == TimeUnit.MINUTES) {
            ticks = ticks * 60 * time;
        } else {
            ticks = ticks * time;
        }
        return ticks;
    }
}
