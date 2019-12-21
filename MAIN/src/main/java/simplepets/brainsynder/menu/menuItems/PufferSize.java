package simplepets.brainsynder.menu.menuItems;

import org.apache.commons.lang.WordUtils;
import org.json.simple.JSONArray;
import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityPufferFishPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ValueType;
import simplepets.brainsynder.wrapper.PufferState;

import java.util.ArrayList;
import java.util.List;

@ValueType(def = "SMALL", target = "https://github.com/brainsynder-Dev/SimplePets/blob/master/MAIN/src/main/java/simplepets/brainsynder/wrapper/PufferState.java")
public class PufferSize extends MenuItemAbstract {
    public PufferSize(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    public PufferSize(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("puffersize", 0);

        if (getEntityPet() instanceof IEntityPufferFishPet) {
            IEntityPufferFishPet var = (IEntityPufferFishPet) getEntityPet();

            PufferState typeID = PufferState.SMALL;
            if (var.getPuffState() != null) typeID = var.getPuffState();
            PufferState prev = PufferState.getPrevious(typeID);
            PufferState next = PufferState.getNext(typeID);

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
        item.setTexture("http://textures.minecraft.net/texture/17152876bc3a96dd2a2299245edb3beef647c8a56ac8853a687c3e7b5d8bb");
        item.withName(" ");
        item.addLore("&6Previous: &7%prev_state%",
                "&6Current: &e%curr_state%",
                "&6Next: &7%next_state%");
        items.add(item);
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityPufferFishPet) {
            IEntityPufferFishPet var = (IEntityPufferFishPet) entityPet;
            PufferState wrapper = PufferState.SMALL;
            if (var.getPuffState() != null) wrapper = var.getPuffState();
            var.setPuffState(PufferState.getNext(wrapper));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IEntityPufferFishPet) {
            IEntityPufferFishPet var = (IEntityPufferFishPet) entityPet;
            PufferState wrapper = PufferState.SMALL;
            if (var.getPuffState() != null) wrapper = var.getPuffState();
            var.setPuffState(PufferState.getPrevious(wrapper));
        }
    }
}
