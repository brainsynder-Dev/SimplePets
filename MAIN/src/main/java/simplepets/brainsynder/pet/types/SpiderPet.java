package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntitySpiderPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.AdditionalData;
import simplepets.brainsynder.wrapper.EntityWrapper;

@AdditionalData(passive = false)
public class SpiderPet extends PetType {
    public SpiderPet(PetCore plugin) {
        super(plugin, "spider", SoundMaker.ENTITY_SPIDER_AMBIENT, EntityWrapper.SPIDER);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SPIDER_EYE).withName("&f&lSpider Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntitySpiderPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.SILENT;
    }
}
