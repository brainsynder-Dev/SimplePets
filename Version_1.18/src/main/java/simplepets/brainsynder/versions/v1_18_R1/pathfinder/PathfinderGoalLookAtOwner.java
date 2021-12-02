package simplepets.brainsynder.versions.v1_18_R1.pathfinder;

import net.minecraft.world.entity.ai.goal.Goal;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_18_R1.entity.EntityPet;

import java.util.EnumSet;

public class PathfinderGoalLookAtOwner extends Goal {
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
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if ((user == null) || (player == null)) {
            this.user = entityPet.getPetUser();
            if (!(user.getPlayer() instanceof Player)) return false;
            this.player = user.getPlayer();
        }

        if (this.entityPet.getRandom().nextFloat() >= this.chance) {
            return false;
        } else {
            return this.user != null;
        }
    }

    // Translation: shouldContinue
    @Override
    public boolean canContinueToUse() {
        if (this.player.isDead()) {
            return false;
        } else if (this.entityPet.distanceToSqr(((CraftPlayer)player).getHandle()) > (double)(this.range * this.range)) {
            return false;
        } else {
            return this.lookTime > 0;
        }
    }

    // Translation: start
    @Override
    public void start() {
        this.lookTime = 40 + this.entityPet.getRandom().nextInt(40);
    }

    // Translation: tick
    @Override
    public void tick() {
        Location location = entityPet.getBukkitEntity().getLocation();
        location.add(location.getDirection().multiply(4.0));
        if (!user.isPetHat(entityPet.getPetType()))
            location.setY(((CraftPlayer)player).getHandle().getEyeY());

        this.entityPet.getLookControl().setLookAt(location.getX(), location.getY(), location.getZ());
        --this.lookTime;
    }
}
