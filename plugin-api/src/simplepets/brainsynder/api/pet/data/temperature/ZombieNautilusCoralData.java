package simplepets.brainsynder.api.pet.data.temperature;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityZombieNautilusPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.ZombieNautilusPetVariant;

@SupportedVersion(version = ServerVersion.v1_21_11)
@Namespace(namespace = "variant")
public class ZombieNautilusCoralData extends PetData<IEntityZombieNautilusPet> {
    public ZombieNautilusCoralData() {
        for (ZombieNautilusPetVariant type : ZombieNautilusPetVariant.values()) {
            addDefaultItem(type.name(), new ItemBuilder(Material.PLAYER_HEAD)
                    .withName("&#c8c8c8{name}: &a" + type.name())
                    .setTexture(type.getTexture()));
        }
    }

    @Override
    public Object getDefaultValue() {
        return ZombieNautilusPetVariant.TEMPERATE;
    }

    @Override
    public void onLeftClick(IEntityZombieNautilusPet entity) {
        entity.setVariant(ZombieNautilusPetVariant.getNext(entity.getVariant()));
    }

    @Override
    public Object value(IEntityZombieNautilusPet entity) {
        return entity.getVariant().name();
    }
}
