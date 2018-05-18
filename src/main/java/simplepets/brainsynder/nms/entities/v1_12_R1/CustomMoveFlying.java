package simplepets.brainsynder.nms.entities.v1_12_R1;

import net.minecraft.server.v1_12_R1.ControllerMove;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.MathHelper;

public class CustomMoveFlying extends ControllerMove {
    public CustomMoveFlying(EntityInsentient entity) {
        super(entity);
    }

    /**
     * NMS translates (Thanks Forge):
     *
     * this.a() = this.limitAngle()
     * MathHelper.c() = MathHelper.atan2()
     * this.a.o() = this.entity.setMoveVertical()
     * this.a.n() = this.entity.setMoveForward()
     * this.a.k() = this.entity.setAIMoveSpeed()
     *
     * GenericAttributes.e = SharedMonsterAttributes.FLYING_SPEED
     */
    public void a() {
        double posX = this.b;
        double posY = this.c;
        double posZ = this.d;
        double speed = this.e;
        EntityInsentient entity = this.a;

        if (this.h == Operation.MOVE_TO) {
            this.h = Operation.WAIT;
            entity.setNoGravity(true);
            double var1 = posX - entity.locX;
            double var3 = posY - entity.locY;
            double var5 = posZ - entity.locZ;
            double var7 = var1 * var1 + var3 * var3 + var5 * var5;
            if (var7 < 2.500000277905201E-7D) {
                entity.o(0.0F);
                entity.n(0.0F);
                return;
            }

            float var9 = (float) (MathHelper.c(var5, var1) * 57.2957763671875D) - 90.0F;
            entity.yaw = this.a(entity.yaw, var9, 10.0F);
            float var10;
            if (entity.onGround) {
                var10 = (float) (speed * entity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
            } else {
                var10 = (float) (speed * entity.getAttributeInstance(GenericAttributes.e).getValue());
            }

            entity.k(var10);
            double var11 = (double) MathHelper.sqrt(var1 * var1 + var5 * var5);
            float var13 = (float) (-(MathHelper.c(var3, var11) * 57.2957763671875D));
            entity.pitch = this.a(entity.pitch, var13, 10.0F);
            entity.o(var3 > 0.0D ? var10 : -var10);
        } else {
            entity.setNoGravity(false);
            entity.o(0.0F);
            entity.n(0.0F);
        }
    }
}