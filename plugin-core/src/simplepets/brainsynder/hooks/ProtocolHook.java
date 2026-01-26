package simplepets.brainsynder.hooks;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.event.user.PetRenameEvent;
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

    public static void renameViaSign(PetUser user, PetType type) {
        typeMap.put(user.getPlayer().getName(), type);
        List<String> layout = MessageFile.getFile().getStringList(MessageOption.RENAME_SIGN_TEXT.getPath());
        int index = 0;
        for (String line : layout) {
            if (line.trim().equalsIgnoreCase("{input}")) break;
            index++;
        }

        layout.add(index, " ");

        if (index >= 4)
            throw new IndexOutOfBoundsException("The layout for the sign gui must have 4 lines, and one of the lines must be for the {input}");

        int finalIndex = index;
        signMenuFactory
            // Does not support HEX colors
            .newMenu(layout)
            .reopenIfFail()
            .response((player, lines) -> {
                final String[] line = {lines[finalIndex]};
                if ((line[0] == null) || line[0].isEmpty()) return false;
                PetType petType = typeMap.getOrDefault(player.getName(), PetType.UNKNOWN);
                if (petType == PetType.UNKNOWN)
                    return false;  // failure. becaues reopenIfFail was called, menu will reopen when closed.
                PetCore.getInstance().getScheduler().getImpl().runNextTick(() -> {
                    SimplePets.getUserManager().getPetUser(player).ifPresent(user1 -> {
                        if (line[0].equalsIgnoreCase("reset")) line[0] = null;

                        PetRenameEvent renameEvent = new PetRenameEvent(user1, petType, line[0]);
                        Bukkit.getPluginManager().callEvent(renameEvent);

                        if (!renameEvent.isCancelled()) user1.setPetName(renameEvent.getName(), petType);
                    });
                });
                return true;
            }).open(user.getPlayer());
    }

    static {
        typeMap = Maps.newHashMap();
        signMenuFactory = new SignMenuFactory(PetCore.getInstance());
    }
}
