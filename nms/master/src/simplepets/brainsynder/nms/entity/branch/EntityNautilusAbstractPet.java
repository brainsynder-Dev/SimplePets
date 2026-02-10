package simplepets.brainsynder.nms.entity.branch;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.craftbukkit.v1_21_R7.CraftRegistry;
import simplepets.brainsynder.api.entity.passive.IEntityNautilusPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.NautilusArmorType;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.EntityTameablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.nautilus.AbstractNautilus}
 */
public class EntityNautilusAbstractPet extends EntityTameablePet implements IEntityNautilusPet {
    private static final EntityDataAccessor<Boolean> DASH = SynchedEntityData.defineId(EntityNautilusAbstractPet.class, EntityDataSerializers.BOOLEAN);
    private boolean isSaddled = false;
    private NautilusArmorType armor = NautilusArmorType.NONE;

    public EntityNautilusAbstractPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
        doIndirectAttach = true; // TODO: test if this is needed for the nautilus mobs...
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(DASH, false);
    }

    @Override
    public void setArmor(NautilusArmorType armor) {
        this.armor = armor;

        if (armor == null) {
            setItemSlot(EquipmentSlot.BODY, Items.AIR.getDefaultInstance());
            return;
        }

        Registry<Item> registry = CraftRegistry.getMinecraftRegistry(Registries.ITEM);
        setItemSlot(EquipmentSlot.BODY, VersionTranslator.getRegistryValue(registry, armor.getKey()).getDefaultInstance());
    }

    @Override
    public NautilusArmorType getArmor() {
        if (armor == null) return NautilusArmorType.NONE;
        return armor;
    }

    @Override
    public void setPetSaddled(boolean saddled) {
        this.isSaddled = saddled;
        setItemSlot(EquipmentSlot.SADDLE, saddled ? Items.SADDLE.getDefaultInstance() : Items.AIR.getDefaultInstance());
    }

    @Override
    public boolean isPetSaddled() {
        return isSaddled;
    }
}
