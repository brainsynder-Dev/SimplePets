package simplepets.brainsynder.storage.files;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.storage.files.base.StorageMaker;

import java.io.File;

public class PlayerStorage extends StorageMaker {
    public PlayerStorage(Player player) {
        super(new File(new File(PetCore.get().getDataFolder().toString() + "/PlayerData/"), player.getPlayer().getUniqueId().toString() + ".stc"));
        setString("username", player.getName());
        if (!hasKey("PetName")) setString("PetName", "null");
        if (!hasKey("NeedsRespawn")) setString("NeedsRespawn", "null");
        if (!hasKey("PurchasedPets")) setJSONArray("PurchasedPets", new JSONArray());
        save();
    }
}
