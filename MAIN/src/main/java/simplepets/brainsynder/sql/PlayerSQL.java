package simplepets.brainsynder.sql;

import com.google.common.collect.Maps;
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
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.impl.PetOwner;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PlayerSQL extends SQLManager {
    private static PlayerSQL instance;
    private final Map<UUID, StorageTagCompound> dataCache = Maps.newHashMap();

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
                PreparedStatement createTable = connection.prepareStatement(table);
                createTable.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            transferOldData();

            Map<UUID, StorageTagCompound> cache = new HashMap<>();
            try (Connection connection = implementConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM `" + tablePrefix + "_players`");
                ResultSet results = statement.executeQuery();
                while (results.next()) {
                    String uuid = results.getString("uuid");
                    try {
                        StorageTagCompound compound = new StorageTagCompound();

                        // Loads the pets the player purchased
                        String raw = results.getString("UnlockedPets");
                        try {
                            if (!raw.equals("W10=")) {
                                compound.setTag("owned_pets", JsonToNBT.parse(Base64Wrapper.decodeString(raw)).toList());
                            }
                        } catch (NBTException e) {
                            SimplePets.getDebugLogger().debug(DebugBuilder.build(getClass()).setMessages(
                                    "Failed to load 'UnlockedPets' for uuid: " + uuid,
                                    "Result: " + raw
                            ).setSync(true).setLevel(DebugLevel.ERROR));
                        }

                        // Loads pet names
                        String rawName = results.getString("PetName");
                        if (Base64Wrapper.isEncoded(rawName)) {
                            rawName = Base64Wrapper.decodeString(rawName);
                            try {
                                compound.setTag("pet_names", JsonToNBT.parse(rawName).toList());
                            } catch (NBTException e) {
                                SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Failed to read name data: " + rawName, true);
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

                        cache.put(UUID.fromString(uuid), compound);
                        // Cache the data...
                    } catch (NullPointerException | IllegalArgumentException ex) {
                        // Failed...
                    }
                }
                results.close();
                statement.close();

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        dataCache.putAll(cache);
                    }
                }.runTask(PetCore.getInstance());

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public StorageTagCompound getCache(UUID uuid) {
        return dataCache.getOrDefault(uuid, new StorageTagCompound());
    }

    @Override
    public void transferOldData() {
        // TODO: Need to transfer the old data from the files
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

    public CompletableFuture<StorageTagCompound> fetchData(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = implementConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tablePrefix + "_players WHERE uuid = ?");
                statement.setString(1, uuid.toString());
                ResultSet results = statement.executeQuery();
                if (!results.next()) {
                    results.close();
                    return new StorageTagCompound();
                }
                try {
                    StorageTagCompound compound = new StorageTagCompound();

                    // Loads the pets the player purchased
                    try {
                        String result = results.getString("UnlockedPets");
                        if (!result.equals("W10=")) {
                            compound.setTag("owned_pets", JsonToNBT.parse(Base64Wrapper.decodeString(results.getString("UnlockedPets"))).toList());
                        }
                    } catch (NBTException e) {
                        SimplePets.getDebugLogger().debug(DebugBuilder.build().setMessages(
                                "Failed to load 'UnlockedPets' for uuid: " + uuid,
                                "Result: " + results.getString("UnlockedPets")
                        ).setLevel(DebugLevel.ERROR));
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

                    results.close();
                    return compound;
                } catch (NullPointerException | IllegalArgumentException ex) {
                    return new StorageTagCompound();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return new StorageTagCompound();
            }
        }, PetCore.getInstance().async).thenApplyAsync(compound -> compound, PetCore.getInstance().sync);
    }

    public CompletableFuture<Object> uploadData(PetUser user) {
        return isPlayerInDatabase(user.getOwnerUUID()).thenApply(data -> {
            if (data) {
                return updateData(user);
            } else {
                return insertData(user);
            }
        });
    }


    public CompletableFuture<Boolean> updateData(PetUser user) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = implementConnection()) {
                PreparedStatement statement = connection.prepareStatement("UPDATE `" + tablePrefix + "_players` SET " +
                        "name=?, UnlockedPets=?, PetName=?, NeedsRespawn=?, SavedPets=? WHERE uuid = ?");
                statement.setString(1, user.getPlayer().getName());
                StorageTagCompound compound = ((PetOwner) user).toCompound();

                statement.setString(2, Base64Wrapper.encodeString(compound.getTag("owned_pets").toString()));
                statement.setString(3, Base64Wrapper.encodeString(compound.getTag("pet_names").toString()));
                statement.setString(4, Base64Wrapper.encodeString(compound.getTag("spawned_pets").toString()));
                statement.setString(5, Base64Wrapper.encodeString(compound.getTag("saved_pets").toString()));
                statement.setString(6, user.getPlayer().getUniqueId().toString());
                statement.executeUpdate();
                return true;
            } catch (SQLException exception) {
                exception.printStackTrace();
                return false;
            }
        }, PetCore.getInstance().async).thenApplyAsync(result -> result, PetCore.getInstance().sync);
    }

    public CompletableFuture<Boolean> insertData(PetUser user) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = implementConnection()) {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO `" + tablePrefix + "_players` " +
                        "(`uuid`, `name`, `UnlockedPets`, `PetName`, `NeedsRespawn`, `SavedPets`) VALUES (?, ?, ?, ?, ?, ?)");
                statement.setString(1, user.getPlayer().getUniqueId().toString());
                statement.setString(2, user.getPlayer().getName());

                StorageTagCompound compound = ((PetOwner) user).toCompound();
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
        }, PetCore.getInstance().async).thenApplyAsync(result -> result, PetCore.getInstance().sync);
    }

    public CompletableFuture<Boolean> isPlayerInDatabase(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = implementConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM `" + tablePrefix + "_players` WHERE uuid = ?");
                statement.setString(1, uuid.toString());
                ResultSet results = statement.executeQuery();
                boolean next = results.next();
                results.close();
                return next;
            } catch (SQLException exception) {
                exception.printStackTrace();
                return false;
            }
        }, PetCore.getInstance().async).thenApplyAsync(result -> result, PetCore.getInstance().sync);
    }
}
