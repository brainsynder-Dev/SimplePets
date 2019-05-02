package simplepets.brainsynder.nms.v1_14_R1.entities.movements;

import net.minecraft.server.v1_14_R1.ControllerMove;
import net.minecraft.server.v1_14_R1.GenericAttributes;
import net.minecraft.server.v1_14_R1.MathHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import simplepets.brainsynder.nms.v1_14_R1.entities.EntityPet;
import simplepets.brainsynder.player.PetOwner;

import java.util.ArrayList;
import java.util.List;

public class ParrotController extends ControllerMove {
    private EntityPet entity;
    private PetOwner owner;

    public ParrotController(EntityPet entity) {
        super(entity);
        owner = PetOwner.getPetOwner(entity.getOwner());
        this.entity = entity;
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
    public void a() { // onUpdateMoveHelper
        if (owner == null) return;
        if (owner.getPlayer() == null) return;
        if (!owner.getPlayer().isOnline()) return;
        if (!owner.hasPet()) return;

        double posX = this.b;
        double posY = this.c;
        double posZ = this.d;
        double speed = this.e;
        List<Block> checks = new ArrayList<>();
        if (owner.getPlayer().isFlying()) {
            Location playerLoc = owner.getPlayer().getLocation().clone();
            for (int i = 0; i < 2; i++) {
                Block below = playerLoc.subtract(0, i, 0).getBlock();
                if ((below == null) || (below.getType() == Material.AIR)) checks.add(below);
            }
            Location entityLoc = entity.getBukkitEntity().getLocation().clone();
            for (int i = 0; i < 2; i++) {
                Block below = entityLoc.subtract(0, i, 0).getBlock();
                if ((below == null) || (below.getType() == Material.AIR)) checks.add(below);
            }
        }

        if ((this.h == Operation.MOVE_TO) || (!checks.isEmpty())) {
            this.h = Operation.WAIT;
            entity.setNoGravity(true);
            double var1 = posX - entity.locX;
            double var3 = posY - entity.locY;
            double var5 = posZ - entity.locZ;
            double var7 = var1 * var1 + var3 * var3 + var5 * var5;
            if (var7 < 2.500000277905201E-7D) {
                entity.s(0.0F);
                entity.r(0.0F);
                return;
            }

            float var9 = (float) (MathHelper.d(var5, var1) * 57.2957763671875D) - 90.0F;
            entity.yaw = this.a(entity.yaw, var9, 10.0F);
            float var10;
            if (entity.onGround) {
                var10 = (float) (speed * entity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
            } else {
                var10 = (float) (speed * entity.getAttributeInstance(GenericAttributes.FLYING_SPEED).getValue());
            }

            entity.o(var10);
            double var11 = (double) MathHelper.sqrt(var1 * var1 + var5 * var5);
            float var13 = (float) (-(MathHelper.d(var3, var11) * 57.2957763671875D));
            entity.pitch = this.a(entity.pitch, var13, 10.0F);
            entity.s(var3 > 0.0D ? var10 : -var10);
        } else {
            entity.setNoGravity(false);
            entity.s(0.0F);
            entity.r(0.0F);
        }
    }
}