package simplepets.brainsynder.nms.entity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import simplepets.brainsynder.api.entity.misc.IWaterEntity;
import simplepets.brainsynder.api.event.entity.PetMoveEvent;
import simplepets.brainsynder.api.event.entity.movment.PetJumpEvent;
import simplepets.brainsynder.api.event.entity.movment.PetRideEvent;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.nms.helper.VersionHelper;

public class EntityPetOverride extends EntityPet {
    public EntityPetOverride(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    public void travel(Vec3 vec3d) {
        if ((getPetType() == null) || (getUser() == null)) return;

        if ((passengers == null)
                || (!isOwnerRiding())) {
            super.travel(vec3d);
            return;
        }

        ServerPlayer passenger = VersionHelper.getEntityHandle(getUser().getPlayer());

        if (doIndirectAttach) {
            if (getFirstPassenger() instanceof SeatEntity seat) {
                // orient the seat entity correctly. Seems to fix the issue
                // where ridden horses are not oriented properly
                seat.setYRot(passenger.getYRot());
                seat.yRotO = this.getYRot();
                seat.setXRot(passenger.getXRot() * 0.5F);
                seat.setRot(this.getYRot(), this.getXRot());
                seat.yHeadRot = this.yBodyRot = this.getYRot();
            }
        }

        this.setYRot(passenger.getYRot());
        this.yRotO = this.getYRot();
        this.setXRot(passenger.getXRot() * 0.5F);
        this.setRot(this.getYRot(), this.getXRot());
        this.yHeadRot = this.yBodyRot = this.getYRot();

        Input clientInput = passenger.getLastClientInput();
        double xxa = (clientInput.left() == clientInput.right() ? 0 : (clientInput.left() ? 1 : -1));
        double zza = (clientInput.forward() == clientInput.backward() ? 0 : (clientInput.forward() ? 1 : -1));

        double strafe = xxa * 0.5;
        double vertical = vec3d.y;
        double forward = zza;
        if (forward <= 0) {
            forward *= 0.25F;
        }

        PetMoveEvent moveEvent = new PetRideEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(moveEvent);
        if (moveEvent.isCancelled()) return;

        double speed = getAttribute(Attributes.MOVEMENT_SPEED).getValue();

        if (!passengers.isEmpty()) {
            SimplePets.getPetConfigManager().getPetConfig(getPetType()).ifPresent(config -> {
                try {
                    boolean flight = false;
                    double height = getJumpHeight();

                    if (config.canFly(getUser().getPlayer())) {
                        flight = true;
                        height = 0.3;
                    }

                    PetJumpEvent jumpEvent = new PetJumpEvent(this, height);
                    Bukkit.getServer().getPluginManager().callEvent(jumpEvent);
                    if ((!jumpEvent.isCancelled()) && clientInput.jump()) {
                        if (flight || this.onGround) {
                            setDeltaMovement(getDeltaMovement().x, jumpEvent.getJumpHeight(), getDeltaMovement().z);
                            this.needsSync = true;
                        }
                    }
                } catch (IllegalArgumentException | IllegalStateException ignored) {
                }
            });
        }

        if (isInWater() && !this.onGround) {
            if (this instanceof IWaterEntity) {
                float pitchRad = (float) Math.toRadians(passenger.getXRot());
                double pitchVertical = -Math.sin(pitchRad) * Math.abs(forward);
                setDeltaMovement(Vec3.ZERO);
                moveRelative((float) waterSpeed, new Vec3(strafe, pitchVertical, forward));
            } else {
                double yForce;
                if (isUnderWater()) {
                    yForce = 0.06;
                } else if (clientInput.jump()) {
                    yForce = getJumpHeight();
                } else {
                    double phase = (getId() % 20) * (Math.PI / 10.0);
                    yForce = Math.sin(level().getGameTime() * 0.1 + phase) * 0.03;
                }
                setDeltaMovement(0, yForce, 0);
                moveRelative((float) waterSpeed, new Vec3(strafe, 0, forward));
            }
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.8));
            calculateEntityAnimation(false);
            return;
        }

        if (isFlightEnabled()) {
            float pitchRad = (float) Math.toRadians(passenger.getXRot());
            double pitchVertical = -Math.sin(pitchRad) * Math.abs(forward);
            setDeltaMovement(Vec3.ZERO);
            moveRelative((float) flySpeed, new Vec3(strafe, pitchVertical, forward));
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.8));
            calculateEntityAnimation(false);
            return;
        }

        this.setSpeed((float) speed);
        super.travel(new Vec3(strafe, vertical, forward));
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        if (getOwnerUUID().equals(player.getUUID())) {
            if (ConfigOption.MISC_TOGGLES_DISABLE_CLICKING.get()) return InteractionResult.FAIL;
            if (InventoryManager.PET_DATA.getType((org.bukkit.entity.Player) player.getBukkitEntity()) != getPetType())
                InventoryManager.PET_DATA.setType((org.bukkit.entity.Player) player.getBukkitEntity(), getPetType());
            InventoryManager.PET_DATA.open(getPetUser());
        }
        return super.interact(player, hand, location);
    }
}
