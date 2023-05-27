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

    Connection implementConnection();

    void createTable();

    CompletableFuture<Boolean> sendPlayerData(UUID uuid, String name, StorageTagCompound compound);
    CompletableFuture<StorageTagCompound> fetchData(UUID uuid);
    CompletableFuture<Integer> getRowCount ();

    CompletableFuture<BiOptional<Integer, Integer>> removeDuplicates();
    CompletableFuture<BiOptional<Integer, Integer>> removeNPCs();
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
