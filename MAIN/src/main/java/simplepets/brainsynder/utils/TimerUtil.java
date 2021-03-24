package simplepets.brainsynder.utils;

import lib.brainsynder.files.JsonFile;
import lib.brainsynder.json.JsonObject;
import simplepets.brainsynder.PetCore;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TimerUtil {
    private static final Map<String, Long> startTimeMap = new HashMap<>();
    private static final LinkedHashMap<String, LinkedHashMap<String, Long>> storedTimeMap = new LinkedHashMap<>();



    /**
     * Finds how many Milliseconds it took to run a task
     *
     * @param clazz    - Class where the task is being called from
     * @param taskName - A name to give the task (use the same name for start/finish)
     */
    public static long findDelay(Class clazz, String taskName) {
        LinkedHashMap<String, Long> map = storedTimeMap.getOrDefault(clazz.getSimpleName(), new LinkedHashMap<>());
        String key = clazz.getSimpleName() + "|" + taskName;
        if (startTimeMap.containsKey(key)) {
            long start = startTimeMap.get(key);
            long end = System.nanoTime();
            long diff = (end - start) / 1000000;

            // Will log what class and how long the task(s) took.
            startTimeMap.remove(key);
            map.put(taskName, diff);
            storedTimeMap.put(clazz.getSimpleName(), map);
            return diff;
        }
        map.put(taskName, -1L);
        storedTimeMap.put(clazz.getSimpleName(), map);
        startTimeMap.put(key, System.nanoTime());
        return 0;
    }

    public static void outputTimings () {
        File file = new File(PetCore.get().getDataFolder(), "Timings.json");
        if (file.exists()) file.delete();
        JsonFile json = new JsonFile(file, true) {
            @Override
            public void loadDefaults() {

            }
        };
        storedTimeMap.forEach((className, map) -> {
            JsonObject object = new JsonObject();
            map.forEach((task, diff) -> object.add(task, diff+"ms"));
            json.set(className, object);
        });
        json.save();
    }
}
