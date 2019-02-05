package simplepets.brainsynder.links.impl.worldguard.old;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import simple.brainsynder.utils.Reflection;
import simplepets.brainsynder.links.impl.worldguard.WGInterface;

import java.lang.reflect.Method;

public class WGOld implements WGInterface {
    private Object instance;
    private Method regionMethod, setMethod;

    @Override
    public boolean initiate() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if ((plugin == null) || (!plugin.isEnabled())) return false;
        Class<?> wg = getClass("com.sk89q.worldguard.bukkit.WorldGuardPlugin");
        instance = invoke(getMethod(wg, "inst"), null);
        regionMethod = getMethod(wg, "getRegionManager", World.class);
        setMethod = getMethod("com.sk89q.worldguard.protection.managers.RegionManager", "getApplicableRegions", Location.class);
        return (instance != null) && (regionMethod != null) && (setMethod != null);
    }

    @Override
    public void cleanup() {
        instance = null;
        regionMethod = null;
        setMethod = null;
    }

    @Override
    public RegionManager getRegionManager(World world) {
        Object invoked = invoke(regionMethod, instance, world);
        return (RegionManager) invoked;
    }

    @Override
    public ApplicableRegionSet getRegionSet(Location location) {
        Object invoked = invoke(setMethod, getRegionManager(location.getWorld()), location);
        return (ApplicableRegionSet) invoked;
    }

    private Method getMethod (String className, String method, Class<?>... params) {
        try {
            return Reflection.getMethod(Class.forName(className), method, params);
        }catch (Exception e){
            return null;
        }
    }

    private Method getMethod (Class<?> clazz, String method, Class<?>... params) {
        try {
            return Reflection.getMethod(clazz, method, params);
        }catch (Exception e){
            Bukkit.getLogger().severe("Count not get method '"+method+"' because: ");
            e.printStackTrace();
            return null;
        }
    }

    private Class<?> getClass (String className) {
        try {
            return Class.forName(className);
        }catch (Exception e){
            Bukkit.getLogger().severe("Count not get class '"+className+"' because: ");
            e.printStackTrace();
            return null;
        }
    }

    private Object invoke(Method method, Object instance, Object... parameters) {
        if (method == null) {
            Bukkit.getLogger().severe("Method can not be null");
            return null;
        } else {
            try {
                return method.invoke(instance, parameters);
            } catch (Exception e) {
                Bukkit.getLogger().severe("Count not invoke method '"+method.getName()+"' because: ");
                e.printStackTrace();
                return null;
            }
        }
    }
}
