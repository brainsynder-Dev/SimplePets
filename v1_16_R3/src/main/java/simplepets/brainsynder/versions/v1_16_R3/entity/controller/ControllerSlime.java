package simplepets.brainsynder.versions.v1_16_R3.entity.controller;

import net.minecraft.server.v1_16_R3.ControllerMove;
import net.minecraft.server.v1_16_R3.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import simplepets.brainsynder.versions.v1_16_R3.entity.list.EntitySlimePet;

import java.util.Random;

public class ControllerSlime extends ControllerMove {
    private float yRot;
    private int jumpDelay;
    private final EntitySlimePet slimePet;
    private int lastJump;

    public ControllerSlime(EntitySlimePet entityslime) {
        super(entityslime);
        this.slimePet = entityslime;
        this.yRot = 180.0F * entityslime.yaw / 3.1415927F;
    }

    public void a(float f) {
        this.yRot = f;
    }

    public void a(double d0) {
        this.e = d0;
        this.h = Operation.MOVE_TO;
    }

    public void a() {
        this.a.a(((CraftPlayer) Bukkit.getPlayer(slimePet.getOwnerUUID())).getHandle(), 10, 10);
        this.yRot = this.a.yaw;
        this.a.yaw = this.a(this.a.yaw, this.yRot, 90.0F);
        this.a.aC = this.a.yaw;
        this.a.aA = this.a.yaw;
        if (this.h != Operation.MOVE_TO && lastJump < 60) {
            this.a.t(0.0F);
            lastJump++;
        } else {
            this.h = Operation.WAIT;
            if (this.a.isOnGround() || lastJump > 60) {
                this.a.q((float)(this.e * this.a.b(GenericAttributes.MOVEMENT_SPEED)));
                if (this.jumpDelay-- <= 0) {
                    this.jumpDelay = new Random().nextInt(20) + 10;
                    this.jumpDelay /= 3;
                    this.slimePet.getControllerJump().jump();
                    if (this.slimePet.getSize() > 0) {
                        this.slimePet.playJumpSound();
                    }
                    lastJump = 0;
                } else {
                    this.slimePet.aR = 0.0F;
                    this.slimePet.aT = 0.0F;
                    this.a.q(0.0F);
                    lastJump++;
                }
            } else {
                lastJump++;
                this.a.q((float)(this.e * this.a.b(GenericAttributes.MOVEMENT_SPEED)));
            }
        }

    }
}
