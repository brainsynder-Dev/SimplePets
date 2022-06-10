package simplepets.brainsynder.api.pet.data.warden;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.hostile.IEntityWardenPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.AngerLevel;

@Namespace(namespace = "anger-level")
public class WardenAngerData extends PetData<IEntityWardenPet> {
    public WardenAngerData() {
        for (AngerLevel level : AngerLevel.values()) {
            addDefaultItem(level.name(), new ItemBuilder(Material.PLAYER_HEAD)
                    .withName("&#c8c8c8{name}: &a"+level.name())
                    .setTexture("http://textures.minecraft.net/texture/1dfd13ca08bf973bfef0293d770704a11ef5a9fe20d40671fb066724d3e18d8"));
        }
    }

    @Override
    public Object getDefaultValue() {
        return AngerLevel.CALM;
    }

    @Override
    public void onLeftClick(IEntityWardenPet entity) {
        entity.setAngerLevel(AngerLevel.getNext(entity.getAngerLevel()));
    }

    @Override
    public void onRightClick(IEntityWardenPet entity) {
        entity.setAngerLevel(AngerLevel.getPrevious(entity.getAngerLevel()));
    }

    @Override
    public Object value(IEntityWardenPet entity) {
        return entity.getAngerLevel();
    }
}
