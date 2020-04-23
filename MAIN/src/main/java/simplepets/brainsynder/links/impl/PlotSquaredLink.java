package simplepets.brainsynder.links.impl;

import lib.brainsynder.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.links.IPlotSquaredLink;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.ReflectionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.UUID;

public class PlotSquaredLink extends PluginLink implements IPlotSquaredLink {
    private Object ps = null;
    private Method plotArea, getPlot, hasOwner, isDenied;
    private Constructor locCon = null;
    private String packageName = "com.intellectualcrafters.plot";
    public PlotSquaredLink() {
        super("PlotSquared");
    }

    @Override public boolean onHook() {
        String main = "PS";
        Class<?> mainClass = null;
        ClassLoader loader = PetCore.get().getLoader();
        try {
            mainClass = Class.forName(packageName+"."+main, false, loader);
            // Server uses old P2 API
            PetCore.get().debug("Server is using the old PlotSquared API");
        }catch (Exception e) {
            main = "PlotSquared";
            packageName = "com.github.intellectualsites.plotsquared.plot";
            try {
                mainClass = Class.forName(packageName+"."+main, false, loader);
                // Server uses new P2 API
                PetCore.get().debug("Server is using the new PlotSquared API");
            }catch (Exception e2) {
                return false;
            }
        }
        ps = invoke(getMethod(mainClass, "get"), null);
        Class<?> loc = getClass("object.Location");

        plotArea = getMethod(mainClass, "getApplicablePlotArea", loc);
        getPlot = getMethod(getClass("object.PlotArea"), "getPlot", loc);
        hasOwner = getMethod(getClass("object.Plot"), "hasOwner");
        isDenied = getMethod(getClass("object.Plot"), "isDenied", UUID.class);
        locCon = ReflectionUtil.getConstructor(loc, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        return true;
    }
    @Override public void onUnhook() {
        ps = null;
        plotArea = null;
        getPlot = null;
        hasOwner = null;
        isDenied = null;
        locCon = null;
        super.onUnhook();
    }

    @Override
    public boolean allowPetEntry(PetOwner owner, Location at) {
        return fetchValue("Move", owner, at);
    }

    @Override
    public boolean allowPetSpawn(PetOwner owner, Location at) {
        return fetchValue("Spawn", owner, at);
    }

    @Override
    public boolean allowPetRiding(PetOwner owner, Location at) {
        return fetchValue("Riding", owner, at);
    }

    @Override
    public boolean allowPetEntry(Location location) {
        return allowPetEntry (null, location);
    }

    @Override
    public boolean allowPetSpawn(Location at) {
        return allowPetSpawn(null, at);
    }

    private boolean fetchValue (String section, PetOwner owner, Location at) {
        if (!isHooked()) return true;
        Object loc = Reflection.initiateClass(locCon, at.getWorld().getName(), at.getBlockX(), at.getBlockY(), at.getBlockZ());
        Object area = invoke(plotArea, ps, loc);
        if (area == null) return true;
        Object plot = invoke(getPlot, area, loc);

        if (owner != null) {
            if (owner.getPlayer().hasPermission(PetCore.get().getConfiguration().getString("PlotSquared.BypassPermission", false))) return true;
        }

        if (plot == null) {
            return PetCore.get().getConfiguration().getBoolean("PlotSquared.On-Roads."+section);
        }

        if (!(boolean) invoke(hasOwner, plot))
            return PetCore.get().getConfiguration().getBoolean("PlotSquared.On-Unclaimed-Plots."+section);

        if (PetCore.get().getConfiguration().getBoolean("PlotSquared.Block-If-Denied."+section) && (owner != null))
            return !(boolean)invoke(isDenied, plot, owner.getPlayer().getUniqueId());

        return true;
    }

    private Method getMethod (String className, String method, Class<?>... params) {
        try {
            return Reflection.getMethod(Class.forName(packageName+"."+className), method, params);
        }catch (Exception e){
            return null;
        }
    }

    private Method getMethod (Class<?> clazz, String method, Class<?>... params) {
        try {
            return Reflection.getMethod(clazz, method, params);
        }catch (Exception e){
            Bukkit.getLogger().severe("Could not get method '"+method+"' because: ");
            e.printStackTrace();
            return null;
        }
    }

    private Class<?> getClass (String className) {
        try {
            return Class.forName(packageName+"."+className);
        }catch (Exception e){
            Bukkit.getLogger().severe("Could not get class '"+className+"' because: ");
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
                Bukkit.getLogger().severe("Could not invoke method '"+method.getName()+"' because: ");
                e.printStackTrace();
                return null;
            }
        }
    }
}
