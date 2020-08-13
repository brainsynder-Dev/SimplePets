package simplepets.brainsynder.nms.v1_16_R2.pathfinders;

import net.minecraft.server.v1_16_R2.PathfinderGoalFloat;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftEntity;
import org.bukkit.entity.Player;
import simplepets.brainsynder.nms.v1_16_R2.entities.EntityPet;
import simplepets.brainsynder.reflection.ReflectionUtil;

import java.util.Set;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.PathfinderGoalSelector}
 */
public class HandlePathfinders extends ReflectionUtil {
    public HandlePathfinders(Player p, org.bukkit.entity.Entity e, double speed) {
        try {
            Object nms_entity = ((CraftEntity) e).getHandle();
            if (nms_entity instanceof EntityPet) {
                final EntityPet creature = (EntityPet) nms_entity;
                Set goalB = (Set) getPrivateField("d", getNmsClass("PathfinderGoalSelector"), creature.goalSelector);
                goalB.clear();
                Set targetC = (Set) getPrivateField("d", getNmsClass("PathfinderGoalSelector"), creature.targetSelector);
                targetC.clear();
                creature.goalSelector.a(0, new PathfinderGoalFloat(creature));
                creature.goalSelector.a(1, new PathFinderGoalWalkToPlayer(creature, p, speed));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
