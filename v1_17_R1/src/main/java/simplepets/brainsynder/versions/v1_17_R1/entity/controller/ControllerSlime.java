package simplepets.brainsynder.versions.v1_17_R1.entity.controller;

import net.minecraft.server.v1_16_R3.ControllerMove;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.versions.v1_17_R1.entity.list.EntitySlimePet;

import java.util.Random;

public class ControllerSlime extends ControllerMove {
    private float yRot;
    private int jumpDelay;
    private final EntitySlimePet slimePet;
    private int lastJump; // Slime sometimes gets stuck when going around fences so I implemented this variable to patch it

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
        // Store owner
        EntityPlayer owner = ((CraftPlayer) Bukkit.getPlayer(slimePet.getOwnerUUID())).getHandle();
        // Store the stopping distance
        int stoppingDistance = PetCore.getInstance().getConfiguration().getInt("Pathfinding.Stopping-Distance");
        // Rotate the slime's position so it can look to the player
        this.a.a(owner, 10, 10);
        this.yRot = this.a.yaw;
        // Turn left or right depending on which is closer
        this.a.yaw = this.a(this.a.yaw, this.yRot, 90.0F);
        this.a.aC = this.a.yaw;
        this.a.aA = this.a.yaw;
        // Let the slime idle if 1. It isn't prompted to move, 2. It isn't stuck, 3. It is close to its owner
        if (this.h != Operation.MOVE_TO && lastJump < 60 && slimePet.h(owner) <= stoppingDistance) {
            this.a.t(0.0F);
            lastJump++;
        } else {
            this.h = Operation.WAIT;
            this.a.q((float)(this.e * this.a.b(GenericAttributes.MOVEMENT_SPEED)));
            // If the slime is on the ground or simply stuck,
            if (this.a.isOnGround() || lastJump > 60) {
                if (this.jumpDelay-- <= 0) {
                    // Reset the jump delay (shortened since otherwise the slime is too slow)
                    this.jumpDelay = (new Random().nextInt(20) + 10) / 3;
                    // Make it jump and play its sound
                    this.slimePet.getControllerJump().jump();
                    this.slimePet.playJumpSound();
                    // Reset the last jump timer
                    lastJump = 0;
                } else {
                    this.slimePet.aR = 0.0F;
                    this.slimePet.aT = 0.0F;
                    this.a.q(0.0F);
                    lastJump++;
                }
            } else {
                lastJump++;
            }
        }

    }
}
