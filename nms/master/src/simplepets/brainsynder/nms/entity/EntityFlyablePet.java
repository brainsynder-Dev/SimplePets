package simplepets.brainsynder.nms.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import simplepets.brainsynder.api.entity.misc.IFlyableEntity;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.VersionTranslator;

public abstract class EntityFlyablePet extends EntityPetOverride implements IFlyableEntity {
    public EntityFlyablePet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
        this.moveControl = new FlyingMoveControl(this, 20, false);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        VersionTranslator.setupFlyingNavigation(this, level, navigation);
        return navigation;
    }

    @Override
    public void travel(Vec3 vec3) {
        if (isOwnerRiding()) {
            super.travel(vec3);
            VersionTranslator.calculateEntityAnimation(this, false);
            return;
        }
        if (this.isInWater()) {
            this.moveRelative(0.02F, vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.800000011920929D));
        } else if (this.isInLava()) {
            this.moveRelative(0.02F, vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
        } else {
            this.moveRelative(this.getSpeed(), vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8100000262260437D));
        }

        VersionTranslator.calculateEntityAnimation(this, false);
    }
}
