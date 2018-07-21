package simplepets.brainsynder.pet;

import org.apache.commons.lang.WordUtils;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.pet.types.*;

import java.text.Collator;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeSet;

public class TypeManager {
    private LinkedList<PetDefault> sortedItems;
    private LinkedHashMap<String, PetDefault> items;

    public TypeManager (PetCore plugin) {
        items = new LinkedHashMap<>();
        sortedItems = new LinkedList<> ();

        register(new ArmorStandDefault(plugin));
        register(new BatDefault(plugin));
        register(new BlazeDefault(plugin));
        register(new CaveSpiderDefault(plugin));
        register(new ChickenDefault(plugin));
        register(new CodDefault(plugin));
        register(new CowDefault(plugin));
        register(new CreeperDefault(plugin));
        register(new DrownedDefault(plugin));
        register(new DolphinDefault(plugin));
        register(new ElderGuardianDefault(plugin));
        register(new EndermanDefault(plugin));
        register(new EndermiteDefault(plugin));
        register(new EvokerDefault(plugin));
        register(new GhastDefault(plugin));
        register(new GiantDefault(plugin));
        register(new GuardianDefault(plugin));
        register(new HorseDefault(plugin));
        register(new HuskDefault(plugin));
        register(new IllusionerDefault(plugin));
        register(new IronGolemDefault(plugin));
        register(new LlamaDefault(plugin));
        register(new MagmaCubeDefault(plugin));
        register(new MooshroomDefault(plugin));
        register(new MuleDefault(plugin));
        register(new OcelotDefault(plugin));
        register(new ParrotDefault(plugin));
        register(new PhantomDefault(plugin));
        register(new PigDefault(plugin));
        register(new PigmanDefault(plugin));
        register(new PolarBearDefault(plugin));
        register(new PufferFishDefault(plugin));
        register(new RabbitDefault(plugin));
        register(new SalmonDefault(plugin));
        register(new SheepDefault(plugin));
        register(new ShulkerDefault(plugin));
        register(new SilverfishDefault(plugin));
        register(new SkeletonDefault(plugin));
        register(new SkeletonHorseDefault(plugin));
        register(new SlimeDefault(plugin));
        register(new SnowmanDefault(plugin));
        register(new SpiderDefault(plugin));
        register(new SquidDefault(plugin));
        register(new StrayDefault(plugin));
        register(new VexDefault(plugin));
        register(new VillagerDefault(plugin));
        register(new VindicatorDefault(plugin));
        register(new WitchDefault(plugin));
        register(new WitherDefault(plugin));
        register(new WitherSkeletonDefault(plugin));
        register(new WolfDefault(plugin));
        register(new ZombieDefault(plugin));
        register(new ZombieHorseDefault(plugin));
        register(new ZombieVillagerDefault(plugin));

        Collection<String> sortKeys = new TreeSet<>(Collator.getInstance());
        items.values().forEach(data -> sortKeys.add(data.getString("sort_key", false)));
        sortKeys.forEach(key -> sortedItems.add(fromSortKey(key)));
    }

    public void unLoad () {
        items.clear();
        items = null;
        sortedItems.clear();
        sortedItems = null;
    }

    private void register (PetDefault item) {
        if (!item.isSupported()) return;
        item.setDefault("sort_key", item.getConfigName());
        item.setDefault("sound", item.getDefaultSound().name());
        item.setDefault("display_name", "&a&l%player%'s " + item.getConfigName() + " Pet");
        item.setDefault("summon_name", WordUtils.capitalizeFully(item.getConfigName().replace("_", " ")));
        item.loadDefaults();
        item.save();
        item.reload();
        item.load();
        items.put(item.getConfigName(), item);
    }

    public PetDefault getType(ItemStack itemstack) {
        for (PetDefault item : items.values()) {
            if (item.getItemBuilder().isSimilar(itemstack)) return item;
        }
        return null;
    }

    private PetDefault fromSortKey(String key) {
        for (PetDefault item : items.values()) {
            if (item.getString("sort_key", false).equals(key)) return item;
        }
        return null;
    }

    public PetDefault getType(String name) {
        return items.getOrDefault(name, null);
    }

    public Collection<PetDefault> getTypes () {
        return sortedItems;
    }
}
