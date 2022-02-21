package simplepets.brainsynder.nms.entity.controller;

import net.minecraft.world.entity.ai.control.MoveControl;
import simplepets.brainsynder.nms.entity.list.EntityRabbitPet;

public class ControllerMoveRabbit extends MoveControl {
        private final EntityRabbitPet rabbit;
        private double rabbitSpeed;

        public ControllerMoveRabbit(EntityRabbitPet entityrabbit) {
            super(entityrabbit);
            this.rabbit = entityrabbit;
        }

        // Translation: tick()
        public void tick() {
            if (this.rabbit.isOnGround() && !this.rabbit.isJumping() && !((ControllerJumpRabbit)this.rabbit.getJumpControl()).wantJump()) {
                this.rabbit.setSpeedModifier(0.0);
            } else if (hasWanted()) { // Translation: this.isMoving()
                this.rabbit.setSpeedModifier(this.rabbitSpeed);
            }

            super.tick();
        }

        // Translation: moveTo
        public void setWantedPosition(double x, double y, double z, double speed) {
            if (this.rabbit.isInWater()) speed = 1.5D;

            super.setWantedPosition(x, y, z, speed);

            if (speed > 0.0D) this.rabbitSpeed = speed;
        }
    }