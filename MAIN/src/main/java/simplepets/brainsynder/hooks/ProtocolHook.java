package simplepets.brainsynder.hooks;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.utils.SignMenuFactory;

import java.util.List;
import java.util.Map;

public class ProtocolHook {
    private static final Map<String, PetType> typeMap;
    private static final SignMenuFactory signMenuFactory;

    public static void renameViaSign (PetUser user, PetType type) {
        typeMap.put(user.getPlayer().getName(), type);
        List<String> layout = MessageFile.getFile().getStringList(MessageOption.RENAME_SIGN_TEXT.getPath());
        int index = 0;
        for (String line : layout) {
            if (line.trim().equalsIgnoreCase("{input}")) break;
            index++;
        }

        layout.add(index, " ");

        if (index >= 4) throw new IndexOutOfBoundsException("The layout for the sign gui must have 4 lines, and one of the lines must be for the {input}");

        int finalIndex = index;
        signMenuFactory
                // Does not support HEX colors
                .newMenu(layout)
                .reopenIfFail()
                .response((player, lines) -> {
                    String line = lines[finalIndex];
                    if ((line == null) || line.isEmpty()) return false;
                    PetType petType = typeMap.getOrDefault(player.getName(), PetType.UNKNOWN);
                    if (petType == PetType.UNKNOWN) return false;  // failure. becaues reopenIfFail was called, menu will reopen when closed.
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            SimplePets.getUserManager().getPetUser(player).ifPresent(user1 -> user1.setPetName(line.trim(), petType));
                        }
                    }.runTask(PetCore.getInstance());
                    return true;
                }).open((Player) user.getPlayer());
    }

    static {
        typeMap = Maps.newHashMap();
        signMenuFactory = new SignMenuFactory(PetCore.getInstance());
    }
}
