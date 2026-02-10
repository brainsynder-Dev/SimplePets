package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WoolCarpetBlock;
import org.bukkit.NamespacedKey;
import simplepets.brainsynder.api.entity.passive.IEntityLlamaPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.ColorWrapper;
import simplepets.brainsynder.api.wrappers.LlamaColor;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.branch.EntityDonkeyAbstractPet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.horse.Llama}
 */
public class EntityLlamaPet extends EntityDonkeyAbstractPet implements IEntityLlamaPet {
    private static final EntityDataAccessor<Integer> STRENGTH = SynchedEntityData.defineId(EntityLlamaPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityLlamaPet.class, EntityDataSerializers.INT);

    public EntityLlamaPet(PetType type, PetUser user) {
        this(EntityType.LLAMA, type, user);
    }

    public EntityLlamaPet(EntityType<? extends Mob> llama, PetType type, PetUser user) {
        super(llama, type, user);
        doIndirectAttach = false;
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("skin", getSkinColor().name());
        data.add("color", getColorWrapper().name());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(STRENGTH, 0);
        dataAccess.define(VARIANT, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setString("skin", getSkinColor().name());
        object.setString("color", getColorWrapper().name());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("skin")) setSkinColor(LlamaColor.getByName(object.getString("skin")));
        if (object.hasKey("color"))
            setColorWrapper(ColorWrapper.getByName(object.getString("color")));
        super.applyCompound(object);
    }

    @Override
    public void setSkinColor(LlamaColor skinColor) {
        this.entityData.set(VARIANT, skinColor.ordinal());
    }

    @Override
    public LlamaColor getSkinColor() {
        return LlamaColor.getByID(entityData.get(VARIANT));
    }

    @Override
    public ColorWrapper getColorWrapper() {
        DyeColor dyeColor = getDyeColor(getItemBySlot(EquipmentSlot.BODY));
        byte data = ((dyeColor == null) ? -1 : (byte)dyeColor.getId());

        ColorWrapper color = ColorWrapper.getByWoolData(data);
        if (color == null) color = ColorWrapper.getByDyeData(data);
        return color;
    }

    @Override
    public void setColorWrapper(ColorWrapper color) {
        if (color == ColorWrapper.NONE) {
            setItemSlot(EquipmentSlot.BODY, ItemStack.EMPTY, true);
            return;
        }
        DyeColor dyeColor = DyeColor.byId(color.getWoolData());
        Item item = VersionTranslator.getRegistryValue(BuiltInRegistries.ITEM, NamespacedKey.minecraft(dyeColor.getName()+"_carpet"));

        setItemSlot(EquipmentSlot.BODY, new ItemStack(item));
    }


    private DyeColor getDyeColor(ItemStack itemstack) {
        Block block = Block.byItem(itemstack.getItem());
        return block instanceof WoolCarpetBlock ? ((WoolCarpetBlock)block).getColor() : null;
    }
}
