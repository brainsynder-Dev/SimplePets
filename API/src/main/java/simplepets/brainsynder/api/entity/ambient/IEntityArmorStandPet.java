package simplepets.brainsynder.api.entity.ambient;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IDisplayEntity;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.ARMOR_STAND)
public interface IEntityArmorStandPet extends IDisplayEntity {
    boolean isSmallStand();
    void setSmallStand(boolean flag);

    boolean isOwner();
    void setOwner(boolean flag);

    default EulerAngle getHeadAngle() { return new EulerAngle(0,0,0); }
    default EulerAngle getBodyAngle() { return new EulerAngle(0,0,0); }
    default EulerAngle getLeftArmAngle() { return new EulerAngle(0,0,0); }
    default EulerAngle getRightArmAngle() { return new EulerAngle(0,0,0); }
    default EulerAngle getLeftLegAngle() { return new EulerAngle(0,0,0); }
    default EulerAngle getRightLegAngle() { return new EulerAngle(0,0,0); }

    default void setHeadAngle(EulerAngle vector) {}
    default void setBodyAngle(EulerAngle vector) {}
    default void setLeftArmAngle(EulerAngle vector) {}
    default void setRightArmAngle(EulerAngle vector) {}
    default void setLeftLegAngle(EulerAngle vector) {}
    default void setRightLegAngle(EulerAngle vector) {}

    void setBasePlateVisibility(boolean flag);
    boolean hasBasePlateVisibility();

    boolean hasArmsVisibile();
    void setArmsVisibile(boolean flag);

    boolean isRestricted();
    void setRestricted(boolean flag);

    // For future ArmorStand Customizations...
    default void setHeadItem(ItemStack item) {}
    default void setBodyItem(ItemStack item) {}
    default void setLegItem(ItemStack item) {}
    default void setFootItem(ItemStack item) {}
    default void setRightArmItem(ItemStack item) {}
    default void setLeftArmItem(ItemStack item) {}
    default ItemStack getHeadItem () {
        return new ItemStack(Material.AIR);
    }
    default ItemStack getBodyItem () {
        return new ItemStack(Material.AIR);
    }
    default ItemStack getLegItem () {
        return new ItemStack(Material.AIR);
    }
    default ItemStack getFootItem () {
        return new ItemStack(Material.AIR);
    }
    default ItemStack getRightArmItem () {
        return new ItemStack(Material.AIR);
    }
    default ItemStack getLeftArmItem () {
        return new ItemStack(Material.AIR);
    }
}
