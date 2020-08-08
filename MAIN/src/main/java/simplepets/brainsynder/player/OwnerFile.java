package simplepets.brainsynder.player;

import lib.brainsynder.json.Json;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.nbt.*;
import lib.brainsynder.utils.Base64Wrapper;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.pet.TypeManager;
import simplepets.brainsynder.storage.files.PlayerStorage;
import simplepets.brainsynder.utils.DebugLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles how the players pet data is saved/loaded
 *
 * <br>
 *
 * 'PetName' - Name of players pet, saved as {@link String} and is Base64 encoded
 * 'UnlockedPets' -
 *      'PurchasedPets' - Old name that saved the data as a {@link JSONArray} that was Base64 encoded
 * 'NeedsRespawn' -
 * 'SavedPets'
 * 'ItemStorage' -
 *
 *
 */
public class OwnerFile {
    private MySQLHandler handler = null;
    private final PetOwner owner;

    OwnerFile(PetOwner owner) {
        this.owner = owner;
        handler = PetCore.get().getSqlHandler();
    }

    public void save() {
        save(true, false);
    }

    public void save(boolean savePet, boolean disabling) {
        final Player p = owner.getPlayer();
        if (p == null) return;
        if (p.hasMetadata("npc") || p.hasMetadata("NPC")) return;

        if (savePet) {
            if (PetCore.get().getConfiguration().getBoolean("MySQL.Enabled", false)) {
                if (PetCore.get().isDisabling()) {
                    PetCore.get().debug("Could not save " + p.getName() + "'s Pet information because they did not log out before the plugin disabled...", false);
                    return;
                }

                if (handler == null) {
                    PetCore.get().debug(DebugLevel.ERROR, "Unable to save data to SQL, sql variable seems to be missing...", false);
                    return;
                }

                handler.save(owner, savePet);
                return;
            }
        }

        PetCore.get().getPlayerStorage(p, !disabling, file -> {
            boolean canSave = false;
            if (!owner.getOwnedPets().isEmpty()) {
                StorageTagList list = new StorageTagList();
                owner.getOwnedPets().forEach(pet -> list.appendTag(new StorageTagString(pet.getName())));
                file.setTag("PurchasedPets", list);
                canSave = true;
            }
            if ((!savePet) && !owner.getSavedPetsArray().getList().isEmpty()) {
                file.setTag("SavedPets", owner.getSavedPetsArray());
                canSave = true;
            }
            if ((owner.getPetName() != null)
                    && (!owner.getPetName().equals("null"))
                    && (!owner.getPetName().equals("PetName"))) {
                file.setString("PetName", owner.getPetName());
                canSave = true;
            }

            // This will only save if it is not saving pet data
            if ((!savePet) && (owner.getStoredInventory() != null)) {
                file.setTag("ItemStorage", owner.getStoredInventory());
                canSave = true;
            }
            // This will only save if it is saving pet data
            if (savePet && owner.hasPet()) {
                file.setTag("NeedsRespawn", owner.getPet().getEntity().asCompound());
                canSave = true;
            }
            if (canSave) file.save();
        });

    }

    void reload() {
        final Player p = owner.getPlayer();
        if (p == null) return;
        if (p.hasMetadata("npc") || p.hasMetadata("NPC")) return;
        if (PetCore.get().getConfiguration().getBoolean("MySQL.Enabled", false)) {
            if (PetCore.get().isDisabling()) {
                PetCore.get().debug("Could not reload " + p.getName() + "'s Pet information because the plugin is disabling...");
                return;
            }

            if (handler == null) {
                PetCore.get().debug(DebugLevel.ERROR, "Unable to load data from SQL, sql variable seems to be missing...");
                return;
            }

            handler.load(owner, p.getName());
            return;
        }

        PetCore.get().getPlayerStorage(p, false, new PetCore.Call<PlayerStorage>() {
            @Override
            public void call(PlayerStorage file) {
                TypeManager manager = PetCore.get().getTypeManager();
                if (file.hasKey("PurchasedPets")) {
                    List<PetType> types = new ArrayList<>();
                    if (file.getTag("PurchasedPets") instanceof StorageTagList) {
                        // Was saved as StorageTagList
                        StorageTagList stored = (StorageTagList) file.getTag("PurchasedPets");
                        stored.getList().forEach(base -> types.add(manager.getType(((StorageTagString) base).getString())));
                    } else {
                        // Was saved in the old format
                        String raw = file.getString("PurchasedPets");
                        String decoded = Base64Wrapper.decodeString(raw);
                        JsonArray array = (JsonArray) Json.parse(decoded);
                        array.values().forEach(jsonValue -> {
                            String name = jsonValue.asString();
                            types.add(manager.getType(name));
                        });
                    }

                    owner.setRawOwned(types);
                }
                if (file.hasKey("ItemStorage")) {
                    StorageTagCompound compound;
                    if (file.getTag("ItemStorage") instanceof StorageTagString) {
                        try {
                            compound = JsonToNBT.getTagFromJson(Base64Wrapper.decodeString(file.getString("ItemStorage")));
                        } catch (NBTException e) {
                            compound = null;
                        }
                    }else{
                        compound = file.getCompoundTag("ItemStorage");
                    }

                    if ((compound != null) && (!compound.hasNoTags())) owner.setStoredInventory(compound, false);
                }
                if (file.hasKey("SavedPets")) {
                    StorageTagList stored = new StorageTagList();
                    if (file.getTag("SavedPets") instanceof StorageTagList) {
                        // Was saved as StorageTagList
                        stored = (StorageTagList) file.getTag("SavedPets");
                    }else{
                        // Was saved in the old format
                        String raw = file.getString("SavedPets");
                        if (Base64Wrapper.isEncoded(raw)) {
                            String decoded = Base64Wrapper.decodeString(raw);
                            try {
                                StorageTagList rawList = JsonToNBT.parse(decoded).toList();
                                for (StorageBase storageBase : rawList.getList()) {
                                    StorageTagString string = (StorageTagString) storageBase;

                                    String rawCompound;
                                    if (Base64Wrapper.isEncoded(string.getString())) {
                                        rawCompound = Base64Wrapper.decodeString(string.getString());
                                    }else{
                                        rawCompound = string.getString();
                                    }

                                    stored.appendTag(JsonToNBT.getTagFromJson(rawCompound));
                                }
                            } catch (NBTException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (!stored.getList().isEmpty()) owner.updateSavedPets(stored);
                }
                if (file.hasKey("NeedsRespawn")) {
                    StorageTagCompound compound = file.getCompoundTag("NeedsRespawn");
                    file.remove("NeedsRespawn");
                    file.save();
                    if (compound.hasKey("PetType")) owner.setPetToRespawn(compound);
                }
                if (file.hasKey("PetName")) owner.setRawPetName(file.getString("PetName"));
            }

            @Override
            public void onFail() {
                owner.setRawOwned(new ArrayList<>());
            }
        });

    }

    private JSONObject parse(String string) {
        if (string.equals("null")) return new JSONObject();
        try {
            return (JSONObject) JSONValue.parseWithException(Base64Wrapper.decodeString(string));
        } catch (ParseException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    private void handle(String json) {
        if ((!json.isEmpty()) && (!json.equals("null"))) {
            StorageTagCompound compound;
            try {
                compound = JsonToNBT.getTagFromJson(Base64Wrapper.decodeString(json));
            } catch (NBTException e) {
                compound = new StorageTagCompound();
                e.printStackTrace();
            }
            owner.setPetToRespawn(compound);
        }
    }
}
