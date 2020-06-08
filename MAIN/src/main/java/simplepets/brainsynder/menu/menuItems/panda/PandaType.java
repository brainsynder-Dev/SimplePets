package simplepets.brainsynder.menu.menuItems.panda;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import org.apache.commons.lang.WordUtils;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityPandaPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;
import simplepets.brainsynder.wrapper.PandaGene;

import java.util.ArrayList;
import java.util.List;

@SupportedVersion(version = ServerVersion.v1_14_R1)
@ValueType(def = "NORMAL", target = "https://github.com/brainsynder-Dev/SimplePets/blob/master/MAIN/src/main/java/simplepets/brainsynder/wrapper/PandaGene.java")
public class PandaType extends MenuItemAbstract<IEntityPandaPet> {

    public PandaType(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public PandaType(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder builder = type.getDataItemByName(getTargetName(), 0);

        if (entityPet instanceof IEntityPandaPet) {
            IEntityPandaPet panda = entityPet;
            builder = type.getDataItemByName(getTargetName(), panda.getGene().ordinal());
        }
        builder.withName(formatName(builder, (entity, name) -> {
            name = name.replace("%value%", WordUtils.capitalize(entity.getGene().name().toLowerCase()));
            return name;
        }));
        return builder;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        for (PandaGene gene : PandaGene.values()) {
            items.add(gene.getIcon()
                    .withName("&6&lPanda Gene: &e"+ WordUtils.capitalize(gene.name().toLowerCase())));
        }
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityPandaPet) {
            IEntityPandaPet panda = entityPet;
            panda.setGene(PandaGene.getNext(panda.getGene()));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IEntityPandaPet) {
            IEntityPandaPet panda = entityPet;
            panda.setGene(PandaGene.getPrevious(panda.getGene()));
        }
    }

    @Override
    public boolean isSupported() {
        return ServerVersion.isEqualNew(ServerVersion.v1_14_R1);
    }

    @Override
    public String getTargetName() {
        return "gene";
    }
}
