package simplepets.brainsynder.storage.files;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.storage.files.base.JSONFile;

import java.io.File;

public class PlayerFile extends JSONFile {

    public PlayerFile(Player player) {
        super(new File(new File(PetCore.get().getDataFolder().toString() + "/PlayerData/"), player.getPlayer().getUniqueId().toString() + ".json"));
        defaults.put("Username", player.getPlayer().getName());
    }

    @Override
    public void loadDefaults() {
        defaults.put("PetName", "null");
        defaults.put("NeedsRespawn", "null");
        defaults.put("PurchasedPets", new JSONArray());
    }
}
