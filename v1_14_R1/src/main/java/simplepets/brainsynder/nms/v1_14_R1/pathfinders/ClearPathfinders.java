package simplepets.brainsynder.nms.v1_14_R1.pathfinders;

import net.minecraft.server.v1_14_R1.Entity;
import net.minecraft.server.v1_14_R1.EntityInsentient;
import net.minecraft.server.v1_14_R1.PathfinderGoalFloat;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import simplepets.brainsynder.reflection.ReflectionUtil;

import java.util.Set;

public class ClearPathfinders extends ReflectionUtil {

    public ClearPathfinders(org.bukkit.entity.Entity e) {
        try {
            Object nms_entity = ((CraftEntity) e).getHandle();
            if (nms_entity instanceof EntityInsentient) {
                final Entity creature = (Entity) nms_entity;
                Set goalC = (Set) getPrivateField("c", getNmsClass("PathfinderGoalSelector"), ((EntityInsentient) creature).goalSelector);
                goalC.clear();
                Set goalD = (Set) getPrivateField("d", getNmsClass("PathfinderGoalSelector"), ((EntityInsentient) creature).goalSelector);
                goalD.clear();
                Set targetC = (Set) getPrivateField("c", getNmsClass("PathfinderGoalSelector"), ((EntityInsentient) creature).targetSelector);
                targetC.clear();
                Set targetD = (Set) getPrivateField("d", getNmsClass("PathfinderGoalSelector"), ((EntityInsentient) creature).targetSelector);
                targetD.clear();
                ((EntityInsentient) creature).goalSelector.a(0, new PathfinderGoalFloat(((EntityInsentient) creature)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
