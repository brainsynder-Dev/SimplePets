package simplepets.brainsynder.nms.entities.v1_12_R1;

import net.minecraft.server.v1_12_R1.ControllerMove;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.MathHelper;

public class CustomMoveFlying extends ControllerMove {
    public CustomMoveFlying(EntityInsentient var1) {
        super(var1);
    }

    public void a() {
        if (this.h == Operation.MOVE_TO) {
            this.h = Operation.WAIT;
            this.a.setNoGravity(true);
            double var1 = this.b - this.a.locX;
            double var3 = this.c - this.a.locY;
            double var5 = this.d - this.a.locZ;
            double var7 = var1 * var1 + var3 * var3 + var5 * var5;
            if (var7 < 2.500000277905201E-7D) {
                this.a.o(0.0F);
                this.a.n(0.0F);
                return;
            }

            float var9 = (float) (MathHelper.c(var5, var1) * 57.2957763671875D) - 90.0F;
            this.a.yaw = this.a(this.a.yaw, var9, 10.0F);
            float var10;
            if (this.a.onGround) {
                var10 = (float) (this.e * this.a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
            } else {
                var10 = (float) (this.e * this.a.getAttributeInstance(GenericAttributes.e).getValue());
            }

            this.a.k(var10);
            double var11 = (double) MathHelper.sqrt(var1 * var1 + var5 * var5);
            float var13 = (float) (-(MathHelper.c(var3, var11) * 57.2957763671875D));
            this.a.pitch = this.a(this.a.pitch, var13, 10.0F);
            this.a.o(var3 > 0.0D ? var10 : -var10);
        } else {
            this.a.setNoGravity(false);
            this.a.o(0.0F);
            this.a.n(0.0F);
        }

    }
}