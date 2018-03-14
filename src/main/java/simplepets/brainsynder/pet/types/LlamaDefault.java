package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityLlamaPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class LlamaDefault extends PetDefault {
    public LlamaDefault(PetCore plugin) {
        super(plugin, "llama", SoundMaker.ENTITY_LLAMA_AMBIENT, EntityWrapper.LLAMA);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SKULL_ITEM).withData(3)
                .setTexture("http://textures.minecraft.net/texture/c2b1ecff77ffe3b503c30a548eb23a1a08fa26fd67cdff389855d74921368")
                .withName("&f&lLlama Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityLlamaPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.LLAMA;
    }
}
