package simplepets.brainsynder.nms.entity.branch;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bsdevelopment.pluginutils.sound.SafeSound;
import org.bukkit.Sound;
import simplepets.brainsynder.api.entity.misc.IEntityWizard;
import simplepets.brainsynder.api.pet.PetDataRegistry;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.WizardSpell;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.monster.illager.SpellcasterIllager}
 */
public class EntityIllagerWizardPet extends EntityIllagerAbstractPet implements IEntityWizard {
    private static final EntityDataAccessor<Byte> SPELL = SynchedEntityData.defineId(EntityIllagerWizardPet.class, EntityDataSerializers.BYTE);

    public EntityIllagerWizardPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("spell", getSpell().name());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(SPELL, (byte) 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setEnum(PetDataRegistry.SPELL.namespace(), getSpell());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(PetDataRegistry.SPELL.namespace())) setSpell(object.getEnum(PetDataRegistry.SPELL.namespace(), WizardSpell.class, WizardSpell.NONE));
        super.applyCompound(object);
    }

    @Override
    public WizardSpell getSpell() {
        return WizardSpell.fromID(this.entityData.get(SPELL));
    }

    @Override
    public void setSpell(WizardSpell spell) {
        this.entityData.set(SPELL, (byte) spell.getId());
        if (spell == WizardSpell.WOLOLO) {
            SafeSound.of(Sound.ENTITY_EVOKER_PREPARE_WOLOLO).playAt(getEntity().getLocation(), 1f, 1f);
        } else {
            SafeSound.of(Sound.ENTITY_EVOKER_CAST_SPELL).playAt(getEntity().getLocation(), 1f, 1f);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.entityData.get(SPELL) > 0) {
            WizardSpell spell = this.getSpell();
            double velocityX = spell.getArray()[0];
            double velocityY = spell.getArray()[1];
            double velocityZ = spell.getArray()[2];

            // Translation: aA = bodyYaw
            float value = this.yBodyRot * 0.017453292F + Mth.cos(this.tickCount * 0.6662F) * 0.25F;
            float offsetX = Mth.cos(value);
            float offsetZ = Mth.sin(value);
//            level().addParticle(ParticleTypes.ENTITY_EFFECT, getX() + offsetX * 0.6D, getY() + 1.8D, getZ() + offsetZ * 0.6D, velocityX, velocityY, velocityZ);
//            level().addParticle(ParticleTypes.ENTITY_EFFECT, getX() - offsetX * 0.6D, getY() + 1.8D, getZ() - offsetZ * 0.6D, velocityX, velocityY, velocityZ);
        }
    }
}
