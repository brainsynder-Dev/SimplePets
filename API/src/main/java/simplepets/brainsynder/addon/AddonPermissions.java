package simplepets.brainsynder.addon;

import com.google.common.collect.Maps;
import net.md_5.bungee.api.ChatColor;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.debug.DebugLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddonPermissions {
    private static final DebugLevel REGISTER;
    private static final Map<String, List<PermissionData>> permissions;

    static {
        permissions = Maps.newHashMap();
        REGISTER = new DebugLevel("Permissions", ChatColor.GRAY, ChatColor.GREEN);
    }

    /**
     * Registers permissions to be listed in the `/pet permissions` command
     *
     * @param addon The addon that the permission is for
     * @param data = The data for the permission
     */
    public static void register (PetAddon addon, PermissionData data) {
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
     * This contains the permissions and if they should be allowed by default,op, or none
     */
    public static Map<String, List<PermissionData>> getPermissions() {
        return permissions;
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
