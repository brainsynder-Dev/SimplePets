package simplepets.brainsynder.versions.v1_16_R3.entity.controller;

import net.minecraft.server.v1_16_R3.ControllerJump;
import simplepets.brainsynder.versions.v1_16_R3.entity.list.EntityRabbitPet;

public class ControllerJumpRabbit extends ControllerJump {
        private final EntityRabbitPet rabbitPet;

        // Translation: UNKNOWN
        private boolean d = false;

        public ControllerJumpRabbit(EntityRabbitPet entity) {
            super(entity);
            this.rabbitPet = entity;
        }

        public boolean isActive() {
            return this.a;
        }

        // Translation: UNKNOWN
        public boolean d() {
            return this.d;
        }

        // Translation: UNKNOWN
        public void a(boolean flag) {
            this.d = flag;
        }

        // Translation: tick()
        public void b() {
            // Translation: this.active
            if (this.a) {
                this.rabbitPet.reseter();
                // Translation: this.active
                this.a = false;
            }

        }
    }