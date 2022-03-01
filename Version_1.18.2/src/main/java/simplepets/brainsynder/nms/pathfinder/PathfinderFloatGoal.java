package simplepets.brainsynder.nms.pathfinder;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;
import simplepets.brainsynder.nms.entity.EntityBase;

import java.util.EnumSet;

public class PathfinderFloatGoal extends Goal {
    private final EntityBase entity;

    public PathfinderFloatGoal(EntityBase entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.JUMP));
        entity.getNavigation().setCanFloat(true);
    }

    public boolean canUse() {
        return this.entity.isInWater()
                && this.entity.getFluidHeight(FluidTags.WATER) > this.entity.getFluidJumpThreshold()
                || this.entity.isInLava();
    }

    public void tick() {
        if (this.entity.getRandom().nextFloat() < 0.8F) {
            this.entity.getJumpControl().jump();
        }

    }
}
