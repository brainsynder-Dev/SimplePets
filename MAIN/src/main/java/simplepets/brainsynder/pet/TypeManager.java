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
    private LinkedList<PetType> rawSort;
    private LinkedList<PetType> sortedItems;
    private LinkedHashMap<String, PetType> rawList;
    private LinkedHashMap<String, PetType> items;

    public TypeManager (PetCore plugin) {
        rawList = new LinkedHashMap<>();
        items = new LinkedHashMap<>();
        rawSort = new LinkedList<> ();
        sortedItems = new LinkedList<> ();

        register(new ArmorStandPet(plugin));
        register(new BatPet(plugin));
        register(new BeePet(plugin));
        register(new BlazePet(plugin));
        register(new CatPet(plugin));
        register(new CaveSpiderPet(plugin));
        register(new ChickenPet(plugin));
        register(new CodPet(plugin));
        register(new CowPet(plugin));
        register(new CreeperPet(plugin));
        register(new DrownedPet(plugin));
        register(new DolphinPet(plugin));
        register(new DonkeyPet(plugin));
        register(new ElderGuardianPet(plugin));
        register(new EndermanPet(plugin));
        register(new EndermitePet(plugin));
        register(new EvokerPet(plugin));
        register(new FoxPet(plugin));
        register(new GhastPet(plugin));
        register(new GiantPet(plugin));
        register(new GuardianPet(plugin));
        register(new HorsePet(plugin));
        register(new HuskPet(plugin));
        register(new IllusionerPet(plugin));
        register(new IronGolemPet(plugin));
        register(new LlamaPet(plugin));
        register(new MagmaCubePet(plugin));
        register(new MooshroomPet(plugin));
        register(new MulePet(plugin));
        register(new OcelotPet(plugin));
        register(new PandaPet(plugin));
        register(new ParrotPet(plugin));
        register(new PhantomPet(plugin));
        register(new PigPet(plugin));
        register(new PigmanPet(plugin));
        register(new PillagerPet(plugin));
        register(new PolarBearPet(plugin));
        register(new PufferFishPet(plugin));
        register(new RabbitPet(plugin));
        register(new RavagerPet(plugin));
        register(new SalmonPet(plugin));
        register(new SheepPet(plugin));
        register(new ShulkerPet(plugin));
        register(new SilverfishPet(plugin));
        register(new SkeletonPet(plugin));
        register(new SkeletonHorsePet(plugin));
        register(new SlimePet(plugin));
        register(new SnowmanPet(plugin));
        register(new SpiderPet(plugin));
        register(new SquidPet(plugin));
        register(new StrayPet(plugin));
        register(new TraderLlamaPet(plugin));
        register(new TropicalFishPet(plugin));
        register(new TurtlePet(plugin));
        register(new VexPet(plugin));
        register(new VillagerPet(plugin));
        register(new VindicatorPet(plugin));
        register(new WanderingTraderPet(plugin));
        register(new WitchPet(plugin));
        register(new WitherPet(plugin));
        register(new WitherSkeletonPet(plugin));
        register(new WolfPet(plugin));
        register(new ZombiePet(plugin));
        register(new ZombieHorsePet(plugin));
        register(new ZombieVillagerPet(plugin));

        register(new PiglinPet(plugin));
        register(new StriderPet(plugin));
        register(new HoglinPet(plugin));
        register(new ZoglinPet(plugin));
        register(new PigZombiePet(plugin));

        Collection<String> sortKeys = new TreeSet<>(Collator.getInstance());
        items.values().forEach(data -> sortKeys.add(data.getString("sort_key")));
        sortKeys.forEach(key -> sortedItems.add(fromSortKey(key)));

        Collection<String> rawSortKeys = new TreeSet<>(Collator.getInstance());
        rawList.values().forEach(data -> rawSortKeys.add(data.getConfigName()));
        rawSortKeys.forEach(key -> rawSort.add(fromSortKey(key)));
    }

    public void unLoad () {
        if (rawList != null) rawList.clear();
        rawList = null;
        if (items != null) items.clear();
        items = null;
        if (sortedItems != null) sortedItems.clear();
        sortedItems = null;
        if (rawSort != null) rawSort.clear();
        rawSort = null;
    }

    private void register (PetType item) {
        item.setDefault("sort_key", item.getConfigName());
        item.setDefault("sound", item.getDefaultSound().name());
        item.setDefault("display_name", "&a&l%player%'s " + item.getConfigName() + " Pet");
        item.setDefault("summon_name", WordUtils.capitalizeFully(item.getConfigName().replace("_", " ")));
        rawList.put(item.getConfigName(), item);
        if (!item.isSupported()) return;
        item.loadDefaults();
        item.save();
        item.reload();
        item.load();
        items.put(item.getConfigName(), item);
    }

    public PetType getType(ItemStack itemstack) {
        for (PetType item : items.values()) {
            if (item.getItemBuilder().isSimilar(itemstack)) return item;
        }
        return null;
    }

    private PetType fromSortKey(String key) {
        for (PetType item : items.values()) {
            if (item.getString("sort_key").equals(key)) return item;
        }
        for (PetType item : rawList.values()) {
            if (item.getConfigName().equals(key)) return item;
        }
        return null;
    }

    public PetType getType(String name) {
        return items.getOrDefault(name, rawList.getOrDefault(name, null));
    }

    public Collection<PetType> getTypes () {
        return sortedItems;
    }

    @Deprecated
    public LinkedList<PetType> getRawTypes() {
        return rawSort;
    }
}
