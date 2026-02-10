package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.math.MathUtils;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import simplepets.brainsynder.api.entity.hostile.IEntityWitchPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.branch.EntityRaiderPet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Witch}
 */
public class EntityWitchPet extends EntityRaiderPet implements IEntityWitchPet {
    private static final EntityDataAccessor<Boolean> IS_DRINKING = SynchedEntityData.defineId(EntityWitchPet.class, EntityDataSerializers.BOOLEAN);

    public EntityWitchPet(PetType type, PetUser user) {
        super(EntityType.WITCH, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("drinking-potion", isDrinkingPotion());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(IS_DRINKING, Boolean.FALSE);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("potion", isDrinkingPotion());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("potion"))
            setDrinkingPotion(object.getBoolean("potion"));
        super.applyCompound(object);
    }

    @Override
    public void setDrinkingPotion(boolean flag) {
        entityData.set(IS_DRINKING, flag);
        if (flag) {
            ItemStack item = new ItemStack(Material.POTION);
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            meta.setColor(Color.fromRGB(MathUtils.random(0,255), MathUtils.random(0,255), MathUtils.random(0,255)));
            item.setItemMeta(meta);
            setItemSlot(EquipmentSlot.MAINHAND, VersionTranslator.toNMSStack(item));
        } else {
            setItemSlot(EquipmentSlot.MAINHAND, Items.AIR.getDefaultInstance());
        }
    }

    @Override
    public boolean isDrinkingPotion() {
        return entityData.get(IS_DRINKING);
    }
}
