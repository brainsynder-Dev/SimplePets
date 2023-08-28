package simplepets.brainsynder.nms.pathfinder;

import lib.brainsynder.math.MathUtils;
import lib.brainsynder.reflection.Reflection;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.event.entity.movment.PetTeleportEvent;
import simplepets.brainsynder.api.other.ParticleHandler;
import simplepets.brainsynder.api.pet.CommandReason;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.EntityPet;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;

public class PathfinderWalkToPlayer extends Goal {
    private final EntityPet entity;
    private PetUser user;
    private net.minecraft.world.entity.player.Player player;
    private final PathNavigation navigation;

    private int updateCountdownTicks;
    private final float maxDistance;
    private final float minDistance;
    private final boolean first = true;
    private boolean large = true;

    public PathfinderWalkToPlayer(EntityPet entity, int minDistance, int maxDistance) {
        this.entity = entity;

        for (Class<?> interfaceClass : entity.getClass().getInterfaces()) {
            if (interfaceClass.isAnnotationPresent(EntityPetType.class)) {
                PetType type = interfaceClass.getAnnotation(EntityPetType.class).petType();
                large = type.isLargePet();
                break;
            }
        }


        navigation = entity.getNavigation();

        this.maxDistance = modifyInt(maxDistance);
        this.minDistance = modifyInt(minDistance);

        // Translation: setControls(EnumSet<Goal.Control>)
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (user == null) {
            this.user = entity.getPetUser();
            this.player = VersionTranslator.<ServerPlayer>getEntityHandle(user.getPlayer());
        }
        if (user == null) return false;
        if (user.getPlayer() == null) return false;
        if (entity == null) return false;

        if (!user.getPlayer().isOnline()) return false;
        if (user.getPlayer().isInsideVehicle()
                && !ConfigOption.INSTANCE.PATHFINDING_FOLLOW_WHEN_RIDING.getValue()) return false;
        if (!user.hasPets()) return false;

        if (user.getUserLocation().isPresent()) {
            Location location = user.getUserLocation().get();

            if (!location.getWorld().getName().equals(entity.getBukkitEntity().getLocation().getWorld().getName()))
                return false;

            double distance = entity.distanceToSqr(player);
            return (distance >= (double) (maxDistance * minDistance)) || first;
        }


        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (navigation.isDone()) return false; // Translation: navigation.isIdle()
        // Translation: Entity.squaredDistanceTo (Entity)
        double distance = entity.distanceToSqr(player);
        return !(distance < (double) (maxDistance * minDistance));
    }

    @Override
    public void tick() {

        // Translation: Entity.squaredDistanceTo (Entity)
        if (entity.distanceToSqr(this.player) >= 155.0D) { // Will teleport the pet if the player is more then 155 blocks away
            // Translation: Entity.distanceTo (Entity)
            if (entity.distanceTo(this.player) >= 144) { // Will teleport the pet if the player is more then 144 blocks away
                entity.teleportToOwner(); // Will ignore all checks and just teleport to the player
            } else {
                this.tryTeleport();
            }
            return;
        }

        // Translation: EntityInsentient.getLookControl().lookAt(EntityPlayer, 10.0F, (float)EntityInsentient.getLookPitchSpeed())
        //entity.getControllerLook().a(player, 10F, entity.O());
        if (--this.updateCountdownTicks <= 0) {
            this.updateCountdownTicks = 10;

            // Will create a path to the player, and stop the pet within 5 (default) blocks of the player
            // it will stop around 10 blocks if it is a large pet
            Path path = fixThisStupidThing();
            alsoFixThis(path);
        }
    }

    @Override
    public void start() {
        this.updateCountdownTicks = 0;
    }

    @Override
    public void stop() {
        navigation.stop(); // Translation: navigation.stop
    }

    private int getStoppingDistance() {
        if (SimplePets.getConfiguration() == null) return 0;

        return large ? ConfigOption.INSTANCE.PATHFINDING_STOP_DISTANCE_LARGE.getValue()
                : ConfigOption.INSTANCE.PATHFINDING_STOP_DISTANCE_SMALL.getValue();
    }

    /**
     * Will double the number if the pet is a large pet
     *
     * @param number - number to be doubled if need be
     */
    private int modifyInt(int number) {
        return (large ? (number + number) : number);
    }


    private void tryTeleport() {
        BlockPos blockposition = player.blockPosition();
        int distance = modifyInt(5);

        for (int i = 0; i < 15; ++i) {
            int x = this.getRandomInt(-distance, distance);
            int y = this.getRandomInt(-1, 1);
            int z = this.getRandomInt(-distance, distance);
            boolean flag = this.tryTeleportTo(blockposition.getX() + x, blockposition.getY() + y, blockposition.getZ() + z);
            if (flag) return;
        }

        entity.teleportToOwner();
    }

    private boolean tryTeleportTo(int x, int y, int z) {
        if (Math.abs((double) x - player.getX()) < 2.0D && Math.abs((double) z - player.getZ()) < 2.0D) return false;

        PetTeleportEvent event = new PetTeleportEvent(entity, new Location(user.getPlayer().getWorld(), x, y, z));
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;

        if (!this.canTeleportTo(new BlockPos(x, y, z))) return false;
        this.entity.moveTo((double) x + 0.5D, y, (double) z + 0.5D, this.entity.getYRot(), this.entity.getXRot());
        SimplePets.getPetUtilities().runPetCommands(CommandReason.TELEPORT, user, entity.getPetType());
        SimplePets.getParticleHandler().sendParticle(ParticleHandler.Reason.TELEPORT, user.getPlayer(), entity.getEntity().getLocation());
        this.navigation.stop();// Translation: navigation.stop()
        return true;
    }

    private boolean canTeleportTo(BlockPos blockposition) {
        BlockPathTypes pathtype = WalkNodeEvaluator.getBlockPathTypeStatic(VersionTranslator.getEntityLevel(entity), blockposition.mutable());
        if (pathtype != BlockPathTypes.WALKABLE) return false;

        // Translation: BlockPosition.subtract (BlockPosition)
        BlockPos position = VersionTranslator.subtract(blockposition, this.entity.blockPosition());
        return VersionTranslator.getEntityLevel(entity).noCollision(this.entity, this.entity.getBoundingBox().move(position));
    }

    private int getRandomInt(int min, int max) {
        return MathUtils.random(max - min) + min;
    }

    private Path fixThisStupidThing() {
        try {
            return navigation.createPath(player, getStoppingDistance());
        } catch (NoSuchMethodError ex) {
            try {
                return (Path) Reflection.getMethod(PathNavigation.class, "createPath", Entity.class, int.class)
                        .invoke(navigation, player, getStoppingDistance());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void alsoFixThis(Path path) {
        try {
            navigation.moveTo(path, entity.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
        } catch (NoSuchMethodError ex) {
            try {
                Reflection.getMethod(PathNavigation.class, "moveTo", Path.class, double.class)
                        .invoke(navigation, path, entity.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
