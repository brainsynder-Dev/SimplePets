package simplepets.brainsynder.versions.v1_16_R3.pathfinder;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PathfinderWalkToPlayer extends PathfinderGoal {
    private final EntityPet entity;
    private PetUser user;
    private EntityPlayer player;
    private final NavigationAbstract navigation;

    private int updateCountdownTicks;
    private final float maxDistance;
    private final float minDistance;
    private boolean first = true;
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

        this.maxDistance = modifyInt (maxDistance);
        this.minDistance = modifyInt (minDistance);

        // Translation: setControls(EnumSet<Goal.Control>)
        this.a(EnumSet.of(Type.MOVE, Type.LOOK));
    }

    @Override
    public boolean a() { // Translation: canStart
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

            if (!location.getWorld().getName().equals(entity.getBukkitEntity().getLocation().getWorld().getName())) return false;

            if ((entity.getEntity().getLocation().distance(location) >= modifyInt(4)) || first) {
                handleWalkLocation();
                if (first) first = false;
            }
        }


        return (entity.getWalkToLocation() != null);
    }

    @Override
    public boolean b() { // Translation: shouldContinue
        if (navigation.m()) return false; // Translation: navigation.isIdle()

        // Translation: Entity.squaredDistanceTo (Entity)
        double distance = entity.h(player);
        return distance > (double) (maxDistance*minDistance);
    }

    @Override
    public void c() { // Translation: start
        this.updateCountdownTicks = 0;
    }

    @Override
    public void d() { // Translation: stop
        navigation.o(); // Translation: navigation.stop
    }

    @Override
    public void e() { // Translation: tick
        // Translation: EntityInsentient.getLookControl().lookAt(EntityPlayer, 10.0F, (float)EntityInsentient.getLookPitchSpeed())
        entity.getControllerLook().a(player, 10F, entity.O());
        if (--this.updateCountdownTicks <= 0) {
            this.updateCountdownTicks = 10;
            if (!entity.isLeashed() && (entity.getVehicle() == null)) {

                // Translation: Entity.squaredDistanceTo (Entity)
                if (entity.h(this.player) >= 155.0D) { // Will teleport the pet if the player is more then 155 blocks away

                    // Translation: Entity.distanceTo (Entity)
                    if (entity.g(this.player) > 144) { // Will teleport the pet if the player is more then 144 blocks away
                        entity.teleportToOwner(); // Will ignore all checks and just teleport to the player
                        return;
                    }

                    this.tryTeleport();
                } else {
                    Location loc = entity.getWalkToLocation();
                    if (loc == null) {
                        handleWalkLocation();
                        loc = entity.getWalkToLocation();
                    }
                    PathEntity path = navigation.a(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), 2);
                    navigation.a(path, entity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
                }
            }
        }
    }

    private int modifyInt (int number) {
        return (large ? (number+number) : number);
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

        this.entity.setPositionRotation((double)x + 0.5D, y, (double)z + 0.5D, this.entity.yaw, this.entity.pitch);
        this.navigation.o();// Translation: navigation.stop()
        handleWalkLocation();
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

    private void handleWalkLocation () {
        List<Location> locations = circle(user.getUserLocation().get(), modifyInt(6), 1, false, false);
        int index = getRandomInt(0, ( locations.size() - 1 ));
        Location loc = locations.get(index);

        while ((loc.distance(user.getUserLocation().get()) <= modifyInt(3))
                && (loc.distance(entity.getEntity().getLocation()) > 5)) {
            index = getRandomInt(0, ( locations.size() - 1 ));
            loc = locations.get(index);
        }
        entity.setWalkToLocation(loc);
    }

    public List<Location> circle(Location loc, double radius, double height, boolean hollow, boolean sphere) {
        ArrayList circleblocks = new ArrayList();
        double cx = loc.getX();
        double cy = loc.getY();
        double cz = loc.getZ();

        for (double x = cx - radius; x <= cx + radius; ++x) {
            for (double z = cz - radius; z <= cz + radius; ++z) {
                for (double y = sphere ? cy - radius : cy; y < (sphere ? cy + radius : cy + height); ++y) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0.0D);
                    if (dist < radius * radius && (!hollow || dist >= (radius - 1.0D) * (radius - 1.0D))) {
                        Location l = new Location(loc.getWorld(), x, y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }

        return circleblocks;
    }
}
