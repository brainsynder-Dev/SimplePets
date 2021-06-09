package simplepets.brainsynder.versions.v1_17_R1.pathfinder;

import net.minecraft.server.v1_16_R3.PathfinderGoal;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityPet;

import java.util.EnumSet;

public class PathfinderGoalLookAtOwner extends PathfinderGoal {
    private final EntityPet entityPet;
    private PetUser user;
    private Player player;
    private final float range;
    private int lookTime;
    private final float chance;

    public PathfinderGoalLookAtOwner(EntityPet entityPet, float range, float chance) {
        this.entityPet = entityPet;
        this.range = range;
        this.chance = chance;
        this.a(EnumSet.of(Type.LOOK));
    }

    // Translation: canStart
    public boolean a() {
        if ((user == null) || (player == null)) {
            this.user = entityPet.getPetUser();
            this.player = (Player) user.getPlayer();
        }

        if (this.entityPet.getRandom().nextFloat() >= this.chance) {
            return false;
        } else {
            return this.user != null;
        }
    }

    // Translation: shouldContinue
    public boolean b() {
        if (this.player.isDead()) {
            return false;
        } else if (this.entityPet.h(((CraftPlayer)player).getHandle()) > (double)(this.range * this.range)) {
            return false;
        } else {
            return this.lookTime > 0;
        }
    }

    // Translation: start
    public void c() {
        this.lookTime = 40 + this.entityPet.getRandom().nextInt(40);
    }

    // Translation: tick
    public void e() {
        Location location = entityPet.getBukkitEntity().getLocation();
        location.add(location.getDirection().multiply(4.0));
        if (!user.isPetHat(entityPet.getPetType()))
            location.setY(((CraftPlayer)player).getHandle().getHeadY());

        this.entityPet.getControllerLook().a(location.getX(), location.getY(), location.getZ());
        --this.lookTime;
    }
}
