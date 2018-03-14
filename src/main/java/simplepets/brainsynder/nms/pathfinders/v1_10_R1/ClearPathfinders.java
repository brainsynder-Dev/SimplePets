package simplepets.brainsynder.nms.pathfinders.v1_10_R1;

import net.minecraft.server.v1_10_R1.Entity;
import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.PathfinderGoalFloat;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import simplepets.brainsynder.reflection.ReflectionUtil;

import java.util.Set;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class ClearPathfinders extends ReflectionUtil {
    public ClearPathfinders(org.bukkit.entity.Entity e) {
        try {
            Object nms_entity = ((CraftEntity) e).getHandle();
            if (nms_entity instanceof EntityInsentient) {
                final Entity creature = (Entity) nms_entity;
                Set goalB = (Set) getPrivateField("b", getNmsClass("PathfinderGoalSelector"), ((EntityInsentient) creature).goalSelector);
                goalB.clear();
                Set goalC = (Set) getPrivateField("c", getNmsClass("PathfinderGoalSelector"), ((EntityInsentient) creature).goalSelector);
                goalC.clear();
                Set targetB = (Set) getPrivateField("b", getNmsClass("PathfinderGoalSelector"), ((EntityInsentient) creature).targetSelector);
                targetB.clear();
                Set targetC = (Set) getPrivateField("c", getNmsClass("PathfinderGoalSelector"), ((EntityInsentient) creature).targetSelector);
                targetC.clear();
                ((EntityInsentient) creature).goalSelector.a(0, new PathfinderGoalFloat(((EntityInsentient) creature)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
