package simplepets.brainsynder.nms.pathfinders.v1_10_R1;

import net.minecraft.server.v1_10_R1.PathfinderGoalFloat;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.entity.Player;
import simplepets.brainsynder.nms.entities.v1_10_R1.EntityPet;
import simplepets.brainsynder.reflection.ReflectionUtil;

import java.util.Set;
import java.util.UUID;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class HandlePathfinders extends ReflectionUtil {
    public HandlePathfinders(Player p, org.bukkit.entity.Entity e, double speed) {
        try {
            final UUID toFollow = p.getUniqueId();
            Object nms_entity = ((CraftEntity) e).getHandle();
            if (nms_entity instanceof EntityPet) {
                final EntityPet creature = (EntityPet) nms_entity;
                Set goalB = (Set) getPrivateField("b", getNmsClass("PathfinderGoalSelector"), creature.goalSelector);
                goalB.clear();
                Set goalC = (Set) getPrivateField("c", getNmsClass("PathfinderGoalSelector"), creature.goalSelector);
                goalC.clear();
                Set targetB = (Set) getPrivateField("b", getNmsClass("PathfinderGoalSelector"), creature.targetSelector);
                targetB.clear();
                Set targetC = (Set) getPrivateField("c", getNmsClass("PathfinderGoalSelector"), creature.targetSelector);
                targetC.clear();
                creature.goalSelector.a(0, new PathfinderGoalFloat(creature));
                creature.goalSelector.a(1, new PathFinderGoalWalkToPlayer(creature, p, speed));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
