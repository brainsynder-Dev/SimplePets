package simplepets.brainsynder.storage.files;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.storage.files.base.StorageMaker;

import java.io.File;

public class PlayerStorage extends StorageMaker {
    public PlayerStorage(Player player) {
        super(new File(new File(PetCore.get().getDataFolder().toString() + File.separator+"PlayerData" + File.separator), player.getPlayer().getUniqueId().toString() + ".stc"));
        setString("username", player.getName());
        if (!hasKey("PetName")) setString("PetName", "null");
        if (!hasKey("NeedsRespawn")) setString("NeedsRespawn", "null");
        if (!hasKey("PurchasedPets")) setJSONArray("PurchasedPets", new JSONArray());
        if (!hasKey("ItemStorage")) setJSONObject("ItemStorage", new JSONObject());
        save();
    }

    public PlayerStorage(File file) {
        super(file);
    }
}
