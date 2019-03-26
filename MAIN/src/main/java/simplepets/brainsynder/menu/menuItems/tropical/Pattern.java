package simplepets.brainsynder.menu.menuItems.tropical;

import org.apache.commons.lang.WordUtils;
import org.json.simple.JSONArray;
import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityTropicalFishPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.TropicalPattern;

import java.util.ArrayList;
import java.util.List;

public class Pattern extends MenuItemAbstract {
    public Pattern(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    public Pattern(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("pattern", 0);

        if (getEntityPet() instanceof IEntityTropicalFishPet) {
            IEntityTropicalFishPet var = (IEntityTropicalFishPet) getEntityPet();

            TropicalPattern typeID = TropicalPattern.KOB;
            if (var.getPattern() != null) typeID = var.getPattern();
            TropicalPattern prev = TropicalPattern.getPrevious(typeID);
            TropicalPattern next = TropicalPattern.getNext(typeID);

            List<String> lore = new ArrayList<>();
            for (Object s : (JSONArray) item.toJSON().get("lore")) {
                String str = String.valueOf(s);
                lore.add(str.replace("%prev_state%", WordUtils.capitalize(prev.name().toLowerCase().replace("_", " ")))
                        .replace("%curr_state%", WordUtils.capitalize(typeID.name().toLowerCase().replace("_", " ")))
                        .replace("%next_state%", WordUtils.capitalize(next.name().toLowerCase().replace("_", " "))));
            }

            item.withLore(lore);
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        ItemBuilder item = ItemBuilder.getSkull(simple.brainsynder.utils.SkullType.PLAYER);
        item.setTexture("http://textures.minecraft.net/texture/36d149e4d499929672e2768949e6477959c21e65254613b327b538df1e4df");
        item.withName(" ");
        item.addLore("&6Previous: &7%prev_state%",
                "&6Current: &e%curr_state%",
                "&6Next: &7%next_state%");
        items.add(item);
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityTropicalFishPet) {
            IEntityTropicalFishPet var = (IEntityTropicalFishPet) entityPet;
            TropicalPattern wrapper = TropicalPattern.KOB;
            if (var.getPattern() != null) wrapper = var.getPattern();
            var.setPattern(TropicalPattern.getNext(wrapper));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IEntityTropicalFishPet) {
            IEntityTropicalFishPet var = (IEntityTropicalFishPet) entityPet;
            TropicalPattern wrapper = TropicalPattern.KOB;
            if (var.getPattern() != null) wrapper = var.getPattern();
            var.setPattern(TropicalPattern.getPrevious(wrapper));
        }
    }
}
