package simplepets.brainsynder.versions.v1_17_R1.entity.special;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import simplepets.brainsynder.api.entity.misc.IEntityControlledPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public abstract class EntityControlledPet extends LivingEntity implements IEntityControlledPet {

    protected EntityControlledPet(net.minecraft.world.entity.EntityType<? extends LivingEntity> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return null;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT; // TODO - make this changeable?
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    @Override
    public void setBurning(boolean var) {

    }

    @Override
    public EntityType getPetEntityType() {
        return null;
    }
}
