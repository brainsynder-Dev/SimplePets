package simplepets.brainsynder.versions.v1_17_R1.entity.controller;

import net.minecraft.server.v1_16_R3.ControllerMove;
import simplepets.brainsynder.versions.v1_17_R1.entity.list.EntityRabbitPet;

public class ControllerMoveRabbit extends ControllerMove {
        private final EntityRabbitPet rabbit;
        private double rabbitSpeed;

        public ControllerMoveRabbit(EntityRabbitPet entityrabbit) {
            super(entityrabbit);
            this.rabbit = entityrabbit;
        }

        // Translation: tick()
        public void a() {
            if (this.rabbit.isOnGround() && !this.rabbit.isJumping() && !((ControllerJumpRabbit)this.rabbit.getControllerJump()).isActive()) {
                this.rabbit.setSpeed(0.0);
            } else if (this.b()) { // Translation: this.isMoving()
                this.rabbit.setSpeed(this.rabbitSpeed);
            }

            super.a();
        }

        // Translation: moveTo
        public void a(double x, double y, double z, double speed) {
            if (this.rabbit.isInWater()) speed = 1.5D;

            super.a(x, y, z, speed);

            if (speed > 0.0D) this.rabbitSpeed = speed;
        }
    }