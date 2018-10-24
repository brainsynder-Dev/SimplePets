package simplepets.brainsynder.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import simple.brainsynder.commands.SubCommand;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.annotations.Permission;
import simplepets.brainsynder.pet.PetDefault;

import java.util.ArrayList;
import java.util.List;

public class PetSubCommand extends SubCommand {

    public boolean hasPermission (CommandSender sender) {
        if (getClass().isAnnotationPresent(Permission.class)){
            return sender.hasPermission(getPermission ());
        }
        return true;
    }

    @Override
    public boolean canExecute(CommandSender sender) {
        if (needsPermission()) return hasPermission(sender);
        return super.canExecute(sender);
    }

    public boolean needsPermission () {
        return getClass().isAnnotationPresent(Permission.class);
    }

    public String getPermission () {
        if (getClass().isAnnotationPresent(Permission.class)){
            return "Pet.commands." + getClass().getAnnotation(Permission.class).permission();
        }
        return null;
    }

    public List<String> getOnlinePlayers () {
        List<String> list = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(o -> list.add(o.getName()));
        return list;
    }

    public List<String> getPetTypes () {
        List<String> list = new ArrayList<>();
        for (PetDefault type : PetCore.get().getTypeManager().getTypes()) {
            if (!type.isEnabled()) continue;
            if (!type.isSupported()) continue;
            list.add(type.getConfigName());
        }
        Bukkit.broadcastMessage("Cached Types:"+list.size());
        return list;
    }
}
