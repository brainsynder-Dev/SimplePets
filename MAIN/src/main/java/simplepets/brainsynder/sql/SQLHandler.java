package simplepets.brainsynder.sql;

import lib.brainsynder.nbt.JsonToNBT;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.nbt.StorageTagList;
import lib.brainsynder.nbt.other.NBTException;
import lib.brainsynder.optional.BiOptional;
import lib.brainsynder.utils.Base64Wrapper;
import lib.brainsynder.utils.Triple;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.debug.DebugLevel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface SQLHandler {
    String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + SQLData.TABLE_PREFIX + "_players (" +
            "`uuid` VARCHAR(265) NOT NULL," +
            "`name` VARCHAR(265) NOT NULL," +
            "`UnlockedPets` " + getStupidTextThing() + " NOT NULL," +
            "`PetName` " + getStupidTextThing() + " NOT NULL," +
            "`NeedsRespawn` " + getStupidTextThing() + " NOT NULL," +
            "`SavedPets` " + getStupidTextThing() + " NOT NULL" +
            ")";

    /**
     * This function returns an implementation of the Connection interface in Java.
     *
     * @return A connection object is being returned.
     */
    Connection implementConnection();

    void initiateDatabase();

    /**
     * This function sends player data, identified by UUID and name, along with a StorageTagCompound object, and returns a
     * CompletableFuture<Boolean>.
     *
     * @param uuid UUID stands for Universally Unique Identifier. It is a 128-bit value that is used to identify
     * information in computer systems. In this case, it is used to identify a specific player in the game.
     * @param name The name parameter is a String that represents the name of the player whose data is being sent.
     * @param compound `compound` is a `StorageTagCompound` object that represents a collection of key-value pairs, similar
     * to a dictionary or a map. It is commonly used in Minecraft modding to store and retrieve data associated with game
     * entities or items. In this context, it likely contains data related to a player
     * @return The method `sendPlayerData` returns a `CompletableFuture<Boolean>`. This is a Java class that represents a
     * future result of an asynchronous computation that will eventually produce a `Boolean` value. The `Boolean` value
     * indicates whether the player data was successfully sent or not.
     */
    CompletableFuture<Boolean> sendPlayerData(UUID uuid, String name, StorageTagCompound compound);

    /**
     * This function is used to send player data synchronization with a UUID, name, and storage
     * tag compound.
     *
     * @param uuid The UUID (Universally Unique Identifier) of the player whose data is being sent. This is a unique
     * identifier assigned to each player by Minecraft.
     * @param name The name parameter is a String that represents the name of the player whose data is being synchronized.
     * @param compound The "compound" parameter is likely a StorageTagCompound object, which is a data structure used to
     * store and manipulate NBT (Named Binary Tag) data. NBT is a format used by Minecraft to store various types of game
     * data, such as player inventories, entity data, and world information
     * @return The method is marked as deprecated, which means it is no longer recommended to use it. However, based on the
     * method signature, it appears that it used to return a boolean value indicating whether the player data
     * synchronization was successful or not.
     *
     * @Deprecated Might be removed in favor of a different way
     */
    @Deprecated
    boolean sendPlayerDataSync(UUID uuid, String name, StorageTagCompound compound);

    /**
     * The function returns a CompletableFuture object that fetches a StorageTagCompound object associated with a given
     * UUID.
     *
     * @param uuid UUID stands for Universally Unique Identifier. It is a 128-bit value used for identifying information in
     * computer systems. In this context, the UUID is likely being used to identify a specific user or entity for which
     * data is being fetched.
     * @return A `CompletableFuture` object that will eventually contain a `StorageTagCompound` object after the data for
     * the specified UUID has been fetched.
     */
    CompletableFuture<StorageTagCompound> fetchData(UUID uuid);

    /**
     * The function returns a CompletableFuture object that will eventually contain an integer representing the row count.
     *
     * @return A `CompletableFuture` object that will eventually contain an `Integer` value representing the row count. The
     * `CompletableFuture` is a way to perform asynchronous programming in Java, allowing the caller to continue executing
     * other tasks while waiting for the row count to be calculated.
     */
    CompletableFuture<Integer> getRowCount ();

    /**
     * The function returns a CompletableFuture that removes duplicates from a pair of integers and returns them as a
     * BiOptional.
     *
     * @return The method `removeDuplicates()` is returning a `CompletableFuture` that will eventually hold a `BiOptional`
     * object containing two `Integer` values.
     */
    CompletableFuture<BiOptional<Integer, Integer>> removeDuplicates();

    /**
     * The function `removeNPCs()` returns a `CompletableFuture` that resolves to a `BiOptional` object containing two
     * integers.
     *
     * @return The method `removeNPCs()` is returning a `CompletableFuture` that will eventually hold a `BiOptional` object
     * containing two `Integer` values.
     */
    CompletableFuture<BiOptional<Integer, Integer>> removeNPCs();

    /**
     * The function returns a CompletableFuture that finds and returns a list of Triple objects containing UUID, String,
     * and Integer values representing duplicates.
     *
     * @return The method `findDuplicates()` returns a `CompletableFuture` that will eventually contain a `List` of
     * `Triple` objects. Each `Triple` contains a `UUID`, a `String`, and an `Integer`. The `List` will contain all the
     * duplicates found in the data.
     */
    CompletableFuture<List<Triple<UUID, String, Integer>>> findDuplicates ();

    /**
     * This function returns either "TEXT" or "LONGTEXT" depending on the value of SQLData.USE_SQLITE.
     *
     * @return The method `getStupidTextThing()` returns a `String` value. The value returned depends on the value of the
     * `USE_SQLITE` constant in the `SQLData` class. If `USE_SQLITE` is `true`, then the method returns `"TEXT"`, otherwise
     * it returns `"LONGTEXT"`.
     */
    static String getStupidTextThing() {
        return SQLData.USE_SQLITE ? "TEXT" : "LONGTEXT";
    }

    /**
     * This Java function loads various pet-related data from a SQL database and stores it in a StorageTagCompound.
     *
     * @param uuid The UUID of a player whose data is being loaded from a database.
     * @param results A ResultSet object containing the data retrieved from the database query.
     * @param syncLogs A boolean value indicating whether to synchronize the logging output. If set to true, the
     * logging output will be synchronized across multiple threads.
     * @return A StorageTagCompound object.
     */
    default StorageTagCompound rowToCompound(UUID uuid, ResultSet results, boolean syncLogs) throws SQLException {
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
}
