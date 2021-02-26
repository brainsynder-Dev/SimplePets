package simplepets.brainsynder.managers;

import lib.brainsynder.anvil.AnvilGUI;
import lib.brainsynder.anvil.AnvilSlot;
import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.utils.RenameType;
import simplepets.brainsynder.utils.SignMenuFactory;

public class RenameManager {
    private final PetCore plugin;
    private final SignMenuFactory signMenuFactory;

    public RenameManager(PetCore plugin) {
        this.plugin = plugin;
        signMenuFactory = new SignMenuFactory(plugin);
    }

    public void renameViaAnvil (PetUser user, PetType type) {
        AnvilGUI gui = new AnvilGUI(plugin, (Player) user.getPlayer(), event -> user.setPetName(event.getName(), type));
        gui.setColorRename(true);
        gui.setTitle("&#de9790[] &#b35349Rename Pet");
        gui.setSlot(AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.NAME_TAG).withName("&#de9790NEW NAME").build());
        gui.open();
    }

    public void renameViaChat (PetUser user, PetType type) {
        ConversationFactory factory = new ConversationFactory(PetCore.getInstance());
        factory.withLocalEcho(false)
                .withFirstPrompt(new PetRenamePrompt())
                .addConversationAbandonedListener(event -> {
                    if (event.gracefulExit()) {
                        String name = event.getContext().getSessionData("name").toString(); // It's a string prompt for a reason
                        if (name.equalsIgnoreCase("cancel")) {
                            ((Player)user.getPlayer()).sendMessage(MessageFile.getTranslation(MessageOption.RENAME_VIA_CHAT_CANCEL));
                        } else if (name.equalsIgnoreCase("reset")) {
                            user.setPetName(null, type);
                        } else {
                            user.setPetName(name, type);
                        }
                    }
                });
        factory.buildConversation(((Player)user.getPlayer())).begin();
    }

    public void renameViaSign (PetUser user, PetType type) {
        Plugin protocol = plugin.getServer().getPluginManager().getPlugin("ProtocolLib");
        if ((protocol == null) || (!protocol.isEnabled())) {
            if (plugin.getConfiguration().getEnum("RenamePet.Type", RenameType.class) == RenameType.SIGN)
                plugin.getConfiguration().setEnum("RenamePet.Type", RenameType.ANVIL);
            return;
        }
        simplepets.brainsynder.hooks.ProtocolHook.renameViaSign(user, type);
    }

    private static class PetRenamePrompt extends StringPrompt {
        @Override
        public String getPromptText(ConversationContext context) {
            return MessageFile.getTranslation(MessageOption.RENAME_VIA_CHAT);
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String answer) {
            context.setSessionData("name", answer);
            return END_OF_CONVERSATION;
        }
    }
}
