package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.hostile.IEntityPufferFishPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.PufferState;

@Namespace(namespace = "size")
public class PufferSizeData extends PetData<IEntityPufferFishPet> {
    public PufferSizeData() {
        for (PufferState state : PufferState.values()) {
            addDefaultItem(state.name(), new ItemBuilder(Material.PLAYER_HEAD)
                    .withName("&#c8c8c8{name}: &a"+state.name())
                    .setTexture("http://textures.minecraft.net/texture/17152876bc3a96dd2a2299245edb3beef647c8a56ac8853a687c3e7b5d8bb"));
        }
    }

    @Override
    public Object getDefaultValue() {
        return PufferState.SMALL;
    }

    @Override
    public void onLeftClick(IEntityPufferFishPet entity) {
        entity.setPuffState(PufferState.getNext(entity.getPuffState()));
    }

    @Override
    public Object value(IEntityPufferFishPet entity) {
        return entity.getPuffState().name();
    }
}
