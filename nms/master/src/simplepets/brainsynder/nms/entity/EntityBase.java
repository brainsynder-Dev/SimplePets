package simplepets.brainsynder.nms.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.list.EntityRabbitPet;
import simplepets.brainsynder.nms.entity.list.EntitySlimePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

public class EntityBase extends Mob {
    protected  final EntityType<? extends Mob> entityType;
    protected  final EntityType<? extends Mob> originalEntityType;
    private PetUser user;
    private PetType petType;

    protected EntityBase(EntityType<? extends Mob> entitytypes, Level world) {
        super(entitytypes, world);
        entityType = getEntityType(entitytypes);
        originalEntityType = entitytypes;
        VersionTranslator.getBukkitEntity(this).remove();
    }

    public EntityBase(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, VersionTranslator.getWorldHandle(user.getPlayer().getLocation().getWorld()));
        this.user = user;
        this.petType = type;
        entityType = getEntityType(entitytypes);
        originalEntityType = entitytypes;
    }

    @Override
    public void jumpFromGround() {
        if (this instanceof EntitySlimePet) {
            Vec3 vec3d = this.getDeltaMovement();
            this.setDeltaMovement(vec3d.x, this.getJumpPower(), vec3d.z);
            this.needsSync = true;
            return;
        }

        super.jumpFromGround();

        if (this instanceof EntityRabbitPet) {
            double speed = this.moveControl.getSpeedModifier();
            if (speed > 0.0D) {
                double length = getDeltaMovement().horizontalDistanceSqr();
                if (length < 0.01D) {
                    this.moveRelative(0.1F, new Vec3(0.0D, 0.0D, 1.0D));
                }
            }

            if (!VersionTranslator.getEntityLevel(this).isClientSide()) VersionTranslator.getEntityLevel(this).broadcastEntityEvent(this, (byte)1);
        }
    }

    public void populateDataAccess(PetDataAccess dataAccess) {}

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder datawatcher) {
        super.defineSynchedData(datawatcher);

        PetDataAccess dataAccess = new PetDataAccess();
        populateDataAccess(dataAccess);
        dataAccess.getAccessorDefinitions().forEach(datawatcher::define);
    }


    // 1.19.4+   Replaces boolean rideableUnderWater()
    @Override
    public boolean dismountsUnderwater() {
        return false;
    }

    public PetType getPetType() {
        return petType;
    }

    public PetUser getUser() {
        return user;
    }

    EntityType<? extends Mob> getEntityType(EntityType<? extends Mob> originalType)  {
        return null;
    }
}
