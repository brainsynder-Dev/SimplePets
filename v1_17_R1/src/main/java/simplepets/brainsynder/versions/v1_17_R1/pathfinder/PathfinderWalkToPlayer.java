package simplepets.brainsynder.versions.v1_17_R1.pathfinder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.managers.ParticleManager;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityPet;

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
            this.player = ((CraftPlayer) entity.getPetUser().getPlayer()).getHandle();
        }
        if (user == null) return false;
        if (entity == null) return false;

        if (!user.getPlayer().isOnline()) return false;
        if (((Player) user.getPlayer()).isInsideVehicle()) return false;
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
            Path path = navigation.createPath(player, getStoppingDistance());
            navigation.moveTo(path, entity.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
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
        return large ? PetCore.getInstance().getConfiguration().getInt("Pathfinding.Stopping-Distance_LargePets")
                : PetCore.getInstance().getConfiguration().getInt("Pathfinding.Stopping-Distance");
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
        int distance = modifyInt(3);

        for (int i = 0; i < 10; ++i) {
            int x = this.getRandomInt(-distance, distance);
            int y = this.getRandomInt(-1, 1);
            int z = this.getRandomInt(-distance, distance);
            boolean flag = this.tryTeleportTo(blockposition.getX() + x, blockposition.getY() + y, blockposition.getZ() + z);
            if (flag) return;
        }
    }

    private boolean tryTeleportTo(int x, int y, int z) {
        if (Math.abs((double) x - player.getX()) < 2.0D && Math.abs((double) z - player.getZ()) < 2.0D) return false;
        if (!this.canTeleportTo(new BlockPos(x, y, z))) return false;
        this.entity.moveTo((double) x + 0.5D, y, (double) z + 0.5D, this.entity.getYRot(), this.entity.getXRot());
        PetCore.getInstance().getParticleHandler().sendParticle(ParticleManager.Reason.TELEPORT, (Player) user.getPlayer(), entity.getEntity().getLocation());
        this.navigation.stop();// Translation: navigation.stop()
        return true;
    }

    private boolean canTeleportTo(BlockPos blockposition) {
        BlockPathTypes pathtype = WalkNodeEvaluator.getBlockPathTypeStatic(this.entity.level, blockposition.mutable());
        if (pathtype != BlockPathTypes.WALKABLE) return false;

        // Translation: BlockPosition.subtract (BlockPosition)
        BlockPos position = blockposition.e(this.entity.blockPosition());
        return this.entity.level.noCollision(this.entity, this.entity.getBoundingBox().move(position));
    }

    private int getRandomInt(int min, int max) {
        return entity.getRandom().nextInt(max - min + 1) + min;
    }
}
