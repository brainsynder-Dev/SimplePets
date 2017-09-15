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
import simplepets.brainsynder.errors.SimplePetsException;
import simplepets.brainsynder.files.PlayerFile;
import simplepets.brainsynder.pet.PetType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OwnerFile {
    private static final String SELECT_PETS = "SELECT * FROM `SimplePets` WHERE `UUID`=?";
    private static final String UPDATE = "UPDATE `SimplePets` SET `UnlockedPets`=?, `PetName`=?, `NeedsRespawn`=? WHERE `UUID`=?";
    private static final String INSERT = "INSERT INTO `SimplePets` (`UUID`, `name`, `UnlockedPets`, `PetName`, `NeedsRespawn`) VALUES(?,?,?,?)";
    private PetOwner owner;
    private StorageTagCompound tagCompound = null;
    private PetType needsRespawn = null;

    OwnerFile(PetOwner owner) {
        this.owner = owner;
    }

    public void save() {
        String needsRespawn = ((!owner.hasPet()) ? "null" : Base64Wrapper.encodeString(owner.pet.getEntity().asCompound().toString()));
        final Player p = owner.getPlayer();
        PetOwner.ownerMap.remove(p.getUniqueId());
        if (PetCore.get().getConfiguration().isSet("MySQL.Enabled")) {
            if (PetCore.get().getConfiguration().getBoolean("MySQL.Enabled")) {
                if (PetCore.get().isDisabling()) {
                    PetCore.get().debug("Could not save " + p.getName() + "'s Pet information because they did not log out before the plugin disabled...");
                    return;
                }
                try {
                    new Thread(() -> {
                        Connection connection = null;
                        try {
                            connection = PetCore.get().getDataSource().getConnection();
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
                        } catch (Exception e) {
                            try {
                                connection.rollback();
                            } catch (SQLException e1) {
                                throw new SimplePetsException("Could not Rollback the Connection Cause:" + e1.getMessage(), e1);
                            }
                        }
                    }).run();
                } catch (Exception e) {
                    PetCore.get().debug("Unable to save " + p.getName() + "'s Pet data.");
                }
                return;
            }
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
        if (PetCore.get().getConfiguration().isSet("MySQL.Enabled")) {
            if (PetCore.get().getConfiguration().getBoolean("MySQL.Enabled")) {
                if (PetCore.get().isDisabling()) {
                    PetCore.get().debug("Could not reload " + p.getName() + "'s Pet information because the plugin is disabling...");
                    return;
                }
                try {
                    new Thread(() -> {
                        Connection connection = null;
                        try {
                            connection = PetCore.get().getDataSource().getConnection();
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
                            result.close();
                        } catch (Exception ignored) {
                            try {
                                connection.rollback();
                            } catch (SQLException e1) {
                                throw new SimplePetsException("Could not Rollback the Connection Cause:" + e1.getMessage(), e1);
                            }
                        }
                    }).run();
                } catch (Exception e) {
                    PetCore.get().debug("Could not retrieve " + p.getName() + "'s Pet data");
                }
                return;
            }
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
                String name = String.valueOf(compound.getString("PetType"));
                needsRespawn = PetType.valueOf(name);
                this.tagCompound = compound;
            }
        }
    }

    public void respawnPet() {
        try {
            if (needsRespawn != null) {
                PlayerFile file = PetCore.get().getPlayerFile(owner.player);
                file.set("NeedsRespawn", "null");
                needsRespawn.setPet(owner.player);
                owner.getPet().getVisableEntity().applyCompound(tagCompound);
                needsRespawn = null;
                tagCompound = null;
            }
        } catch (Exception ignored) {
        }
    }
}
