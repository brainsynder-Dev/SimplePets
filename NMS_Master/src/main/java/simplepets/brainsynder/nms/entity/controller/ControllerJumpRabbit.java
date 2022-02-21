package simplepets.brainsynder.nms.entity.controller;

import net.minecraft.world.entity.ai.control.JumpControl;
import simplepets.brainsynder.nms.entity.list.EntityRabbitPet;

public class ControllerJumpRabbit extends JumpControl {
        private final EntityRabbitPet rabbitPet;

        // Translation: UNKNOWN
        private boolean canJump = false;

        public ControllerJumpRabbit(EntityRabbitPet entity) {
            super(entity);
            this.rabbitPet = entity;
        }

        public boolean wantJump() {
            return this.jump;
        }

        // Translation: UNKNOWN
        public boolean canJump() {
            return this.canJump;
        }

        // Translation: UNKNOWN
        public void setCanJump(boolean flag) {
            this.canJump = flag;
        }

        // Translation: tick()
        public void tick() {
            // Translation: this.active
            if (this.jump) {
                this.rabbitPet.reseter();
                // Translation: this.active
                this.jump = false;
            }

        }
    }