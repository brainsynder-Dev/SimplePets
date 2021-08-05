package simplepets.brainsynder.utils.animation;

import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;

import java.util.*;

public class AnimationCycle {
    private ArrayList<AnimationFrame> frames;
    private final HashSet<UUID> endless = new HashSet<>();
    private final Map<UUID, Boolean> toggle = new HashMap<>();
    private final Map<UUID, ArrayList<AnimationFrame>> framesMap = new HashMap<>();
    private BukkitTask runnable;

    public AnimationCycle(MovementFrames frames) {
        this.frames = frames.getFrames();
    }

    public ArrayList<AnimationFrame> getFrames() {
        return this.frames;
    }

    public void register(final IEntityArmorStandPet armor, final long delay) {
        UUID uuid = armor.getEntity().getUniqueId();
        framesMap.putIfAbsent(uuid, frames);
        toggle.putIfAbsent(uuid, true);
        if (!endless.contains(uuid)) {
            this.endless.add(uuid);
            runnable = new BukkitRunnable() {
                int i = 0;

                @Override
                public void run() {
                    if (!endless.contains(uuid)) {
                        this.cancel();
                        return;
                    }
                    if (!framesMap.containsKey(uuid)) {
                        this.cancel();
                        return;
                    }
                    if (!toggle.containsKey(uuid)) {
                        this.i = 0;
                        return;
                    }
                    if (!toggle.get(uuid)) {
                        this.i = 0;
                        return;
                    }
                    ArrayList<AnimationFrame> frames = framesMap.get(uuid);
                    if (this.i == frames.size()) {
                        this.i = 0;
                    }

                    try {
                        frames.get(this.i).setLocations(armor);
                    } catch (Exception ignored) {
                    }
                    ++this.i;

                }
            }.runTaskTimerAsynchronously(PetCore.getInstance(), 0, delay);
        }
    }

    public boolean isRunning(IEntityArmorStandPet stand) {
        UUID uuid = stand.getEntity().getUniqueId();
        if (!endless.contains(uuid)) return false;
        if (!toggle.containsKey(uuid)) return false;
        return toggle.get(uuid);
    }

    public boolean isRegistered(IEntityArmorStandPet stand) {
        return endless.contains(stand.getEntity().getUniqueId());
    }

    public void toggle(IEntityArmorStandPet armor, boolean var) {
        toggle.put(armor.getEntity().getUniqueId(), var);
    }

    public void cancelTask(ArmorStand armor) {
        runnable.cancel();
        List<UUID> remove = new ArrayList<>();
        for (UUID uuid : endless) {
            if (armor.getUniqueId().equals(uuid)) {
                remove.add(uuid);
            }
        }
        this.endless.removeAll(remove);

    }
}