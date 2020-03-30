package simplepets.brainsynder.utils;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import simplepets.brainsynder.PetCore;

public class PetRenamePrompt extends StringPrompt {
    @Override
    public String getPromptText(ConversationContext context) {
        return PetCore.get().getMessages().getString("Pet-RenameViaChat", true);
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String answer) {
        context.setSessionData("name", answer);
        return null;
    }
}
