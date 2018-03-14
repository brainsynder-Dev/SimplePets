package simplepets.brainsynder.nms.pathfinders.v1_8_R3;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import simplepets.brainsynder.nms.entities.v1_8_R3.EntityPet;
import simplepets.brainsynder.reflection.ReflectionUtil;

import java.util.List;
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
            EntityPlayer player = ((CraftPlayer) p).getHandle();
            if (nms_entity instanceof EntityPet) {
                final EntityPet creature = (EntityPet) nms_entity;
                List goalB = (List) getPrivateField("b", getNmsClass("PathfinderGoalSelector"), creature.goalSelector);
                goalB.clear();
                List goalC = (List) getPrivateField("c", getNmsClass("PathfinderGoalSelector"), creature.goalSelector);
                goalC.clear();
                List targetB = (List) getPrivateField("b", getNmsClass("PathfinderGoalSelector"), creature.targetSelector);
                targetB.clear();
                List targetC = (List) getPrivateField("c", getNmsClass("PathfinderGoalSelector"), creature.targetSelector);
                targetC.clear();
                creature.goalSelector.a(0, new PathfinderGoalFloat(creature));
                creature.goalSelector.a(1, new PathFinderGoalWalkToPlayer(creature, p, speed));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}