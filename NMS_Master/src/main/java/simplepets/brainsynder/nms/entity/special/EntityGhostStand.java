/*
 * Copyright 2023
 * BSDevelopment <https://bsdevelopment.org>
 */

package simplepets.brainsynder.nms.entity.special;

import lib.brainsynder.reflection.Reflection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simplepets.brainsynder.nms.VersionTranslator;

public class EntityGhostStand extends ArmorStand {
    private EntityControllerPet controllerPet;

    public EntityGhostStand(EntityType<? extends ArmorStand> entitytypes, Level world) {
        super(entitytypes, world);
        kill();
    }

    public EntityGhostStand(EntityType<? extends ArmorStand> entitytypes, Level world, EntityControllerPet controllerPet) {
        super(entitytypes, world);
        this.controllerPet = controllerPet;
    }

    @Override
    public EntityType<?> getType() {
        return EntityType.ARMOR_STAND;
    }

    public static EntityGhostStand spawn(Location location, EntityControllerPet pet) {
        EntityGhostStand stand = new EntityGhostStand(EntityType.ARMOR_STAND, Reflection.getWorldHandle(location.getWorld()), pet);
        stand.setPos(location.getX(), location.getY(), location.getZ());
        stand.setNoBasePlate(true);
        stand.setInvulnerable(true);
        stand.setInvisible(true);
        stand.persist = true;
        VersionTranslator.addEntity(VersionTranslator.getWorldHandle(location.getWorld()), stand, CreatureSpawnEvent.SpawnReason.CUSTOM);
        pet.setIgnoreVanish(true);
        return stand;
    }

    @Override
    public void tick() {
        super.tick();
        if ((this.controllerPet == null)
                || (this.controllerPet.getEntity().isDead())
                || (!this.controllerPet.getEntity().isValid())) {
            controllerPet = null;
            kill();
            return;
        }

        if (isCustomNameVisible()) setCustomNameVisible(false);
        if (!isInvisible()) setInvisible(true);
    }

    @Override
    public InteractionResult interactAt(Player entityhuman, Vec3 vec3d, InteractionHand enumhand) {
        return InteractionResult.FAIL;
    }

    protected boolean damageEntity0(DamageSource damagesource, float f) {
        return false;
    }

    // God damnit Spigot changing the method name...
    // See: https://tiny.bsdevelopment.org/spigot-changed-damage-method
    protected boolean actuallyHurt(DamageSource damagesource, float f) {
        return false;
    }




    /**
     * These methods prevent pets from being saved in the worlds
     */
    @Override
    public boolean saveAsPassenger(CompoundTag nbttagcompound) {// Calls e
        return false;
    }

    @Override
    public boolean save(CompoundTag nbttagcompound) {// Calls e
        return false;
    }

    @Override
    public void load(CompoundTag nbttagcompound) {
    }


    // Prevents the stand from being teleported via a portal
    @Override
    public boolean isOnPortalCooldown() {
        return true;
    }

    @Override
    protected void handleNetherPortal() {}
}
