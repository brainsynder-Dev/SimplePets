package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityLlamaPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.LlamaColor;

@Namespace(namespace = "skin")
public class LlamaSkinData extends PetData<IEntityLlamaPet> {
    public LlamaSkinData() {
        for (LlamaColor color : LlamaColor.values()) {
            addDefaultItem(color.name(), new ItemBuilder(Material.PLAYER_HEAD)
                    .withName("&#c8c8c8{name}: &a"+color.name())
                    .setTexture(color.getTexture()));
        }
    }

    @Override
    public Object getDefaultValue() {
        return LlamaColor.CREAMY;
    }

    @Override
    public void onLeftClick(IEntityLlamaPet entity) {
        entity.setSkinColor(LlamaColor.getNext(entity.getSkinColor()));
    }

    @Override
    public void onRightClick(IEntityLlamaPet entity) {
        entity.setSkinColor(LlamaColor.getPrevious(entity.getSkinColor()));
    }

    @Override
    public Object value(IEntityLlamaPet entity) {
        return entity.getSkinColor().name();
    }
}
