package simplepets.brainsynder.menu.items.list;

import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.player.PetOwner;

import java.io.File;

public class Data extends Item {
    public Data(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return ItemBuilder.getSkull(simple.brainsynder.utils.SkullType.PLAYER)
                .withName("&cPet Data")
                .addLore("&7Click Here to open","&7the Pet Data Menu")
                .setTexture("http://textures.minecraft.net/texture/8514d225b262d847c7e557b474327dcef758c2c5882e41ee6d8c5e9cd3bc914");
    }


    @Override
    public void onClick(PetOwner owner, CustomInventory inventory) {
        inventory.reset(owner);
        PetCore.get().getInvLoaders().PET_DATA.open(owner);
    }
}
