package simplepets.brainsynder.nms.pathfinder;

import lib.brainsynder.math.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import simplepets.brainsynder.api.entity.misc.IFlyableEntity;
import simplepets.brainsynder.api.event.entity.movment.PetTeleportEvent;
import simplepets.brainsynder.api.other.ParticleHandler;
import simplepets.brainsynder.api.pet.CommandReason;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.EntityPet;

import java.util.EnumSet;

public class PathfinderFollowPlayer extends Goal {
    private final EntityPet entity;
    private final PathNavigation navigation;
    private final float maxRange;
    private final double minRange;
    private final int teleportDistance;

    private PetUser user;
    private Player player;
    private BlockPos targetPos;

    public PathfinderFollowPlayer(EntityPet entity) {
        this.entity = entity;
        this.maxRange = ConfigOption.INSTANCE.PATHFINDING_MAX_DISTANCE.getValue();
        this.minRange = ConfigOption.INSTANCE.PATHFINDING_MIN_DISTANCE.getValue();
        this.teleportDistance = ConfigOption.INSTANCE.PATHFINDING_TELEPORT_DISTANCE.getValue();

        navigation = entity.getNavigation();

        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        VersionTranslator.setFollowAttributes(entity, maxRange);
    }

    @Override // Runs per tick
    public boolean canUse() {
        // Simple Check in case user is null
        if (user == null) {
            this.user = entity.getPetUser();
            this.player = VersionTranslator.<ServerPlayer>getEntityHandle(user.getPlayer());
        }

        if (user == null) return false; // Failed: no user found
        if (user.getPlayer() == null) return false; // Failed: no player found
        if (entity == null) return false; // Failed: entity is missing ?!?

        if (!user.getPlayer().isOnline()) return false; // Failed: player is not online

        // Failed: player is riding a mob, and config denies pet from following player
        if (user.getPlayer().isInsideVehicle()
                && !ConfigOption.INSTANCE.PATHFINDING_FOLLOW_WHEN_RIDING.getValue()) return false;
        if (!user.hasPets()) return false; // Failed: player has no pets

        // Failed: pet and player are in different worlds
        if (!VersionTranslator.getEntityLevel(player).getWorld().getName()
                .equals(VersionTranslator.getEntityLevel(entity).getWorld().getName())) return false;

        targetPos = new BlockPos(randomize(player.getX()), player.getBlockY(), randomize(player.getZ()));
        return true;
    }

    @Override // called after start() and if canUse() == true
    public boolean canContinueToUse() {
        return !navigation.isInProgress() && (entity.distanceToSqr(player) < (double) (this.maxRange * this.maxRange));
    }

    @Override
    public void tick() {
        if (entity.distanceToSqr(this.player) >= teleportDistance) { // Will teleport the pet if the player is more than 155 blocks away
            if (entity.distanceTo(this.player) >= 80) { // Will teleport the pet if the player is more than 144 blocks away
                entity.teleportToOwner(); // Will ignore all checks and just teleport to the player
            } else {
                this.tryTeleport();
            }
        }
    }

    @Override // Set navigation here
    public void start() {
        // Failed: pet is in the air and is not a flying pet
        if ( (!(entity instanceof IFlyableEntity)) && (!entity.onGround())) return;

        Path path = navigation.createPath(targetPos, 1);
        navigation.moveTo(path, VersionTranslator.getWalkSpeed(entity));
    }

    @Override
    public void stop() {
        navigation.stop();
    }

    private void tryTeleport() {
        BlockPos blockPosition = player.blockPosition();
        double distance = this.minRange;

        for (int i = 0; i < 15; ++i) {
            double x = this.getRandomInt(-distance, distance);
            double y = this.getRandomInt(-1, 1);
            double z = this.getRandomInt(-distance, distance);
            boolean flag = this.tryTeleportTo(blockPosition.getX() + x, blockPosition.getY() + y, blockPosition.getZ() + z);
            if (flag) return;
        }

        entity.teleportToOwner();
    }

    private boolean tryTeleportTo(double x, double y, double z) {
        if (Math.abs(x - player.getX()) < 2.0D && Math.abs(z - player.getZ()) < 2.0D) return false;

        PetTeleportEvent event = new PetTeleportEvent(entity, new Location(user.getPlayer().getWorld(), x, y, z));
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;

        if (!this.canTeleportTo(new BlockPos((int) x, (int) y, (int) z))) return false;
        VersionTranslator.moveTo(entity, x + 0.5D, y, z + 0.5D, this.entity.getYRot(), this.entity.getXRot());
        SimplePets.getPetUtilities().runPetCommands(CommandReason.TELEPORT, user, entity.getPetType());
        SimplePets.getParticleHandler().sendParticle(ParticleHandler.Reason.TELEPORT, user.getPlayer(), entity.getEntity().getLocation());
        this.navigation.stop();
        return true;
    }

    private boolean canTeleportTo(BlockPos blockposition) {
        if (!VersionTranslator.isWalkable(entity, blockposition.mutable())) return false;

        BlockPos position = VersionTranslator.subtract(blockposition, this.entity.blockPosition());
        return VersionTranslator.getEntityLevel(entity).noCollision(this.entity, this.entity.getBoundingBox().move(position));
    }

    private int randomize (double value) {
        return (int) (value + ( MathUtils.random((int) -this.minRange, (int) this.minRange) ));
    }

    private double getRandomInt(double min, double max) {
        return MathUtils.random((float) (max - min)) + min;
    }
}
