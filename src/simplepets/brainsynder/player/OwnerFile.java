package simplepets.brainsynder.player;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import simple.brainsynder.nbt.JsonToNBT;
import simple.brainsynder.nbt.NBTException;
import simple.brainsynder.nbt.StorageTagCompound;
import simple.brainsynder.utils.Base64Wrapper;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.database.MySQL;
import simplepets.brainsynder.files.PlayerFile;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OwnerFile {
    private static final String SELECT_PETS = "SELECT * FROM `SimplePets` WHERE `UUID`=?";
    private static final String UPDATE = "UPDATE `SimplePets` SET `UnlockedPets`=?, `PetName`=?, `NeedsRespawn`=? WHERE `UUID`=?";
    private static final String INSERT = "INSERT INTO `SimplePets` (`UUID`, `name`, `UnlockedPets`, `PetName`, `NeedsRespawn`) VALUES(?,?,?,?,?)";
    private PetOwner owner;

    OwnerFile(PetOwner owner) {
        this.owner = owner;
    }

    public void save() {
        String needsRespawn = ((!owner.hasPet()) ? "null" : Base64Wrapper.encodeString(owner.pet.getEntity().asCompound().toString()));
        final Player p = owner.getPlayer();
        PetOwner.ownerMap.remove(p.getUniqueId());
        if (PetCore.get().getConfiguration().isSet("MySQL.Enabled") && PetCore.get().getConfiguration().getBoolean("MySQL.Enabled")) {
            if (PetCore.get().isDisabling()) {
                PetCore.get().debug("Could not save " + p.getName() + "'s Pet information because they did not log out before the plugin disabled...");
                return;
            }
            try {
                new Thread(() -> {
                    String host = PetCore.get().getConfiguration().getString("MySQL.Host", false);
                    String port = PetCore.get().getConfiguration().getString("MySQL.Port", false);
                    String databaseName = PetCore.get().getConfiguration().getString("MySQL.DatabaseName", false);
                    String username = PetCore.get().getConfiguration().getString("MySQL.Login.Username", false);
                    String password = PetCore.get().getConfiguration().getString("MySQL.Login.Password", false);
                    MySQL sql = new MySQL(host, port, databaseName, username, password);
                    try {
                        sql.connectAutoClose(connection -> {
                            PreparedStatement select = connection.prepareStatement(SELECT_PETS);
                            select.setString(1, p.getUniqueId().toString());
                            ResultSet result = select.executeQuery();
                            if (result.next()) {
                                PreparedStatement update = connection.prepareStatement(UPDATE);
                                JSONObject obj = new JSONObject();
                                obj.put("StoredPets", owner.getOwnedPets());
                                update.setString(1, Base64Wrapper.encodeString(obj.toJSONString()));
                                update.setString(2, Base64Wrapper.encodeString(owner.getPetName()));
                                update.setString(3, needsRespawn);
                                update.setString(4, p.getUniqueId().toString());
                                update.execute();
                                update.close();
                            } else {
                                PreparedStatement insert = connection.prepareStatement(INSERT);
                                JSONObject obj = new JSONObject();
                                obj.put("StoredPets", owner.getOwnedPets());
                                insert.setString(1, p.getUniqueId().toString());
                                insert.setString(2, p.getName());
                                insert.setString(3, Base64Wrapper.encodeString(obj.toJSONString()));
                                insert.setString(4, Base64Wrapper.encodeString(owner.getPetName()));
                                insert.setString(5, needsRespawn);
                                insert.execute();
                                insert.close();
                            }
                        });
                    } catch (Exception e) {
                        PetCore.get().debug("Unable to save " + p.getName() + "'s Pet data.");
                    }
                }).run();
            } catch (Exception e) {
                PetCore.get().debug("Unable to save " + p.getName() + "'s Pet data.");
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
        if (!needsRespawn.equals("null")) {
            if (!PetCore.get().isDisabling()) {
                file.set("NeedsRespawn", needsRespawn);
                canSave = true;
            }
        }
        if (canSave) file.save();
    }

    void reload() {
        final Player p = owner.getPlayer();
        if (PetCore.get().getConfiguration().isSet("MySQL.Enabled") && PetCore.get().getConfiguration().getBoolean("MySQL.Enabled")) {
            if (PetCore.get().isDisabling()) {
                PetCore.get().debug("Could not reload " + p.getName() + "'s Pet information because the plugin is disabling...");
                return;
            }
            try {
                new Thread(() -> {
                    String host = PetCore.get().getConfiguration().getString("MySQL.Host", false);
                    String port = PetCore.get().getConfiguration().getString("MySQL.Port", false);
                    String databaseName = PetCore.get().getConfiguration().getString("MySQL.DatabaseName", false);
                    String username = PetCore.get().getConfiguration().getString("MySQL.Login.Username", false);
                    String password = PetCore.get().getConfiguration().getString("MySQL.Login.Password", false);
                    MySQL sql = new MySQL(host, port, databaseName, username, password);
                    try {
                        sql.connectAutoClose(connection -> {
                            PreparedStatement select = connection.prepareStatement(SELECT_PETS);
                            select.setString(1, p.getUniqueId().toString());
                            ResultSet result = select.executeQuery();
                            if (result.next()) {
                                String unlocked = Base64Wrapper.decodeString(result.getString("UnlockedPets"));
                                JSONObject obj = (JSONObject) JSONValue.parseWithException(unlocked);
                                JSONArray array = (JSONArray) obj.get("StoredPets");
                                owner.setRawOwned(array);
                                String name = Base64Wrapper.decodeString(result.getString("PetName"));
                                owner.setRawPetName(name);
                                handle(result.getString("NeedsRespawn"));
                            }
                        });
                    } catch (Exception e) {
                        PetCore.get().debug("Unable to save " + p.getName() + "'s Pet data.");
                    }
                }).run();
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
        String needs = file.getString("NeedsRespawn", false);
        handle(needs);
    }

    public void handle(String needs) {
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
}
