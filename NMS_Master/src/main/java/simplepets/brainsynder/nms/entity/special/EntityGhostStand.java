/*
 * Copyright 2023
 * BSDevelopment <https://bsdevelopment.org>
 */

package simplepets.brainsynder.nms.entity.special;

import lib.brainsynder.reflection.Reflection;
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
    public EntityGhostStand(EntityType<? extends ArmorStand> entitytypes, Level world) {
        super(entitytypes, world);
    }

    public static EntityGhostStand spawn(Location location, EntityControllerPet pet) {
        EntityGhostStand stand = new EntityGhostStand(EntityType.ARMOR_STAND, Reflection.getWorldHandle(location.getWorld()));
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
        if (!isInvisible()) setInvisible(true);
    }

    @Override
    public InteractionResult interactAt(Player entityhuman, Vec3 vec3d, InteractionHand enumhand) {
        return InteractionResult.FAIL;
    }

    @Override
    protected boolean damageEntity0(DamageSource damagesource, float f) {
        return false;
    }
}
