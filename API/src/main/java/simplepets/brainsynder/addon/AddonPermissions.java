package simplepets.brainsynder.addon;

import com.google.common.collect.Maps;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.debug.DebugLevel;

import java.util.*;

public class AddonPermissions {
    private static final DebugLevel REGISTER;
    private static final Map<String, List<PermissionData>> permissions;
    private static final Map<String, Map<PermissionData, List<PermissionData>>> parentPermissions;

    static {
        permissions = Maps.newHashMap();
        parentPermissions = Maps.newHashMap();
        REGISTER = new DebugLevel("Permissions", ChatColor.GRAY, ChatColor.GREEN);
    }

    /**
     * Will check if the sender has a permission, it will also check the registered parent permissions
     *
     * @param addon The addon that the permission is for
     * @param sender The CommandSender being checked
     * @param permission The permission in question
     * @return
     *      true | They have permission
     *      false | They do not have permission
     */
    public static boolean hasPermission (PetModule addon, CommandSender sender, String permission) {
        if (sender.isOp()) return true;
        String name = addon.getNamespace().namespace();
        Map<PermissionData, List<PermissionData>> map = parentPermissions.getOrDefault(name, new HashMap<>());
        for (Map.Entry<PermissionData, List<PermissionData>> entry : map.entrySet()) {
            for (PermissionData data : entry.getValue()) {
                // The permission is a child permission
                if (data.getPermission().equalsIgnoreCase(permission)) {
                    // The sender has the parent permission
                    if (sender.hasPermission(entry.getKey().getPermission())) return true;
                }
            }
        }

        return sender.hasPermission(permission);
    }

    /**
     * Registers permissions to be listed in the `/pet permissions` command
     *
     * @param addon The addon that the permission is for
     * @param data = The data for the permission
     */
    public static void register (PetModule addon, PermissionData data) {
        Objects.requireNonNull(addon);
        Objects.requireNonNull(data);
        String name = addon.getNamespace().namespace();

        List<PermissionData> list = permissions.getOrDefault(name, new ArrayList<>());
        if (list.contains(data)) return;

        list.add(data);
        permissions.put(addon.getNamespace().namespace(), list);
        SimplePets.getDebugLogger().debug(REGISTER, "Registering '"+data.getPermission()+"' permission for the '"+addon.getNamespace().namespace()+"' addon");
    }

    /**
     * Registers permissions to be listed in the `/pet permissions` command
     *
     * @param addon The addon that the permission is for
     * @param data = The data for the permission
     */
    public static void register (PetModule addon, PermissionData parentPermission, PermissionData data) {
        Objects.requireNonNull(addon);
        Objects.requireNonNull(parentPermission);
        Objects.requireNonNull(data);
        String name = addon.getNamespace().namespace();

        Map<PermissionData, List<PermissionData>> map = parentPermissions.getOrDefault(name, new HashMap<>());
        List<PermissionData> children = map.getOrDefault(parentPermission, new ArrayList<>());
        if (children.isEmpty()) {
            SimplePets.getDebugLogger().debug(REGISTER, "Registering '"+parentPermission.getPermission()+"' permission and its children permissions for the '"+addon.getNamespace().namespace()+"' addon");
        }

        if (!children.contains(data)) {
            children.add(data);
            map.put(parentPermission, children);
            parentPermissions.put(name, map);
        }
    }

    /**
     * This contains the permissions and if they should be allowed by default,op, or none
     */
    public static Map<String, List<PermissionData>> getPermissions() {
        return permissions;
    }

    public static Map<String, Map<PermissionData, List<PermissionData>>> getParentPermissions() {
        return parentPermissions;
    }

    public enum AllowType {
        /**
         * Will not be given by default
         */
        NONE ("false"),
        /**
         * Will allow players to have this permission by default
         */
        DEFAULT ("true"),
        /**
         * Will allow OPs to have access to this permission by default (even tho they normally do :P)
         */
        OP("op");

        private final String allow;
        AllowType(String allow) {
            this.allow = allow;
        }

        @Override
        public String toString() {
            return allow;
        }
    }
}
