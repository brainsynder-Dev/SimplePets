package simplepets.brainsynder.managers;

import anvil.brainsynder.AnvilGUI;
import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.event.user.PetRenameEvent;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
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
        AnvilGUI.Builder builder = new AnvilGUI.Builder().plugin(plugin);
        builder.itemLeft(new ItemBuilder(Material.NAME_TAG).withName(MessageFile.getTranslation(MessageOption.RENAME_ANVIL_TAG)).build());
        builder.onComplete((player, name) -> {
            if (name.equalsIgnoreCase("reset")) name = null;
            PetRenameEvent renameEvent = new PetRenameEvent (user, type, name);
            Bukkit.getPluginManager().callEvent(renameEvent);

            if (!renameEvent.isCancelled()) user.setPetName(renameEvent.getName(), type);
            return AnvilGUI.Response.close();
        }).title(MessageFile.getTranslation(MessageOption.RENAME_ANVIL_TITLE));
        builder.open(user.getPlayer());
    }

    public void renameViaChat (PetUser user, PetType type) {
        ConversationFactory factory = new ConversationFactory(PetCore.getInstance());
        factory.withLocalEcho(false)
                .withFirstPrompt(new PetRenamePrompt())
                .addConversationAbandonedListener(event -> {
                    if (event.gracefulExit()) {
                        String name = event.getContext().getSessionData("name").toString(); // It's a string prompt for a reason
                        if (name.equalsIgnoreCase("cancel")) {
                            user.getPlayer().sendMessage(MessageFile.getTranslation(MessageOption.RENAME_VIA_CHAT_CANCEL));
                            return;
                        }
                        if (name.equalsIgnoreCase("reset")) name = null;
                        PetRenameEvent renameEvent = new PetRenameEvent (user, type, name);
                        Bukkit.getPluginManager().callEvent(renameEvent);

                        if (!renameEvent.isCancelled()) user.setPetName(renameEvent.getName(), type);

                    }
                });
        factory.buildConversation(user.getPlayer()).begin();
    }

    public void renameViaSign (PetUser user, PetType type) {
        Plugin protocol = plugin.getServer().getPluginManager().getPlugin("ProtocolLib");
        if ((protocol == null) || (!protocol.isEnabled())) {
            if (RenameType.getType(ConfigOption.INSTANCE.RENAME_TYPE.getValue(), RenameType.ANVIL) == RenameType.SIGN)
                ConfigOption.INSTANCE.RENAME_TYPE.setValue(RenameType.ANVIL.name(), true);
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
