package simplepets.brainsynder.managers;

import lib.brainsynder.anvil.AnvilGUI;
import lib.brainsynder.anvil.AnvilSlot;
import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.event.user.PetRenameEvent;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.utils.RenameType;

public class RenameManager {
    private final PetCore plugin;

    public RenameManager(PetCore plugin) {
        this.plugin = plugin;
    }

    public void renameViaAnvil (PetUser user, PetType type) {
        AnvilGUI gui = new AnvilGUI(plugin, (Player) user.getPlayer(), event -> {
            String name = event.getName();
            if (name.equalsIgnoreCase("reset")) name = null;
            PetRenameEvent renameEvent = new PetRenameEvent (user, type, name);
            Bukkit.getPluginManager().callEvent(renameEvent);

            if (!renameEvent.isCancelled()) user.setPetName(renameEvent.getName(), type);
        });
        gui.setColorRename(true);
        gui.setTitle(MessageFile.getTranslation(MessageOption.RENAME_ANVIL_TITLE));
        gui.setSlot(AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.NAME_TAG).withName(MessageFile.getTranslation(MessageOption.RENAME_ANVIL_TAG)).build());
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
                            return;
                        }
                        if (name.equalsIgnoreCase("reset")) name = null;
                        PetRenameEvent renameEvent = new PetRenameEvent (user, type, name);
                        Bukkit.getPluginManager().callEvent(renameEvent);

                        if (!renameEvent.isCancelled()) user.setPetName(renameEvent.getName(), type);

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
