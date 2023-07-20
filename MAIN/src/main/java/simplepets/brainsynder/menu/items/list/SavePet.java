package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.nbt.StorageTagCompound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.menu.inventory.PetSelectorMenu;
import simplepets.brainsynder.utils.Utilities;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Namespace(namespace = "savepet")
public class SavePet extends Item {
    public SavePet(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.CHAIN_COMMAND_BLOCK)
                .withName("&#e3c79a&lSave Pet")
                .addLore("&7Click here to save your current","&7pet for you to spawn later");
    }

    @Override
    public boolean addItemToInv(PetUser user, CustomInventory inventory) {
        return user.canSaveMorePets();
    }

    @Override
    public void onClick(PetUser masterUser, CustomInventory inventory, IEntityPet pet) {
        if (!masterUser.hasPets()) return;

        if (pet != null) {
            if (canSavePet(masterUser, pet)) {
                StorageTagCompound compound = pet.asCompound();
                if (pet.getPetType() == PetType.ARMOR_STAND) compound.setBoolean("restricted", true);
                masterUser.addPetSave(compound);
            }
            PetCore.getInstance().getScheduler().getImpl().runAtEntityLater(masterUser.getPlayer(), () -> inventory.open(masterUser), 50L, TimeUnit.MILLISECONDS);
            return;
        }

        if (masterUser.getPetEntities().size() == 1) {
            masterUser.getPetEntities().stream().findFirst().ifPresent(iEntityPet -> {
                if (canSavePet(masterUser, iEntityPet)) {
                    StorageTagCompound compound = iEntityPet.asCompound();
                    if (iEntityPet.getPetType() == PetType.ARMOR_STAND) compound.setBoolean("restricted", true);
                    masterUser.addPetSave(compound);
                }
            });
            PetCore.getInstance().getScheduler().getImpl().runAtEntityLater(masterUser.getPlayer(), () -> inventory.open(masterUser), 50L, TimeUnit.MILLISECONDS);
            return;
        }
        PetSelectorMenu menu = InventoryManager.SELECTOR;
        menu.setTask(masterUser.getPlayer().getName(), (user, type) -> {
            user.getPetEntity(type).ifPresent(entity -> {
                if (canSavePet(user, entity)) {
                    StorageTagCompound compound = entity.asCompound();
                    if (type == PetType.ARMOR_STAND) compound.setBoolean("restricted", true);
                    user.addPetSave(compound);
                }
            });
            PetCore.getInstance().getScheduler().getImpl().runAtEntityLater(user.getPlayer(), () -> inventory.open(user), 50L, TimeUnit.MILLISECONDS);
        });
        menu.open(masterUser, 1, inventory.getTitle());
    }

    private boolean canSavePet (PetUser user, IEntityPet entityPet) {
        if (!ConfigOption.INSTANCE.PET_SAVES_ENABLED.getValue()) return false;
        Player player = user.getPlayer();

        if (!user.canSaveMorePets()) {
            player.sendMessage(MessageFile.getTranslation(MessageOption.PET_SAVES_LIMIT_REACHED));
            return false;
        }
        
        if (player.hasPermission("pet.saves."+entityPet.getPetType().getName()+".bypass")) return true;

        int saveLimit = Utilities.parseTypeSaveLimit(entityPet.getPetType());
        if (saveLimit < 0) return true;

        int typeCount = 0;
        for (PetUser.Entry<PetType, StorageTagCompound> entry : user.getSavedPets()) {
            if (entry.getKey() == entityPet.getPetType()) typeCount++;
        }

        if (typeCount < Utilities.getPermissionAmount(player, saveLimit, "pet.saves."+entityPet.getPetType().getName()+".")) {
            return true;
        }
        player.sendMessage(MessageFile.getTranslation(MessageOption.PET_SAVES_LIMIT_REACHED_TYPE).replace("{type}", entityPet.getPetType().getName()));
        return false;
    }
}