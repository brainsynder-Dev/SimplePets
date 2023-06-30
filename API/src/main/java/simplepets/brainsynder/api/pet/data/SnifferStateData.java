package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntitySnifferPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.SnifferState;

@Namespace(namespace = "state")
public class SnifferStateData extends PetData<IEntitySnifferPet> {
    public SnifferStateData() {
        for (SnifferState state : SnifferState.values()) {
            addDefaultItem(state.name(), new ItemBuilder(Material.PLAYER_HEAD)
                    .withName("&#c8c8c8{name}: &a"+state.name())
                    .setTexture(state.getTexture()));
        }
    }

    @Override
    public Object getDefaultValue() {
        return SnifferState.IDLING;
    }

    @Override
    public void onLeftClick(IEntitySnifferPet entity) {
        entity.setSnifferState(SnifferState.getNext(entity.getSnifferState()));
    }

    @Override
    public void onRightClick(IEntitySnifferPet entity) {
        entity.setSnifferState(SnifferState.getPrevious(entity.getSnifferState()));
    }

    @Override
    public Object value(IEntitySnifferPet entity) {
        return entity.getSnifferState();
    }
}
