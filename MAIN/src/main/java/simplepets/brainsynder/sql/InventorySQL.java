package simplepets.brainsynder.sql;

import lib.brainsynder.nbt.JsonToNBT;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.nbt.other.NBTException;
import lib.brainsynder.utils.Base64Wrapper;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.debug.DebugLevel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class InventorySQL extends SQLManager {
    private static InventorySQL instance;

    public InventorySQL () {
        super(true);
        instance = this;
    }

    public static InventorySQL getInstance() {
        return instance;
    }

    public void fetchRowCount(Consumer<Integer> consumer) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = implementConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM `" + tablePrefix + "_inventory`");
                ResultSet result = statement.executeQuery();
                int size = 0;
                if (usingSqlite) {
                    while (result.next()) size++;
                } else {
                    result = statement.executeQuery("SELECT COUNT(*) FROM `" + tablePrefix + "_inventory`");
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
        }, PetCore.getInstance().async);
    }

    @Override
    public void createTable() {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = implementConnection()) {
                PreparedStatement createTable = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " +
                        "`"+tablePrefix + "_inventory` " +
                        "(`uuid` VARCHAR(265) NOT NULL, `name` VARCHAR(265) NOT NULL, `inventory` "+getStupidTextThing()+" NOT NULL)");
                createTable.executeUpdate();

            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            transferOldData();
        }, PetCore.getInstance().async);
    }

    @Override
    public void transferOldData() {
    }

    public void uploadData (UUID uuid, StorageTagCompound compound) {
        isPlayerInDatabase(uuid).thenAccept(data -> {
            if (data) {
                updateData(uuid, compound).thenAccept(data1 -> {});
            }else{
                insertData(uuid, compound).thenAccept(data1 -> {});
            }
        });
    }

    public CompletableFuture<StorageTagCompound> fetchData(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = implementConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tablePrefix + "_inventory WHERE uuid = ?");
                statement.setString(1, uuid.toString());
                ResultSet results = statement.executeQuery();
                if (results.next()) {
                    try {
                        StorageTagCompound compound = new StorageTagCompound();

                        // Loads the pets the player purchased
                        try {
                            compound.setTag("item_storage", JsonToNBT.getTagFromJson(Base64Wrapper.decodeString(results.getString("inventory"))));
                        } catch (NBTException e) {
                            SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Failed to load 'inventory' for uuid: "+ uuid);
                            SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Result: "+results.getString("inventory"));
                        }
                        results.close();
                        return compound;
                    } catch (NullPointerException | IllegalArgumentException ex) {
                    }
                    results.close();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            return null;
        }, PetCore.getInstance().async).thenApplyAsync(result -> result, PetCore.getInstance().sync);
    }

    public CompletableFuture<Boolean> updateData(UUID uuid, StorageTagCompound compound) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = implementConnection()) {
                PreparedStatement statement = connection.prepareStatement("UPDATE " + tablePrefix + "_inventory SET inventory = ? WHERE uuid = ?");
                statement.setString(1, Base64Wrapper.encodeString(compound.toString()));
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
                return true;
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            return false;
        }, PetCore.getInstance().async).thenApplyAsync(result -> result, PetCore.getInstance().sync);
    }

    public CompletableFuture<Boolean> insertData(UUID uuid, StorageTagCompound compound) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = implementConnection()) {
                if (uuid == null) return false;
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO " + tablePrefix + "_inventory (uuid, name, inventory) VALUES (?, ?, ?)");

                statement.setString(1, uuid.toString());
                statement.setString(2, Bukkit.getOfflinePlayer(uuid).getName());
                statement.setString(3, Base64Wrapper.encodeString(compound.toString()));
                statement.executeUpdate();

                statement.close();
                return true;
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            return false;
        }, PetCore.getInstance().async).thenApplyAsync(result -> result, PetCore.getInstance().sync);
    }

    public CompletableFuture<Boolean> isPlayerInDatabase(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = implementConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tablePrefix + "_inventory WHERE uuid = ?");
                statement.setString(1, uuid.toString());
                ResultSet results = statement.executeQuery();
                return results.next();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            return false;
        }, PetCore.getInstance().async).thenApplyAsync(result -> result, PetCore.getInstance().sync);
    }
}
