package simplepets.brainsynder.player;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.*;
import lib.brainsynder.nbt.other.NBTException;
import lib.brainsynder.utils.Base64Wrapper;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.database.MySQL;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.pet.TypeManager;
import simplepets.brainsynder.utils.DebugLevel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MySQLHandler {
    private final String SELECT_PETS = "SELECT * FROM `SimplePets` WHERE `UUID`=?";
    private final String UPDATE = "UPDATE `SimplePets` SET `UnlockedPets`=?, `PetName`=?, `NeedsRespawn`=?, `SavedPets`=? WHERE `UUID`=?";
    private final String INSERT = "INSERT INTO `SimplePets` (`UUID`, `name`, `UnlockedPets`, `PetName`, `NeedsRespawn`, `SavedPets`) VALUES(?,?,?,?,?,?)";
    private final PetCore core;
    private MySQL sql = null;

    public MySQLHandler(PetCore core, MySQL sql) {
        this.core = core;
        this.sql = sql;
    }

    public void save(PetOwner owner, boolean savePet) {
        if (sql == null) {
            core.debug(DebugLevel.ERROR, "Unable to save data to SQL, sql variable seems to be missing...", false);
            return;
        }
        Player player = owner.getPlayer();
        if (player == null) return;
        StorageTagCompound needsRespawn = new StorageTagCompound();
        try {
            if (owner.hasPet() && savePet) {
                needsRespawn = owner.pet.getEntity().asCompound();
            }
        } catch (Exception ignored) {}

        try {
            String name = player.getName(),
                    uuid = player.getUniqueId().toString(),
                    petName = owner.getPetName();

            if (petName == null || petName.isEmpty()) {
                petName = "empty";
            }

            StorageTagList list = new StorageTagList();
            owner.getOwnedPets().forEach(petType -> list.appendTag(new StorageTagString(petType.getName())));
            String finalPetName = petName;

            String finalNeedsRespawn = needsRespawn.toString();
            CompletableFuture.runAsync(() -> {
                try (Connection connection = sql.getSource().getConnection()) {
                    PreparedStatement select = connection.prepareStatement(SELECT_PETS);
                    select.setString(1, player.getUniqueId().toString());
                    ResultSet result = select.executeQuery();
                    if (result.next()) {
                        PreparedStatement update = connection.prepareStatement(UPDATE);
                        update.setString(1, Base64Wrapper.encodeString(list.toString())); // Owned Pets
                        update.setString(2, Base64Wrapper.encodeString(finalPetName)); // Pets Name
                        update.setString(3, Base64Wrapper.encodeString(finalNeedsRespawn)); // Have a Pet to respawn?
                        update.setString(4, Base64Wrapper.encodeString(owner.getSavedPetsArray().toString())); // Saved Pets
                        update.setString(5, uuid); // Player UUID
                        update.execute();
                        update.close();
                    } else {
                        PreparedStatement insert = connection.prepareStatement(INSERT);
                        insert.setString(1, uuid); // Player UUID
                        insert.setString(2, name); // Player Name
                        insert.setString(3, Base64Wrapper.encodeString(list.toString())); // Owned Pets
                        insert.setString(4, Base64Wrapper.encodeString(finalPetName)); // Pets Name
                        insert.setString(5, Base64Wrapper.encodeString(finalNeedsRespawn)); // Have a Pet to respawn?
                        insert.setString(6, Base64Wrapper.encodeString(owner.getSavedPetsArray().toString())); // Saved Pets
                        insert.execute();
                        insert.close();
                    }
                    select.close();
                    result.close();
                } catch (Exception e) {
                    PetCore.get().debug("Unable to save " + name + "'s Pet data.", false);
                    PetCore.get().debug("Data that failed to save: ", false);
                    PetCore.get().debug("- Name:" + name, false);
                    PetCore.get().debug("- UUID:" + uuid, false);
                    PetCore.get().debug("- PetName (Base64):" + Base64Wrapper.encodeString(finalPetName), false);
                    PetCore.get().debug("- PetData:" + finalNeedsRespawn, false);
                    PetCore.get().debug("- PurchasedPets (Base64):" + Base64Wrapper.encodeString(list.toString()), false);
                    PetCore.get().debug("- Error:", false);
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            PetCore.get().debug("Could not save " + player.getName() + "'s Pet data", false);
            e.printStackTrace();
        }

    }

    public void load(PetOwner owner, String name) {
        if (sql == null) {
            core.debug(DebugLevel.ERROR, "Unable to load data to SQL, sql variable seems to be missing...");
            return;
        }
        Player player = owner.getPlayer();
        if (player == null) return;

        CompletableFuture.runAsync(() -> {
            final JsonObject data = new JsonObject();
            try (Connection connection = sql.getSource().getConnection()) {
                PreparedStatement select = connection.prepareStatement(SELECT_PETS);
                select.setString(1, player.getUniqueId().toString()); // TODO
                ResultSet result = select.executeQuery();

                // Has Saved Data
                if (result.next()) {
                    data.add("UnlockedPets", Base64Wrapper.decodeString(result.getString("UnlockedPets")));
                    data.add("PetName", Base64Wrapper.decodeString(result.getString("PetName")));
                    data.add("NeedsRespawn", Base64Wrapper.decodeString(result.getString("NeedsRespawn")));
                    data.add("SavedPets", Base64Wrapper.decodeString(result.getString("SavedPets")));
                }

                select.close();
                result.close();
            } catch (Exception e) {
                PetCore.get().debug("Unable to load " + name + "'s Pet data.");
                e.printStackTrace();
            }


            new BukkitRunnable() { // run task to make it sync
                @Override
                public void run() {
                    TypeManager manager = PetCore.get().getTypeManager();
                    try {
                        if ((data.names().contains("UnlockedPets"))
                                && (data.get("UnlockedPets") != null)
                                && (!data.get("UnlockedPets").asString().isEmpty())) {
                            List<PetType> types = new ArrayList<>();
                            String pets = data.get("UnlockedPets").asString();
                            JsonToNBT nbt = JsonToNBT.parse(pets);
                            StorageTagList list;
                            if (pets.startsWith("[")) {
                                list = nbt.toList();
                            } else {
                                StorageTagCompound compound = nbt.toCompound();
                                list = compound.getTagList("StoredPets", compound.getTagId("StoredPets"));
                            }
                            list.getList().forEach(base -> {
                                StorageTagString string = (StorageTagString) base;
                                PetType type = manager.getType(string.getString());
                                if (type != null) types.add(type);
                            });
                            owner.setRawOwned(types);
                        }

                        if ((data.names().contains("PetName"))
                                && (data.get("PetName") != null)
                                && (!data.get("PetName").asString().isEmpty())) {
                            String name = data.get("PetName").asString();
                            if (!name.equals("empty")) owner.setRawPetName(name);
                        }

                        if ((data.names().contains("SavedPets"))
                                && (data.get("SavedPets") != null)
                                && (!data.get("SavedPets").asString().isEmpty())) {
                            StorageTagList list = JsonToNBT.parse(data.get("SavedPets").asString()).toList();
                            owner.updateSavedPets(list);
                        }

                        if ((data.names().contains("NeedsRespawn"))
                                && (data.get("NeedsRespawn") != null)
                                && (!data.get("NeedsRespawn").asString().isEmpty())
                                && (!data.get("NeedsRespawn").asString().equals("null")))
                            handle(owner, data.get("NeedsRespawn").asString());
                    } catch (Exception e) {
                        PetCore.get().debug("Could not retrieve " + player.getName() + "'s Pet data");
                        e.printStackTrace();
                    }
                }
            }.runTask(PetCore.get());
        });

    }

    private void handle(PetOwner owner, String json) {
        if ((!json.isEmpty()) && (!json.equals("null"))) {
            StorageTagCompound compound;
            try {
                compound = JsonToNBT.getTagFromJson(json);
            } catch (NBTException e) {
                compound = new StorageTagCompound();

                core.debug(DebugLevel.ERROR, "Failed to handle NeedsRespawn data : " + e.getMessage());
                StackTraceElement[] trace = e.getStackTrace();
                for (StackTraceElement traceElement : trace) core.debug(DebugLevel.ERROR, "at "+traceElement);
                Throwable cause = e.getCause();
                if (cause != null) {
                    handleThrowable(cause);
                }

            }
            owner.setPetToRespawn(compound);
        }
    }

    private void handleThrowable (Throwable throwable) {
        StackTraceElement[] trace = throwable.getStackTrace();
        int m = trace.length - 1;
        while (m >= 0) m--;
        // Print our stack trace
        for (int i = 0; i <= m; i++) core.debug(DebugLevel.ERROR, "at "+trace[i]);

        Throwable cause = throwable.getCause();
        if (cause != null) handleThrowable(cause);
    }
}
