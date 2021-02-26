package simplepets.brainsynder.menu;

import com.google.common.collect.Lists;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.menu.Icon;
import lib.brainsynder.menu.IconMenu;
import lib.brainsynder.menu.OptionClickEventHandler;
import lib.brainsynder.utils.Colorize;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.menu.items.list.NextPage;
import simplepets.brainsynder.menu.items.list.PreviousPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PetSelectorGUI {
    private final Map<String, Map<Integer, Map<Integer, PetType>>> pageCache = new HashMap<>();

    public void open (PetUser user, CustomInventory inventory, OptionClickEventHandler handler) {
        ((Player)user.getPlayer()).closeInventory();
        inventory.reset(user);

        IconMenu menu = new IconMenu(Colorize.translateBungeeHex(inventory.getTitle()), 54, 4, PetCore.getInstance());

        int page = 0;
        AtomicInteger index = new AtomicInteger(0);
        List<Integer> pages = new ArrayList<>();
        List<Integer> petSlots = Lists.newArrayList(
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44
        );

        for (IEntityPet entityPet : user.getPetEntities()) {
            PetType type = entityPet.getPetType();
            if (index.get() == petSlots.size()) {
                page++;
                index.set(0);
            }
            if (!pages.contains(page)) pages.add(page);

            int slot = petSlots.get(index.get());

            Map<Integer, Map<Integer, PetType>> cacheMap = pageCache.getOrDefault(user.getPlayer().getName(), new HashMap<>());
            Map<Integer, PetType> typeMap = cacheMap.getOrDefault(page, new HashMap<>());
            typeMap.put(slot, type);
            cacheMap.put(page, typeMap);
            pageCache.put(user.getPlayer().getName(), cacheMap);

            int finalPage = page;
            SimplePets.getPetConfigManager().getPetConfig(type).ifPresent(config -> {
                ItemBuilder builder = config.getBuilder().clone();
                builder.clearLore();

                Icon icon = new Icon(builder.build()).withHandler(handler);
                menu.setIcon(finalPage, slot, icon);
                index.incrementAndGet();
            });
        }

        for (Integer integer : pages) {
            if (integer != page) {
                // Next page item
                SimplePets.getItemHandler().getItem(NextPage.class).ifPresent(nextPage -> {
                    menu.setIcon(integer, 53, new Icon(nextPage.getItemBuilder().build()).withHandler(event -> menu.openNextPage()));
                });
            }

            if (integer != 0) {
                // Previous page item
                SimplePets.getItemHandler().getItem(PreviousPage.class).ifPresent(previousPage -> {
                    menu.setIcon(integer, 45, new Icon(previousPage.getItemBuilder().build()).withHandler(event -> menu.openPreviousPage()));
                });
            }
        }

        menu.open((Player) user.getPlayer());
    }

    public Map<Integer, Map<Integer, PetType>> getPageCache(PetUser user) {
        return pageCache.getOrDefault(user.getPlayer().getName(), new HashMap<>());
    }
}
