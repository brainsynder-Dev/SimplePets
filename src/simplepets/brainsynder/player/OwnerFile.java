package simplepets.brainsynder.player;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import simple.brainsynder.nbt.CompressedStreamTools;
import simple.brainsynder.nbt.JsonToNBT;
import simple.brainsynder.nbt.NBTException;
import simple.brainsynder.nbt.StorageTagCompound;
import simple.brainsynder.utils.Base64Wrapper;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.database.ConnectionPool;
import simplepets.brainsynder.database.MySQL;
import simplepets.brainsynder.files.PlayerFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;


public class OwnerFile {
    private MySQL sql = null;
    private static final String SELECT_PETS = "SELECT * FROM `SimplePets` WHERE `UUID`=?";
    private static final String UPDATE = "UPDATE `SimplePets` SET `UnlockedPets`=?, `PetName`=?, `NeedsRespawn`=? WHERE `UUID`=?";
    private static final String INSERT = "INSERT INTO `SimplePets` (`UUID`, `name`, `UnlockedPets`, `PetName`, `NeedsRespawn`) VALUES(?,?,?,?,?)";
    private PetOwner owner;

    OwnerFile(PetOwner owner) {
        this.owner = owner;
        sql = PetCore.get().getMySQL();
    }

    public void save() {
        String needsRespawn = ((!owner.hasPet()) ? "null" : Base64Wrapper.encodeString(owner.pet.getEntity().asCompound().toString()));
        final Player p = owner.getPlayer();
        if (p.hasMetadata("npc") || p.hasMetadata("NPC")) return;
        PetOwner.ownerMap.remove(p.getUniqueId());
        if (PetCore.get().getConfiguration().isSet("MySQL.Enabled") && PetCore.get().getConfiguration().getBoolean("MySQL.Enabled")) {
            if (PetCore.get().isDisabling()) {
                PetCore.get().debug("Could not save " + p.getName() + "'s Pet information because they did not log out before the plugin disabled...");
                return;
            }
            if (sql == null) {
                PetCore.get().debug(2, "Unable to save data to SQL, sql variable seems to be missing...");
                return;
            }

            try {
                String name = p.getName(),
                        uuid = p.getUniqueId().toString(),
                        petName = owner.getPetName();

                if (petName == null || petName.isEmpty()) {
                    petName = "empty";
                }

                JSONObject obj = new JSONObject();
                obj.put("StoredPets", owner.getOwnedPets());
                String finalPetName = petName;

                // The CompletableFuture.class is a a class that allows async operations (according to the docs)
                CompletableFuture.runAsync(() -> {
                    try {
                        ConnectionPool pool = sql.getPool();
                        Connection connection = pool.borrowConnection();
                        PreparedStatement select = connection.prepareStatement(SELECT_PETS);
                        select.setString(1, p.getUniqueId().toString());
                        ResultSet result = select.executeQuery();
                        if (result.next()) {
                            PreparedStatement update = connection.prepareStatement(UPDATE);
                            update.setString(1, Base64Wrapper.encodeString(obj.toJSONString()));
                            update.setString(2, Base64Wrapper.encodeString(finalPetName));
                            update.setString(3, needsRespawn);
                            update.setString(4, uuid);
                            update.execute();
                            update.close();
                        } else {
                            PreparedStatement insert = connection.prepareStatement(INSERT);
                            insert.setString(1, uuid);
                            insert.setString(2, name);
                            insert.setString(3, Base64Wrapper.encodeString(obj.toJSONString()));
                            insert.setString(4, Base64Wrapper.encodeString(finalPetName));
                            insert.setString(5, needsRespawn);
                            insert.execute();
                            insert.close();
                        }
                        select.close();
                        result.close();
                        pool.surrenderConnection(connection);
                    } catch (Exception e) {
                        PetCore.get().debug("Unable to save " + name + "'s Pet data.");
                        PetCore.get().debug("Data that failed to save: ");
                        PetCore.get().debug("- Name:" + name);
                        PetCore.get().debug("- UUID:" + uuid);
                        PetCore.get().debug("- PetName (Base64):" + Base64Wrapper.encodeString(finalPetName));
                        PetCore.get().debug("- PetData:" + needsRespawn);
                        PetCore.get().debug("- PurchasedPets (Base64):" + Base64Wrapper.encodeString(obj.toJSONString()));
                        PetCore.get().debug("- Error:");
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                PetCore.get().debug("Unable to save " + p.getName() + "'s Pet data.");
                PetCore.get().debug("- Error:");
                e.printStackTrace();
            }
            return;
        }

        boolean canSave = false;
        PlayerFile file = PetCore.get().getPlayerFile(p);
        if (!owner.getOwnedPets().isEmpty()) {
            file.set("PurchasedPets", owner.getOwnedPets());
            canSave = true;
        }
        if ((owner.getPetName() != null)
                && (!owner.getPetName().equals("null"))
                && (!owner.getPetName().equals("PetName"))) {
            file.set("PetName", owner.getPetName());
            canSave = true;
        }
        if (owner.hasPet()) {
            try {
                File save = getPetSave(p.getUniqueId().toString());
                if (!save.exists()) save.createNewFile();
                CompressedStreamTools.writeCompressed(owner.getPet().getEntity().asCompound(), new FileOutputStream(save));
            } catch (Exception ignored) {
            }
        }
        if (canSave) file.save();
    }

    void reload() {
        final Player p = owner.getPlayer();
        if (p.hasMetadata("npc") || p.hasMetadata("NPC")) return;
        if (PetCore.get().getConfiguration().isSet("MySQL.Enabled") && PetCore.get().getConfiguration().getBoolean("MySQL.Enabled")) {
            if (PetCore.get().isDisabling()) {
                PetCore.get().debug("Could not reload " + p.getName() + "'s Pet information because the plugin is disabling...");
                return;
            }
            if (sql == null) {
                PetCore.get().debug(2, "Unable to load data from SQL, sql variable seems to be missing...");
                return;
            }
            try {
                // The CompletableFuture.class is a a class that allows async operations (according to the docs)
                CompletableFuture<Data> future = CompletableFuture.supplyAsync(() -> {
                    Data data = new Data();
                    try {
                        ConnectionPool pool = sql.getPool();
                        Connection connection = pool.borrowConnection();

                        PreparedStatement select = connection.prepareStatement(SELECT_PETS);
                        select.setString(1, p.getUniqueId().toString());
                        ResultSet result = select.executeQuery();
                        if (result.next()) {
                            data.unlocked = (result.getString("UnlockedPets"));
                            data.name = (result.getString("PetName"));
                            data.needsRespawn = (result.getString("NeedsRespawn"));
                        }
                        select.close();
                        result.close();
                        pool.surrenderConnection(connection);
                    } catch (Exception e) {
                        PetCore.get().debug("Unable to load " + p.getName() + "'s Pet data.");
                    }
                    return data;
                });
                Data data = future.get();

                String unlocked = Base64Wrapper.decodeString(data.unlocked);
                JSONObject obj = (JSONObject) JSONValue.parseWithException(unlocked);
                JSONArray array = (JSONArray) obj.get("StoredPets");
                owner.setRawOwned(array);
                String name = Base64Wrapper.decodeString(data.name);
                if (!name.equals("empty")) owner.setRawPetName(name);
                handle(data.needsRespawn);
            } catch (Exception e) {
                PetCore.get().debug("Could not retrieve " + p.getName() + "'s Pet data");
            }
            return;
        }

        PlayerFile file = PetCore.get().getPlayerFile(p);
        try {
            owner.setRawOwned(file.getArray("PurchasedPets"));
        } catch (Exception e) {
            owner.setRawOwned(new JSONArray());
        }
        owner.setRawPetName(file.getString("PetName", false));
        StorageTagCompound compound = getPetData(p.getUniqueId().toString());
        if (compound.hasKey("PetType")) owner.setPetToRespawn(compound);
    }

    private void handle(String needs) {
        if (!needs.equals("null") && !needs.equals("NeedsRespawn")) {
            StorageTagCompound compound;
            try {
                compound = JsonToNBT.getTagFromJson(Base64Wrapper.decodeString(needs));
            } catch (NBTException e) {
                compound = new StorageTagCompound();
                e.printStackTrace();
            }
            if (compound.hasKey("PetType")) {
                owner.setPetToRespawn(compound);
            }
        }
    }

    // These new methods handle the Pet Saving for when MySQL is disabled.
    private StorageTagCompound getPetData(String uuid) {
        StorageTagCompound compound = new StorageTagCompound ();
        if (hasPetSave(uuid)) {
            try {
                File file = getPetSave(uuid);
                FileInputStream stream = new FileInputStream(file);
                compound = CompressedStreamTools.readCompressed(stream);
                file.delete();
            } catch (Exception ignored) {
            }
        }
        return compound;
    }

    private boolean hasPetSave(String uuid) {
        return getPetSave(uuid).exists();
    }

    private File getPetSave(String uuid) {
        return new File(getFolder(), uuid + ".stc");
    }

    private File getFolder() {
        File folder = new File(PetCore.get().getDataFolder().toString() + File.separator + "PetSaves");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    private class Data {
        String unlocked, name, needsRespawn;
    }
}
