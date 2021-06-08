package simplepets.brainsynder.versions.v1_16_R3.pathfinder;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.managers.ParticleManager;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;

import java.util.EnumSet;

public class PathfinderWalkToPlayer extends PathfinderBase {
    private final EntityPet entity;
    private PetUser user;
    private EntityPlayer player;
    private final NavigationAbstract navigation;

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
        this.a(EnumSet.of(Type.MOVE, Type.LOOK));
    }

    @Override
    public boolean canStart() {
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

            double distance = entity.h(player);
            return (distance >= (double) (maxDistance * minDistance)) || first;
        }


        return false;
    }

    @Override
    public boolean shouldContinue() {
        if (navigation.m()) return false; // Translation: navigation.isIdle()
        // Translation: Entity.squaredDistanceTo (Entity)
        double distance = entity.h(player);
        return !(distance < (double) (maxDistance * minDistance));
    }

    @Override
    public void tick() {

        // Translation: Entity.squaredDistanceTo (Entity)
        if (entity.h(this.player) >= 155.0D) { // Will teleport the pet if the player is more then 155 blocks away
            // Translation: Entity.distanceTo (Entity)
            if (entity.g(this.player) >= 144) { // Will teleport the pet if the player is more then 144 blocks away
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
            PathEntity path = navigation.a(player, getStoppingDistance());
            navigation.a(path, entity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
        }
    }

    @Override
    public void start() {
        this.updateCountdownTicks = 0;
    }

    @Override
    public void stop() {
        navigation.o(); // Translation: navigation.stop
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
        BlockPosition blockposition = player.getChunkCoordinates();
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
        if (Math.abs((double) x - player.locX()) < 2.0D && Math.abs((double) z - player.locZ()) < 2.0D) return false;
        if (!this.canTeleportTo(new BlockPosition(x, y, z))) return false;
        this.entity.setPositionRotation((double) x + 0.5D, y, (double) z + 0.5D, this.entity.yaw, this.entity.pitch);
        PetCore.getInstance().getParticleHandler().sendParticle(ParticleManager.Reason.TELEPORT, (Player) user.getPlayer(), entity.getEntity().getLocation());
        this.navigation.o();// Translation: navigation.stop()
        return true;
    }

    private boolean canTeleportTo(BlockPosition blockposition) {
        // Translation: LandPathNodeMaker.getLandNodeType (World, BlockPosition)
        // Translation: BlockPosition.mutableCopy()
        PathType pathtype = PathfinderNormal.a(this.entity.world, blockposition.i());
        if (pathtype != PathType.WALKABLE) return false;

        // Translation: BlockPosition.subtract (BlockPosition)
        BlockPosition position = blockposition.b(this.entity.getChunkCoordinates());
        return this.entity.world.getCubes(this.entity, this.entity.getBoundingBox().a(position));
    }

    private int getRandomInt(int min, int max) {
        return entity.getRandom().nextInt(max - min + 1) + min;
    }
}
