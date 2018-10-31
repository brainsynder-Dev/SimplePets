package simplepets.brainsynder.nms.v1_13_R2.pathfinders;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.PathfinderGoalFloat;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import simplepets.brainsynder.reflection.ReflectionUtil;

import java.util.Set;

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
