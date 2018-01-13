package simplepets.brainsynder.nms.pathfinders.v1_8_R3;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import simplepets.brainsynder.reflection.ReflectionUtil;

import java.util.List;

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
                List goalB = (List) getPrivateField("b", getNmsClass("PathfinderGoalSelector"), ((EntityInsentient) creature).goalSelector);
                goalB.clear();
                List goalC = (List) getPrivateField("c", getNmsClass("PathfinderGoalSelector"), ((EntityInsentient) creature).goalSelector);
                goalC.clear();
                List targetB = (List) getPrivateField("b", getNmsClass("PathfinderGoalSelector"), ((EntityInsentient) creature).targetSelector);
                targetB.clear();
                List targetC = (List) getPrivateField("c", getNmsClass("PathfinderGoalSelector"), ((EntityInsentient) creature).targetSelector);
                targetC.clear();
                ((EntityInsentient) creature).goalSelector.a(0, new PathfinderGoalFloat(((EntityInsentient) creature)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}