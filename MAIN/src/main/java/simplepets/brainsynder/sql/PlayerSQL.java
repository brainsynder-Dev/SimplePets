

package simplepets.brainsynder.sql;

import lib.brainsynder.files.StorageFile;
import lib.brainsynder.json.Json;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonValue;
import lib.brainsynder.nbt.JsonToNBT;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.nbt.StorageTagList;
import lib.brainsynder.nbt.StorageTagString;
import lib.brainsynder.nbt.other.NBTException;
import lib.brainsynder.utils.Base64Wrapper;
import lib.brainsynder.utils.Triple;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.impl.PetOwner;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PlayerSQL extends SQLManager {
    private static PlayerSQL instance;

    public PlayerSQL() {
        super(false);
        instance = this;
    }

    public static PlayerSQL getInstance() {
        return instance;
    }

    public void fetchRowCount(Consumer<Integer> consumer) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = implementConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM `" + tablePrefix + "_players`");
                ResultSet result = statement.executeQuery();
                int size = 0;
                if (usingSqlite) {
                    while (result.next()) size++;
                } else {
                    result = statement.executeQuery("SELECT COUNT(*) FROM `" + tablePrefix + "_players`");
                    if (result.next()) size = result.getInt(1);
                }
                result.close();
                statement.close();

                int finalSize = size;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        consumer.accept(finalSize);
                    }
                }.runTask(PetCore.getInstance());
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void createTable() {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = implementConnection()) {
                String table = "CREATE TABLE IF NOT EXISTS " + tablePrefix + "_players (" +
                        "`uuid` VARCHAR(265) NOT NULL," +
                        "`name` VARCHAR(265) NOT NULL," +
                        "`UnlockedPets` " + getStupidTextThing() + " NOT NULL," +
                        "`PetName` " + getStupidTextThing() + " NOT NULL," +
                        "`NeedsRespawn` " + getStupidTextThing() + " NOT NULL," +
                        "`SavedPets` " + getStupidTextThing() + " NOT NULL" +
                        ")";
                try (PreparedStatement createTable = connection.prepareStatement(table)) {
                    createTable.executeUpdate();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            transferOldData();
        });
    }

    @Override
    public void transferOldData() {
        File folder = new File(PetCore.getInstance().getDataFolder() + File.separator + "PlayerData");
        if (!folder.exists()) return;
        if (folder.listFiles() == null) return;
        if (folder.listFiles().length == 0) return;
        Arrays.asList(folder.listFiles()).forEach(file -> {
            String uuid = file.getName().replace(".stc", "");
            StorageFile storage = new StorageFile(file);
            StorageTagCompound compound = new StorageTagCompound();
            compound.setUniqueId("uuid", UUID.fromString(uuid));
            compound.setString("name", storage.getString("username"));

            String respawn = storage.getString("NeedsRespawn");
            if ((respawn != null) && (!respawn.equalsIgnoreCase("null"))) {
                if (Base64Wrapper.isEncoded(respawn)) {
                    // They have a pet to respawn
                    try {
                        StorageTagCompound compound1 = new StorageTagCompound();
                        StorageTagCompound tag = JsonToNBT.getTagFromJson(Base64Wrapper.decodeString(respawn));
                        compound1.setTag("data", tag);
                        compound1.setString("type", tag.getString("PetType"));
                        compound.setTag("spawned_pets", new StorageTagList().appendTag(compound1));
                    } catch (NBTException e) {
                        e.printStackTrace();
                    }
                }
            }

            StorageTagList ownedPets = new StorageTagList();
            if (storage.getTag("PurchasedPets") instanceof StorageTagList) {
                // Was saved as StorageTagList
                ownedPets = (StorageTagList) storage.getTag("PurchasedPets");
            } else {
                // Was saved in the old format
                String raw = storage.getString("PurchasedPets");
                String decoded = Base64Wrapper.decodeString(raw);
                JsonArray array = (JsonArray) Json.parse(decoded);
                for (JsonValue value : array.values()) {
                    String name = value.asString();
                    ownedPets.appendTag(new StorageTagString(name));
                }
            }
            compound.setTag("owned_pets", ownedPets);
            compound.setTag("pet_names", new StorageTagList());

            {
                // Saves the Inventory data to the new sql
                String data = storage.getString("ItemStorage");
                if (Base64Wrapper.isEncoded(data)) {
                    try {
                        InventorySQL.getInstance().uploadData(UUID.fromString(uuid), JsonToNBT.getTagFromJson(Base64Wrapper.decodeString(data)));
                    } catch (NBTException ignored) {
                    }
                }
            }
            // Delete the file after the data transfer
            file.delete();
        });
    }

    public StorageTagCompound rowToCompound(UUID uuid, ResultSet results, boolean syncLogs) throws SQLException {
        StorageTagCompound compound = new StorageTagCompound();

        // Loads the pets the player purchased
        String raw = results.getString("UnlockedPets");
        try {
            if (!raw.equals("W10=")) {
                compound.setTag("owned_pets", JsonToNBT.parse(Base64Wrapper.decodeString(raw)).toList());
            }
        } catch (NBTException e) {
            SimplePets.getDebugLogger().debug(DebugBuilder.build().setMessages(
                    "Failed to load 'UnlockedPets' for uuid: " + uuid,
                    "Result: " + raw
            ).setSync(syncLogs).setLevel(DebugLevel.ERROR));
        }

        // Loads pet names
        String rawName = results.getString("PetName");
        if (Base64Wrapper.isEncoded(rawName)) {
            rawName = Base64Wrapper.decodeString(rawName);
            try {
                compound.setTag("pet_names", JsonToNBT.parse(rawName).toList());
            } catch (NBTException e) {
                // Old pet name save... not supported in the new system
            }
        }

        String spawnedPets = results.getString("NeedsRespawn");
        if (Base64Wrapper.isEncoded(spawnedPets)) {
            spawnedPets = Base64Wrapper.decodeString(spawnedPets);
            StorageTagList pets = new StorageTagList();
            try {
                JsonToNBT parser = JsonToNBT.parse(spawnedPets);

                if (spawnedPets.startsWith("[")) {
                    // New system
                    parser.toList().getList().forEach(storageBase -> {
                        StorageTagCompound tag = (StorageTagCompound) storageBase;
                        if (!tag.hasKey("type")) {
                            if (tag.hasKey("data")) {
                                tag.setString("type", tag.getCompoundTag("data").getString("PetType"));
                                pets.appendTag(tag);
                            }
                            // Ignore the other values because it is not formatted correctly
                        } else {
                            pets.appendTag(storageBase);
                        }
                    });
                    compound.setTag("spawned_pets", pets);
                } else {
                    // Old system of saving 1 pet
                    StorageTagCompound tag = parser.toCompound();
                    compound.setTag("spawned_pets", pets.appendTag(new StorageTagCompound().setString("type", tag.getString("PetType")).setTag("data", tag)));
                }
            } catch (NBTException e) {
                // Old pet name save... not supported in the new system
            }
        }

        // Loading saved pets
        String savedPets = results.getString("SavedPets");
        if (Base64Wrapper.isEncoded(savedPets)) {
            savedPets = Base64Wrapper.decodeString(savedPets);
            StorageTagList pets = new StorageTagList();
            try {
                JsonToNBT parser = JsonToNBT.parse(savedPets);

                parser.toList().getList().forEach(storageBase -> {
                    StorageTagCompound tag = (StorageTagCompound) storageBase;
                    if (!tag.hasKey("type")) {
                        if (tag.hasKey("data")) {
                            tag.setString("type", tag.getCompoundTag("data").getString("PetType"));
                            pets.appendTag(tag);
                        }
                        // Ignore the other values because it is not formatted correctly
                    } else {
                        pets.appendTag(storageBase);
                    }
                });
                compound.setTag("saved_pets", pets);
            } catch (NBTException e) {
            }
        }

        return compound;
    }

    public CompletableFuture<StorageTagCompound> fetchData(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = implementConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tablePrefix + "_players WHERE uuid = ?")) {
                    statement.setString(1, uuid.toString());
                    ResultSet results = statement.executeQuery();
                    if (!results.next()) {
                        results.close();
                        return new StorageTagCompound();
                    }
                    try {
                        StorageTagCompound compound = rowToCompound(uuid, results, false);
                        results.close();
                        return compound;
                    } catch (NullPointerException | IllegalArgumentException ex) {
                        return new StorageTagCompound();
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return new StorageTagCompound();
            }
        }, PetCore.getInstance().async).thenApplyAsync(compound -> compound, PetCore.getInstance().sync);
    }

    public CompletableFuture<Object> uploadData(PetUser user) {
        StorageTagCompound compound = ((PetOwner) user).toCompound();

        return CompletableFuture.<Void>supplyAsync(() -> {
            Connection connection = implementConnection();
            isPlayerInDatabase(connection, user.getOwnerUUID()).thenApply(data -> {
                if (data) {
                    return updateData(connection, user.getOwnerUUID(), user.getOwnerName(), compound);
                } else {
                    return insertData(connection, user.getOwnerUUID(), user.getOwnerName(), compound);
                }
            });
            return null;
        }, PetCore.getInstance().async).thenApplyAsync(result -> result, PetCore.getInstance().sync);
    }

    public boolean uploadDataSync(PetUser user) {
        try (Connection connection = implementConnection()) {
            if (isPlayerInDatabaseSync(connection, user.getOwnerUUID())) {
                return updateDataSync(connection, user.getOwnerUUID(), user.getOwnerName(), ((PetOwner) user).toCompound());
            } else {
                return insertDataSync(connection, user.getOwnerUUID(), user.getOwnerName(), ((PetOwner) user).toCompound());
            }
        } catch (SQLException exception) {
            return false;
        }
    }

    /**
     * Sync methods should be used for shutdown only
     */
    public boolean updateDataSync(Connection connection, UUID uuid, String name, StorageTagCompound compound) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE `" + tablePrefix + "_players` SET " +
                    "name=?, UnlockedPets=?, PetName=?, NeedsRespawn=?, SavedPets=? WHERE uuid = ?");
            statement.setString(1, name);

            statement.setString(2, Base64Wrapper.encodeString(compound.getTag("owned_pets").toString()));
            statement.setString(3, Base64Wrapper.encodeString(compound.getTag("pet_names").toString()));
            statement.setString(4, Base64Wrapper.encodeString(compound.getTag("spawned_pets").toString()));
            statement.setString(5, Base64Wrapper.encodeString(compound.getTag("saved_pets").toString()));
            statement.setString(6, uuid.toString());
            statement.executeUpdate();
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean insertDataSync(Connection connection, UUID uuid, String name, StorageTagCompound compound) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `" + tablePrefix + "_players` " +
                    "(`uuid`, `name`, `UnlockedPets`, `PetName`, `NeedsRespawn`, `SavedPets`) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, uuid.toString());
            statement.setString(2, name);

            statement.setString(3, Base64Wrapper.encodeString(compound.getTag("owned_pets").toString()));
            statement.setString(4, Base64Wrapper.encodeString(compound.getTag("pet_names").toString()));
            statement.setString(5, Base64Wrapper.encodeString(compound.getTag("spawned_pets").toString()));
            statement.setString(6, Base64Wrapper.encodeString(compound.getTag("saved_pets").toString()));
            statement.executeUpdate();
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean isPlayerInDatabaseSync(Connection connection, UUID uuid) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM `" + tablePrefix + "_players` WHERE uuid = '" + uuid.toString() + "' LIMIT 1")) {

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException throwables) {
            return false;
        }
    }

    public void removeDuplicates(CommandSender sender) {
        List<String> list = new ArrayList<>();
        try (Connection connection = implementConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT `uuid`, COUNT(`uuid`) FROM `"+tablePrefix+"_players` GROUP BY `uuid` HAVING COUNT(`uuid`) > 1;");

            int size = 0;
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    ++size;
                    list.add(result.getString("uuid"));
                }
            }
            sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+ChatColor.GRAY+" Number of duplicate accounts found: "+size);

            if (list.isEmpty()) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+ChatColor.GRAY+" No duplicate entries found that needed to be deleted");
                return;
            }

            statement = connection.prepareStatement("DELETE FROM `" + tablePrefix + "_players` WHERE `uuid`=?");

            for (String string : list) {
                statement.setString(1, string);
                statement.addBatch();
            }

            sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+ ChatColor.GRAY+" Number of duplicate entries deleted: "+statement.executeBatch().length);
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void findDuplicates (Consumer<List<Triple<UUID, String, Integer>>> consumer) {
        CompletableFuture.runAsync(() -> {
            List<Triple<UUID, String, Integer>> list = new ArrayList<>();

            try (Connection connection  = implementConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT `uuid`,`name`, COUNT(`uuid`) FROM `"+tablePrefix+"_players` GROUP BY `uuid` HAVING COUNT(`uuid`) > 1;");

                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    list.add(Triple.of(UUID.fromString(result.getString(1)), result.getString(2), result.getInt(3)));
                }

                result.close();
                statement.close();
            }catch (SQLException ignored) {}

            new BukkitRunnable() {
                @Override
                public void run() {
                    consumer.accept(list);
                }
            }.runTask(SimplePets.getPlugin());
        });
    }

    public void removeNPCs(CommandSender sender) {
        List<String> list = new ArrayList<>();
        try (Connection connection = implementConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `" + tablePrefix + "_players` WHERE `uuid` LIKE '________-____-2___-____-____________';");

            int size = 0;
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    ++size;
                    list.add(result.getString("uuid"));
                }
            }
            sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+ChatColor.GRAY+" Number of NPC accounts found: "+size);

            if (list.isEmpty()) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+ChatColor.GRAY+" No entries found that needed to be deleted");
                return;
            }

            statement = connection.prepareStatement("DELETE FROM `" + tablePrefix + "_players` WHERE `uuid`=?");

            for (String string : list) {
                statement.setString(1, string);
                statement.addBatch();
            }

            sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+ ChatColor.GRAY+" Number of entries deleted: "+statement.executeBatch().length);
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public CompletableFuture<Boolean> updateData(Connection connection, UUID uuid, String name, StorageTagCompound compound) {
        // Prevents Offline UUIDs from being saved into the database
        if (uuid.version()  != 4) return CompletableFuture.completedFuture(false);
        return CompletableFuture.supplyAsync(() -> updateDataSync(connection, uuid, name, compound), PetCore.getInstance().async)
                .thenApplyAsync(result -> result, PetCore.getInstance().sync);
    }

    public CompletableFuture<Boolean> insertData(Connection connection, UUID uuid, String name, StorageTagCompound compound) {
        // Prevents Offline UUIDs from being saved into the database
        if (uuid.version()  != 4) return CompletableFuture.completedFuture(false);
        return CompletableFuture.supplyAsync(() -> insertDataSync(connection, uuid, name, compound), PetCore.getInstance().async)
                .thenApplyAsync(result -> result, PetCore.getInstance().sync);
    }

    public CompletableFuture<Boolean> isPlayerInDatabase(Connection connection, UUID uuid) {
        return CompletableFuture.supplyAsync(() -> isPlayerInDatabaseSync(connection, uuid), PetCore.getInstance().async)
                .thenApplyAsync(result -> result, PetCore.getInstance().sync);
    }
}
