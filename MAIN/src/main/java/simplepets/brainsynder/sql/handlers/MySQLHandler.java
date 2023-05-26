package simplepets.brainsynder.sql.handlers;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.utils.Base64Wrapper;
import lib.brainsynder.utils.Triple;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.sql.SQLData;
import simplepets.brainsynder.sql.SQLHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQLHandler implements SQLHandler {
    @Override
    public Connection implementConnection() {
        Connection connection = null;
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mysql://").append(ConfigOption.INSTANCE.MYSQL_HOST.getValue()).append(":")
                .append(ConfigOption.INSTANCE.MYSQL_PORT.getValue()).append("/").append(SQLData.DATABASE_NAME);
        url.append("?useSSL=").append(ConfigOption.INSTANCE.MYSQL_SSL.getValue());
        url.append("&autoReconnect=true");

        try {
            connection = DriverManager.getConnection(url.toString(),
                    ConfigOption.INSTANCE.MYSQL_USERNAME.getValue(), ConfigOption.INSTANCE.MYSQL_PASSWORD.getValue());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    @Override
    public void createTable() {
        try (Connection connection = implementConnection()) {
            PreparedStatement statement = connection.prepareStatement(CREATE_TABLE);
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean setDataSync (UUID uuid, String name, StorageTagCompound compound) {
        try (Connection connection = implementConnection()) {
            // Checks if the uuid is in the MySQL database
            boolean hasEntry;
            {
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM `" + SQLData.TABLE_PREFIX + "_players` WHERE uuid = '" + uuid.toString() + "' LIMIT 1");
                ResultSet result = statement.executeQuery();
                hasEntry = result.next();
            }

            PreparedStatement statement;
            if (hasEntry) {
                statement = connection.prepareStatement("UPDATE `" + SQLData.TABLE_PREFIX + "_players` SET " +
                        "name=?, UnlockedPets=?, PetName=?, NeedsRespawn=?, SavedPets=? WHERE uuid = ?");
                statement.setString(1, name);

                statement.setString(2, Base64Wrapper.encodeString(compound.getTag("owned_pets").toString()));
                statement.setString(3, Base64Wrapper.encodeString(compound.getTag("pet_names").toString()));
                statement.setString(4, Base64Wrapper.encodeString(compound.getTag("spawned_pets").toString()));
                statement.setString(5, Base64Wrapper.encodeString(compound.getTag("saved_pets").toString()));
                statement.setString(6, uuid.toString());
            }else{
                statement = connection.prepareStatement("INSERT INTO `" + SQLData.TABLE_PREFIX + "_players` " +
                        "(`uuid`, `name`, `UnlockedPets`, `PetName`, `NeedsRespawn`, `SavedPets`) VALUES (?, ?, ?, ?, ?, ?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, name);

                statement.setString(3, Base64Wrapper.encodeString(compound.getTag("owned_pets").toString()));
                statement.setString(4, Base64Wrapper.encodeString(compound.getTag("pet_names").toString()));
                statement.setString(5, Base64Wrapper.encodeString(compound.getTag("spawned_pets").toString()));
                statement.setString(6, Base64Wrapper.encodeString(compound.getTag("saved_pets").toString()));
            }
            statement.executeUpdate();
            return true;
        }catch (SQLException exception) {
            return false;
        }
    }

    @Override
    public StorageTagCompound fetchData(UUID uuid) {
        StorageTagCompound compound = new StorageTagCompound();

        try (Connection connection = implementConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + SQLData.TABLE_PREFIX + "_players WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                try {
                    compound = rowToCompound(uuid, results, false);
                } catch (NullPointerException | IllegalArgumentException ignored) {}
            }

            results.close();
            statement.close();
        } catch (SQLException ignored) {}
        return compound;
    }

    @Override
    public int getRowCount () {
        int count = 0;

        try (Connection connection = implementConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM `" + SQLData.TABLE_PREFIX + "_players`");
            ResultSet result = statement.executeQuery();

            int size = 0;
            if (result.next()) size = result.getInt(1);

            count = size;

            statement.close();
        } catch (SQLException ignored) {}
        return count;
    }

    @Override
    public void removeDuplicates(CommandSender sender) {
        try (Connection connection = implementConnection()){

            PreparedStatement statement = connection.prepareStatement("SELECT `uuid`, COUNT(`uuid`) FROM `" + SQLData.TABLE_PREFIX + "_players` GROUP BY `uuid` HAVING COUNT(`uuid`) > 1;");

            List<String> list = new ArrayList<>();
            int size = 0;
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    ++size;
                    list.add(result.getString("uuid"));
                }
            }
            sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + ChatColor.GRAY + " Number of duplicate accounts found: " + size);

            if (list.isEmpty()) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + ChatColor.GRAY + " No duplicate entries found that needed to be deleted");
                return;
            }

            statement = connection.prepareStatement("DELETE FROM `" + SQLData.TABLE_PREFIX + "_players` WHERE `uuid`=?");

            for (String string : list) {
                statement.setString(1, string);
                statement.addBatch();
            }

            sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + ChatColor.GRAY + " Number of duplicate entries deleted: " + statement.executeBatch().length);
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void removeNPCs(CommandSender sender) {
        try (Connection connection = implementConnection()){

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `" + SQLData.TABLE_PREFIX + "_players` WHERE `uuid` LIKE '________-____-2___-____-____________';");

            List<String> list = new ArrayList<>();
            int size = 0;
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    ++size;
                    list.add(result.getString("uuid"));
                }
            }
            sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + ChatColor.GRAY + " Number of NPC accounts found: " + size);

            if (list.isEmpty()) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + ChatColor.GRAY + " No entries found that needed to be deleted");
                return;
            }

            statement = connection.prepareStatement("DELETE FROM `" + SQLData.TABLE_PREFIX + "_players` WHERE `uuid`=?");

            for (String string : list) {
                statement.setString(1, string);
                statement.addBatch();
            }

            sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + ChatColor.GRAY + " Number of entries deleted: " + statement.executeBatch().length);
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public List<Triple<UUID, String, Integer>> findDuplicates() {
        List<Triple<UUID, String, Integer>> list = new ArrayList<>();
        try (Connection connection = implementConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT `uuid`,`name`, COUNT(`uuid`) FROM `" + SQLData.TABLE_PREFIX + "_players` GROUP BY `uuid` HAVING COUNT(`uuid`) > 1;");

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                list.add(Triple.of(
                        UUID.fromString(result.getString(1)),
                        result.getString(2),
                        result.getInt(3)
                ));
            }

            statement.close();
        } catch (SQLException ignored) {}
        return list;
    }
}
