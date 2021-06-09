package simplepets.brainsynder.versions.v1_17_R1.pathfinder;

import net.minecraft.server.v1_16_R3.PathfinderGoal;

public abstract class PathfinderBase extends PathfinderGoal {

    public abstract boolean canStart ();
    public boolean canStop () {
        return true;
    }
    public boolean shouldContinue () {
        return canStart();
    }
    public void start () {}
    public void stop () {}
    public void tick () {}

    @Override
    public void c() {
        start();
    }

    @Override
    public void d() {
        stop();
    }

    @Override
    public void e() {
        tick();
    }

    @Override
    public boolean a() {
        return canStart();
    }

    @Override
    public boolean C_() {
        return canStop();
    }

    @Override
    public boolean b() {
        return shouldContinue();
    }
}
