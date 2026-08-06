package simplepets.brainsynder.api.pet;

import org.bsdevelopment.pluginutils.inventory.ItemBuilder;
import org.bsdevelopment.pluginutils.text.WordUtils;
import org.bsdevelopment.pluginutils.version.ServerVersion;
import org.bukkit.Material;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.*;
import simplepets.brainsynder.api.entity.misc.*;
import simplepets.brainsynder.api.entity.passive.*;
import simplepets.brainsynder.api.pet.data.SizeData;
import simplepets.brainsynder.api.pet.data.TemperatureVariantData;
import simplepets.brainsynder.api.pet.data.color.ColorData;
import simplepets.brainsynder.api.pet.data.color.ResetColorData;
import simplepets.brainsynder.api.pet.data.tropicalfish.BodyColorData;
import simplepets.brainsynder.api.pet.data.tropicalfish.PatternColorData;
import simplepets.brainsynder.api.pet.data.tropicalfish.PatternData;
import simplepets.brainsynder.api.wrappers.*;
import simplepets.brainsynder.api.wrappers.horse.HorseArmorType;
import simplepets.brainsynder.api.wrappers.horse.HorseColorType;
import simplepets.brainsynder.api.wrappers.horse.HorseStyleType;
import simplepets.brainsynder.api.wrappers.villager.BiomeType;
import simplepets.brainsynder.api.wrappers.villager.VillagerLevel;
import simplepets.brainsynder.api.wrappers.villager.VillagerType;

public interface PetDataRegistry {
    PetData<IAgeablePet> BABY = PetData.of("baby", IAgeablePet.class)
            .defaultValue(false)
            .item(true, ItemBuilder.of(Material.WHEAT).withName("&#c8c8c8{name}: &atrue"))
            .item(false, ItemBuilder.of(Material.WHEAT).withName("&#c8c8c8{name}: &cfalse"))
            .onToggle(entityPet -> entityPet.setBabySafe(!entityPet.isBabySafe()))
            .value(IAgeablePet::isBabySafe).build();

    PetData<IEntityPet> BURNING = PetData.of("burning", IEntityPet.class)
            .defaultValue(false)
            .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/4080bbefca87dc0f36536b6508425cfc4b95ba6e8f5e6a46ff9e9cb488a9ed").withName("&#c8c8c8{name}: &atrue"))
            .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/f946443fa0039354edd31a70c749c4f963464744dc20b79137bd9910356ee90").withName("&#c8c8c8{name}: &cfalse"))
            .onToggle(entityPet -> {
                if (entityPet instanceof IEntityControllerPet controller) {
                    controller.getVisibleEntity().setBurning(!controller.getVisibleEntity().isBurning());
                    return;
                }
                entityPet.setBurning(!entityPet.isBurning());
            }).value(entityPet -> {
                if (entityPet instanceof IEntityControllerPet controller)
                    return controller.getVisibleEntity().isBurning();
                return entityPet.isBurning();
            }).build();

    PetData<IEntityPet> FROZEN = PetData.of("frozen", IEntityPet.class)
            .defaultValue(false)
            .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/43c52eae747cad5b4fd19b1a23b39a336b62ed422797a622d045f43e5d38").withName("&#c8c8c8{name}: &atrue"))
            .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/8449b9318e33158e64a46ab0de121c3d40000e3332c1574932b3c849d8fa0dc2").withName("&#c8c8c8{name}: &cfalse"))
            .onToggle(entityPet -> {
                if (entityPet instanceof IEntityControllerPet controller) {
                    controller.getVisibleEntity().setFrozen(!controller.getVisibleEntity().isFrozen());
                    return;
                }
                entityPet.setFrozen(!entityPet.isFrozen());
            }).value(entityPet -> {
                if (entityPet instanceof IEntityControllerPet controller)
                    return controller.getVisibleEntity().isFrozen();
                return entityPet.isFrozen();
            }).build();

    PetData<IEntityPet> SILENT = PetData.of("silent", IEntityPet.class)
            .defaultValue(false)
            .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/b1f327c3f349158d209b4867d68ffb1890bc57a01ba9483e3fffe4ec7fdea0b0").withName("&#c8c8c8{name}: &atrue"))
            .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/5461518b74d5f7016f72294756fc68c5471110cc97f3bb093e0c6ed94a9e3").withName("&#c8c8c8{name}: &cfalse"))
            .onToggle(entityPet -> {
                if (entityPet instanceof IEntityControllerPet controller) {
                    controller.getVisibleEntity().setPetSilent(!controller.getVisibleEntity().isPetSilent());
                    return;
                }
                entityPet.setPetSilent(!entityPet.isPetSilent());
            }).value(entityPet -> {
                if (entityPet instanceof IEntityControllerPet controller)
                    return controller.getVisibleEntity().isPetSilent();
                return entityPet.isPetSilent();
            }).build();

    PetData<IEntityPet> VISIBLE = PetData.of("visible", IEntityPet.class)
            .defaultValue(false).enabledByDefault(false)
            .item(false, ItemBuilder.of(Material.POTION).handleMeta(PotionMeta.class, potionMeta -> {
                potionMeta.setBasePotionType(PotionType.INVISIBILITY);
                return potionMeta;
            }).withName("&#c8c8c8{name}: &atrue"))
            .item(true, ItemBuilder.of(Material.GLASS_BOTTLE).withName("&#c8c8c8{name}: &cfalse"))
            .onToggle(entityPet -> {
                if (entityPet instanceof IEntityControllerPet controller) {
                    controller.getVisibleEntity().setPetVisible(!controller.getVisibleEntity().isPetVisible());
                    return;
                }
                entityPet.setPetVisible(!entityPet.isPetVisible());
            }).value(entityPet -> {
                if (entityPet instanceof IEntityControllerPet controller)
                    return controller.getVisibleEntity().isPetVisible();
                return entityPet.isPetVisible();
            }).build();

    PetData<IEntityPet> HALF_SCALE = PetData.of("half_scale", IEntityPet.class)
            .defaultValue(false).enabledByDefault(false)
            .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/5aa7ebadfd28e58d8b8c1c595b09ff0101989f79ad6cdeb16aaed2a809874").withName("&#c8c8c8{name}: &atrue"))
            .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/76fdd4b13d54f6c91dd5fa765ec93dd9458b19f8aa34eeb5c80f455b119f278").withName("&#c8c8c8{name}: &cfalse"))
            .onToggle(entityPet -> entityPet.setPetScale(entityPet.isFullSize() ? 0.5 : 1.0))
            .value(entityPet -> !entityPet.isFullSize()).build();

    PetData<ISitting> SITTING = PetData.of("sitting", ISitting.class)
            .defaultValue(false)
            .item(true, ItemBuilder.of(Material.OAK_STAIRS).withName("&#c8c8c8{name}: &atrue"))
            .item(false, ItemBuilder.of(Material.OAK_STAIRS).withName("&#c8c8c8{name}: &cfalse"))
            .onToggle(entityPet -> entityPet.setSitting(!entityPet.isSitting()))
            .value(ISitting::isSitting).build();

    PetData<ITameable> TAMED = PetData.of("tamed", ITameable.class)
            .defaultValue(false)
            .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/82d16e0f17cb1ca2ebefa4d3126b04b0312444f30b77b2366d7e544e573e334a").withName("&#c8c8c8{name}: &atrue"))
            .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/28d408842e76a5a454dc1c7e9ac5c1a8ac3f4ad34d6973b5275491dff8c5c251").withName("&#c8c8c8{name}: &cfalse"))
            .onToggle(entityPet -> entityPet.setTamed(!entityPet.isTamed()))
            .value(ITameable::isTamed).build();

    PetData<ISaddle> SADDLE = PetData.of("saddled", ISaddle.class)
            .defaultValue(false)
            .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/26b92a05b40a5cdae904b393930ab064c4a8d4c590392668352270bb472df70").withName("&#c8c8c8{name}: &atrue"))
            .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/d09bb9264b0709310bcd083ca682a081181687766623e8c2a016e3cc0c6ff4a6").withName("&#c8c8c8{name}: &cfalse"))
            .onToggle(entityPet -> entityPet.setPetSaddled(!entityPet.isPetSaddled()))
            .value(ISaddle::isPetSaddled).build();

    PetData<IShaking> SHAKE = PetData.of("shaking", IShaking.class)
            .defaultValue(false)
            .item(true, ItemBuilder.of(Material.ENCHANTED_GOLDEN_APPLE).withName("&#c8c8c8{name}: &atrue"))
            .item(false, ItemBuilder.of(Material.APPLE).withName("&#c8c8c8{name}: &cfalse"))
            .onToggle(entityPet -> entityPet.setShaking(!entityPet.isShaking()))
            .value(IShaking::isShaking).build();

    PetData<ISleeper> SLEEP = PetData.of("sleep", ISleeper.class)
            .defaultValue(false)
            .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/5c819a42c0b1c25b9e2e0bc1ebb574b4d6690777d3e831d82d3c932116c02bdc").withName("&#c8c8c8{name}: &atrue"))
            .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/cb80600254ae8a6b65e2c26dca71d1fea5cf01679232d26ae658e64d6c3a0212").withName("&#c8c8c8{name}: &cfalse"))
            .onToggle(entityPet -> entityPet.setPetSleeping(!entityPet.isPetSleeping()))
            .value(ISleeper::isPetSleeping).build();

    PetData<IColorable> COLOR = new ColorData();
    PetData<IResetColor> RESET_COLOR = new ResetColorData();
    PetData<IRainbow> RAINBOW = PetData.of("rainbow", IRainbow.class)
            .defaultValue(false)
            .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/33577dac43f4bd8bf40be87342e2f1576bebbdbf86d7cbf3c6be65d4927095c5").withName("&#c8c8c8{name}: &atrue"))
            .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/72b6732199faf1b2447725e56829090fbceb6c2b514953862ff03c16b53f3599").withName("&#c8c8c8{name}: &cfalse"))
            .onToggle(entityPet -> entityPet.setRainbow(!entityPet.isRainbow()))
            .value(IRainbow::isRainbow).build();


    PetData<ISizable> SIZE = new SizeData();

    PetData<ISheared> SHEAR = PetData.of("sheared", ISheared.class)
            .defaultValue(false)
            .item(true, ItemBuilder.of(Material.SHEARS).withName("&#c8c8c8{name}: &atrue"))
            .item(false, ItemBuilder.of(Material.SHEARS).withName("&#c8c8c8{name}: &cfalse"))
            .onToggle(entityPet -> entityPet.setSheared(!entityPet.isSheared()))
            .value(ISheared::isSheared).build();

    PetData<IPowered> POWERED = PetData.of("powered", IPowered.class)
            .defaultValue(false)
            .item(true, ItemBuilder.of(Material.GUNPOWDER).withName("&#c8c8c8{name}: &atrue"))
            .item(false, ItemBuilder.of(Material.GUNPOWDER).withName("&#c8c8c8{name}: &cfalse"))
            .onToggle(entityPet -> entityPet.setPowered(!entityPet.isPowered()))
            .value(IPowered::isPowered).build();

    PetData<IEntityWizard> SPELL = PetData.of("spell", IEntityWizard.class)
            .defaultValue(WizardSpell.NONE)
            .items(WizardSpell.values(), spell -> spell.getIcon().withName("&#c8c8c8{name}: &a" + WordUtils.capitalize(spell.name().toLowerCase().replace('_', ' '))))
            .onLeftClick(entityPet -> entityPet.setSpell(PetData.cycleForward(entityPet.getSpell(), WizardSpell.values())))
            .onRightClick(entityPet -> entityPet.setSpell(PetData.cycleBackward(entityPet.getSpell(), WizardSpell.values())))
            .value(IEntityWizard::getSpell).build();

    PetData<IEntityPet> ARMS = PetData.of("raised_arms", IEntityPet.class)
            .defaultValue(false)
            .item(true, ItemBuilder.of(Material.STICK).withName("&#c8c8c8{name}: &atrue"))
            .item(false, ItemBuilder.of(Material.STICK).withName("&#c8c8c8{name}: &cfalse"))
            .onToggle(entityPet -> {
                if (entityPet instanceof IEntityZombiePet zombie) {
                    zombie.setArmsRaised(!zombie.isArmsRaised());
                } else if (entityPet instanceof ISkeletonAbstract skeleton) {
                    skeleton.setArmsRaised(!skeleton.isArmsRaised());
                }
            }).value(entityPet -> {
                if (entityPet instanceof IEntityZombiePet zombie) {
                    return zombie.isArmsRaised();
                } else if (entityPet instanceof ISkeletonAbstract skeleton) {
                    return skeleton.isArmsRaised();
                }
                return false;
            }).build();



    interface Allay {
        PetData<IEntityAllayPet> DANCING = PetData.of("dancing", IEntityAllayPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/98896605e41a1f4e2c3c92a964f391f4e61390cb10af2c0fab615a5d34e61074").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/b3e7bba47b64f458579db865daeea4d6f8a4034153a543aedd8bf7ce0aeab7c8").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setDancing(!entityPet.isDancing()))
                .value(IEntityAllayPet::isDancing).build();
    }

    interface Armadillo {
        PetData<IEntityArmadilloPet> PHASE = PetData.of("phase", IEntityArmadilloPet.class)
                .defaultValue(ArmadilloPhase.STANDING)
                .items(ArmadilloPhase.values(), value -> value.getIcon().withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setPhase(PetData.cycleForward(entityPet.getPhase(), ArmadilloPhase.values())))
                .onRightClick(entityPet -> entityPet.setPhase(PetData.cycleBackward(entityPet.getPhase(), ArmadilloPhase.values())))
                .value(IEntityArmadilloPet::getPhase).build();
    }

    interface Axolotl {
        PetData<IEntityAxolotlPet> VARIANT = PetData.of("variant", IEntityAxolotlPet.class)
                .defaultValue(AxolotlVariant.LUCY)
                .items(AxolotlVariant.values(), value -> value.getIcon().withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setVariant(PetData.cycleForward(entityPet.getVariant(), AxolotlVariant.values())))
                .onRightClick(entityPet -> entityPet.setVariant(PetData.cycleBackward(entityPet.getVariant(), AxolotlVariant.values())))
                .value(IEntityAxolotlPet::getVariant).build();

        PetData<IEntityAxolotlPet> PLAY_DEAD = PetData.of("playing_dead", IEntityAxolotlPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/88fd654d856bde8b69f0c3567d28fafe94e71eae10d32ea59ee23e9bd64b41b0").withName("&#c8c8c8{name}: &atrue"))
                .item(false, AxolotlVariant.LUCY.getIcon().withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setPlayingDead(!entityPet.isPlayingDead()))
                .value(IEntityAxolotlPet::isPlayingDead).build();
    }

    interface Bat {
        PetData<IEntityBatPet> HANG = PetData.of("hanging", IEntityBatPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.of(Material.FEATHER).withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.of(Material.FEATHER).withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setHanging(!entityPet.isHanging()))
                .value(IEntityBatPet::isHanging).build();
    }

    interface Bee {
        PetData<IEntityBeePet> ANGRY = PetData.of("angry", IEntityBeePet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/e400223f1fa54741d421d7e8046409d5f3e15c7f4364b1b739940208f3b686d4").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/fa2cb74c13245d3ce9bacc8b1600af02fd7c91f501feaf97364e1f8b6f04f47f").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setAngry(!entityPet.isAngry()))
                .value(IEntityBeePet::isAngry).build();

        PetData<IEntityBeePet> NECTAR = PetData.of("nectar", IEntityBeePet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/b727d0ab03f5cd022f8705d3f7f133ca4920eae8e1e47b5074433a137e691e4e").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/fa2cb74c13245d3ce9bacc8b1600af02fd7c91f501feaf97364e1f8b6f04f47f").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setHasNectar(!entityPet.hasNectar()))
                .value(IEntityBeePet::hasNectar).build();

        PetData<IEntityBeePet> STINGER = PetData.of("stinger", IEntityBeePet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/fa2cb74c13245d3ce9bacc8b1600af02fd7c91f501feaf97364e1f8b6f04f47f").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/fa2cb74c13245d3ce9bacc8b1600af02fd7c91f501feaf97364e1f8b6f04f47f").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setHasStung(!entityPet.hasStung()))
                .value(IEntityBeePet::hasStung).build();

        PetData<IEntityBeePet> FLIPPED = PetData.of("flipped", IEntityBeePet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/fa2cb74c13245d3ce9bacc8b1600af02fd7c91f501feaf97364e1f8b6f04f47f").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/fa2cb74c13245d3ce9bacc8b1600af02fd7c91f501feaf97364e1f8b6f04f47f").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setFlipped(!entityPet.isFlipped()))
                .value(IEntityBeePet::isFlipped).build();
    }

    interface Cat {
        PetData<IEntityCatPet> TYPE = PetData.of("type", IEntityCatPet.class)
                .defaultValue(CatVariant.TABBY)
                .items(CatVariant.values(), value -> value.getIcon().withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setCatType(PetData.cycleForward(entityPet.getCatType(), CatVariant.values())))
                .onRightClick(entityPet -> entityPet.setCatType(PetData.cycleBackward(entityPet.getCatType(), CatVariant.values())))
                .value(IEntityCatPet::getCatType).build();

        PetData<IEntityCatPet> LOOK_UP = PetData.of("head_up", IEntityCatPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.of(Material.SKELETON_SKULL).withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.of(Material.SKELETON_SKULL).withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setHeadUp(!entityPet.isHeadUp()))
                .value(IEntityCatPet::isHeadUp).build();
    }

    interface Chicken {
        PetData<ITemperaturePet> VARIANT = new TemperatureVariantData.ChickenTemperature();
    }

    interface CopperGolem {
        PetData<IEntityCopperGolemPet> OXIDATION = PetData.of("oxidation", IEntityCopperGolemPet.class)
                .defaultValue(CopperGolemOxidation.UNAFFECTED)
                .items(CopperGolemOxidation.values(), value -> value.getIcon().withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setOxidation(PetData.cycleForward(entityPet.getOxidation(), CopperGolemOxidation.values())))
                .onRightClick(entityPet -> entityPet.setOxidation(PetData.cycleBackward(entityPet.getOxidation(), CopperGolemOxidation.values())))
                .value(IEntityCopperGolemPet::getOxidation).build();
    }

    interface Cow {
        PetData<ITemperaturePet> VARIANT = new TemperatureVariantData.CowTemperature();
    }

    interface Enderman {
        PetData<IEntityEndermanPet> SCREAM = PetData.of("screaming", IEntityEndermanPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/120baf2ed7f2326803165ad801fc056d002243be8ccf2d87ea26b9c76dc3fa6e").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/96c0b36d53fff69a49c7d6f3932f2b0fe948e032226d5e8045ec58408a36e951").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setScreaming(!entityPet.isScreaming()))
                .value(IEntityEndermanPet::isScreaming).build();
    }

    interface Fox {
        PetData<IEntityFoxPet> INTEREST = PetData.of("interested", IEntityFoxPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setInterested(!entityPet.isInterested()))
                .value(IEntityFoxPet::isInterested).build();

        PetData<IEntityFoxPet> CROUCHING = PetData.of("crouching", IEntityFoxPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/364371509aa11d648457665e44b089438a8a81f2b6710ad58eaaa036709297a1").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setCrouching(!entityPet.isCrouching()))
                .value(IEntityFoxPet::isCrouching).build();

        PetData<IEntityFoxPet> TYPE = PetData.of("type", IEntityFoxPet.class)
                .defaultValue(FoxVariant.RED)
                .item(FoxVariant.RED, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a").withName("&#c8c8c8{name}: &#ee7e46Red Fox"))
                .item(FoxVariant.WHITE, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/ddcd0db8cbe8f1e0ab1ec0a9385fb9288da84d3202c1c397da76ee1035e608b0").withName("&#c8c8c8{name}: &#eeebffSnow Fox"))
                .onToggle(entityPet -> entityPet.setFoxType(PetData.cycleForward(entityPet.getFoxType(), FoxVariant.values())))
                .value(IEntityFoxPet::getFoxType).build();

        PetData<IEntityFoxPet> SITTING = PetData.of("sitting", IEntityFoxPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.of(Material.OAK_STAIRS).withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.of(Material.OAK_STAIRS).withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setSitting(!entityPet.isSitting()))
                .value(IEntityFoxPet::isSitting).build();
    }

    interface Frog {
        PetData<ITemperaturePet> VARIANT = new TemperatureVariantData.FrogTemperature();

        PetData<IEntityFrogPet> CROAKING = PetData.of("croaking", IEntityFrogPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/1b20e0c01e2a241fb6fbf45045b9c9dbfecf745c62a8fda6eb6522fc2d53e2cf").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/1b20e0c01e2a241fb6fbf45045b9c9dbfecf745c62a8fda6eb6522fc2d53e2cf").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setCroaking(!entityPet.isCroaking()))
                .value(IEntityFrogPet::isCroaking).build();

        PetData<IEntityFrogPet> TONGUE = PetData.of("tongue", IEntityFrogPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/23ce6f9998ed2da757d1e6372f04efa20e57dfc17c3a06478657bbdf51c2f2a2").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/23ce6f9998ed2da757d1e6372f04efa20e57dfc17c3a06478657bbdf51c2f2a2").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setUsingTongue(!entityPet.isUsingTongue()))
                .value(IEntityFrogPet::isUsingTongue).build();
    }

    interface Ghast {
        PetData<IEntityGhastPet> SCREAM = PetData.of("screaming", IEntityGhastPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/78f77eeeef6ffb2f6818e57698794ae0351ab32ba234d621c22fe4ce8e1599d2").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/7a8b714d32d7f6cf8b37e221b758b9c599ff76667c7cd45bbc49c5ef19858646").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setScreaming(!entityPet.isScreaming()))
                .value(IEntityGhastPet::isScreaming).build();
    }

    interface GlowSquid {
        PetData<IEntityGlowSquidPet> GLOWING = PetData.of("glowing", IEntityGlowSquidPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/3e94a1bb1cb00aaa153a74daf4b0eea20b8974522fe9901eb55aef478ebeff0d").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/b4f546cd512da37c0282fe531e7cdb3c5dc35baa32696099e68d3b894c16d2c").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setSquidGlowing(!entityPet.isSquidGlowing()))
                .value(IEntityGlowSquidPet::isSquidGlowing).build();
    }

    interface Goat {
        PetData<IEntityGoatPet> LEFT_HORN = PetData.of("left-horn", IEntityGoatPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/957607099d06b7a8b1327093cd0a488be7c9f50b6121b22151271b59170f3c21").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/957607099d06b7a8b1327093cd0a488be7c9f50b6121b22151271b59170f3c21").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setLeftHorn(!entityPet.hasLeftHorn()))
                .value(IEntityGoatPet::hasLeftHorn).build();

        PetData<IEntityGoatPet> RIGHT_HORN = PetData.of("right-horn", IEntityGoatPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/957607099d06b7a8b1327093cd0a488be7c9f50b6121b22151271b59170f3c21").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/957607099d06b7a8b1327093cd0a488be7c9f50b6121b22151271b59170f3c21").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setRightHorn(!entityPet.hasRightHorn()))
                .value(IEntityGoatPet::hasRightHorn).build();
    }

    interface Horse {
        PetData<IChestedAbstractPet> CHEST = PetData.of("chest", IChestedAbstractPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.of(Material.CHEST_MINECART).withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.of(Material.MINECART).withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setChested(!entityPet.isChested()))
                .value(IChestedAbstractPet::isChested).build();

        PetData<IHorseAbstract> EATING = PetData.of("eating", IHorseAbstract.class)
                .defaultValue(false)
                .item(true, ItemBuilder.of(Material.APPLE).withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.of(Material.DEAD_BUSH).withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setEating(!entityPet.isEating()))
                .value(IHorseAbstract::isEating).build();

        PetData<IEntityHorsePet> ARMOR = PetData.of("armor", IEntityHorsePet.class)
                .defaultValue(HorseArmorType.NONE)
                .item(HorseArmorType.NONE, ItemBuilder.of(Material.BARRIER).withName("&#c8c8c8{name}: &aNONE"))
                .items(HorseArmorType.values(), armor -> (armor == HorseArmorType.NONE) || !armor.isSupported(), armor -> ItemBuilder.of(armor.itemType().asMaterial())
                        .withName("&#c8c8c8{name}: &a" + armor.name()))
                .onLeftClick(entityPet -> entityPet.setArmor(PetData.cycleForward(entityPet.getArmor(), PetData.filterValues(HorseArmorType.values(), horseArmorType -> !horseArmorType.isSupported()))))
                .onRightClick(entityPet -> entityPet.setArmor(PetData.cycleBackward(entityPet.getArmor(), PetData.filterValues(HorseArmorType.values(), horseArmorType -> !horseArmorType.isSupported()))))
                .value(IEntityHorsePet::getArmor).build();

        PetData<IEntityHorsePet> COLOR = PetData.of("color", IEntityHorsePet.class)
                .defaultValue(HorseColorType.WHITE)
                .items(HorseColorType.values(), value -> value.getIcon().withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setColor(PetData.cycleForward(entityPet.getColor(), HorseColorType.values())))
                .onRightClick(entityPet -> entityPet.setColor(PetData.cycleBackward(entityPet.getColor(), HorseColorType.values())))
                .value(IEntityHorsePet::getColor).build();

        PetData<IEntityHorsePet> STYLE = PetData.of("style", IEntityHorsePet.class)
                .defaultValue(HorseStyleType.NONE)
                .items(HorseStyleType.values(), value -> ItemBuilder.of(Material.LEAD).withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setStyle(PetData.cycleForward(entityPet.getStyle(), HorseStyleType.values())))
                .onRightClick(entityPet -> entityPet.setStyle(PetData.cycleBackward(entityPet.getStyle(), HorseStyleType.values())))
                .value(IEntityHorsePet::getStyle).build();
    }

    interface Llama {
        PetData<IEntityLlamaPet> SKIN = PetData.of("skin", IEntityLlamaPet.class)
                .defaultValue(LlamaColor.CREAMY)
                .items(LlamaColor.values(), value -> value.getIcon().withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setSkinColor(PetData.cycleForward(entityPet.getSkinColor(), LlamaColor.values())))
                .onRightClick(entityPet -> entityPet.setSkinColor(PetData.cycleBackward(entityPet.getSkinColor(), LlamaColor.values())))
                .value(IEntityLlamaPet::getSkinColor).build();
    }

    interface Mooshroom {
        PetData<IEntityMooshroomPet> COLOR = PetData.of("type", IEntityMooshroomPet.class)
                .defaultValue(MooshroomVariant.RED)
                .items(MooshroomVariant.values(), value -> value.getIcon().withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setMooshroomType(PetData.cycleForward(entityPet.getMooshroomType(), MooshroomVariant.values())))
                .onRightClick(entityPet -> entityPet.setMooshroomType(PetData.cycleBackward(entityPet.getMooshroomType(), MooshroomVariant.values())))
                .value(IEntityMooshroomPet::getMooshroomType).build();
    }

    interface Nautilus {
        PetData<IEntityNautilusPet> ARMOR = PetData.of("armor", IEntityNautilusPet.class)
                .defaultValue(NautilusArmorType.NONE).minVersion(ServerVersion.v1_21_11)
                .item(NautilusArmorType.NONE, ItemBuilder.of(Material.BARRIER).withName("&#c8c8c8{name}: &aNONE"))
                .items(NautilusArmorType.values(), armor -> (armor != NautilusArmorType.NONE), armor -> ItemBuilder.of(armor.itemType().asMaterial())
                        .withName("&#c8c8c8{name}: &a" + armor.name()))
                .onLeftClick(entityPet -> entityPet.setArmor(PetData.cycleForward(entityPet.getArmor(), NautilusArmorType.values())))
                .onRightClick(entityPet -> entityPet.setArmor(PetData.cycleBackward(entityPet.getArmor(), NautilusArmorType.values())))
                .value(IEntityNautilusPet::getArmor).build();
    }

    interface Panda {
        PetData<IEntityPandaPet> GENE = PetData.of("type", IEntityPandaPet.class)
                .defaultValue(PandaVariant.NORMAL)
                .items(PandaVariant.values(), value -> value.getIcon().withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setGene(PetData.cycleForward(entityPet.getGene(), PandaVariant.values())))
                .onRightClick(entityPet -> entityPet.setGene(PetData.cycleBackward(entityPet.getGene(), PandaVariant.values())))
                .value(IEntityPandaPet::getGene).build();

        PetData<IEntityPandaPet> SNEEZE = PetData.of("sneeze", IEntityPandaPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/5c2d25e956337d82791fa0e6617a40086f02d6ebfbfd5a6459889cf206fca787").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/dca096eea506301bea6d4b17ee1605625a6f5082c71f74a639cc940439f47166").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setSneezing(!entityPet.isSneezing()))
                .value(IEntityPandaPet::isSneezing).build();

        PetData<IEntityPandaPet> EATING = PetData.of("eating", IEntityPandaPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.of(Material.BAMBOO).withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.of(Material.BAMBOO).withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setEating(!entityPet.isEating()))
                .value(IEntityPandaPet::isEating).build();
    }

    interface Parrot {
        PetData<IEntityParrotPet> VARIANT = PetData.of("variant", IEntityParrotPet.class)
                .defaultValue(ParrotVariant.RED)
                .items(ParrotVariant.values(), value -> value.getIcon().withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setVariant(PetData.cycleForward(entityPet.getVariant(), ParrotVariant.values())))
                .onRightClick(entityPet -> entityPet.setVariant(PetData.cycleBackward(entityPet.getVariant(), ParrotVariant.values())))
                .value(IEntityParrotPet::getVariant).build();
    }

    interface Pig {
        PetData<ITemperaturePet> VARIANT = new TemperatureVariantData.PigTemperature();
    }

    interface Piglin {
        PetData<IEntityPiglinPet> CHARGING = PetData.of("charging", IEntityPiglinPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.of(Material.IRON_SWORD).withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.of(Material.IRON_SWORD).withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setCharging(!entityPet.isCharging()))
                .value(IEntityPiglinPet::isCharging).build();

        PetData<IEntityPiglinPet> DANCING = PetData.of("dancing", IEntityPiglinPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/9f18107d275f1cb3a9f973e5928d5879fa40328ff3258054db6dd3e7c0ca6330").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/9f18107d275f1cb3a9f973e5928d5879fa40328ff3258054db6dd3e7c0ca6330").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setDancing(!entityPet.isDancing()))
                .value(IEntityPiglinPet::isDancing).build();
    }

    interface PolarBear {
        PetData<IEntityPolarBearPet> STANDING = PetData.of("standing", IEntityPolarBearPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.of(Material.IRON_LEGGINGS).withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.of(Material.IRON_LEGGINGS).withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setStandingUp(!entityPet.isStanding()))
                .value(IEntityPolarBearPet::isStanding).build();
    }

    interface Pufferfish {
        PetData<IEntityPufferFishPet> SIZE = PetData.of("size", IEntityPufferFishPet.class)
                .defaultValue(PufferState.SMALL)
                .items(PufferState.values(), value -> ItemBuilder.playerSkull("http://textures.minecraft.net/texture/17152876bc3a96dd2a2299245edb3beef647c8a56ac8853a687c3e7b5d8bb")
                        .withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setPuffState(PetData.cycleForward(entityPet.getPuffState(), PufferState.values())))
                .onRightClick(entityPet -> entityPet.setPuffState(PetData.cycleBackward(entityPet.getPuffState(), PufferState.values())))
                .value(IEntityPufferFishPet::getPuffState).build();
    }

    interface Rabbit {
        PetData<IEntityRabbitPet> VARIANT = PetData.of("variant", IEntityRabbitPet.class)
                .defaultValue(RabbitVariant.BROWN)
                .items(RabbitVariant.values(), value -> ItemBuilder.playerSkull("http://textures.minecraft.net/texture/17152876bc3a96dd2a2299245edb3beef647c8a56ac8853a687c3e7b5d8bb")
                        .withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setRabbitType(PetData.cycleForward(entityPet.getRabbitType(), RabbitVariant.values())))
                .onRightClick(entityPet -> entityPet.setRabbitType(PetData.cycleBackward(entityPet.getRabbitType(), RabbitVariant.values())))
                .value(IEntityRabbitPet::getRabbitType).build();
    }

    interface Ravager {
        PetData<IEntityRavagerPet> CHOMP = PetData.of("chomping", IEntityRavagerPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/cd20bf52ec390a0799299184fc678bf84cf732bb1bd78fd1c4b441858f0235a8").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/cd20bf52ec390a0799299184fc678bf84cf732bb1bd78fd1c4b441858f0235a8").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setChomping(!entityPet.isChomping()))
                .value(IEntityRavagerPet::isChomping).build();
    }

    interface Shulker {
        PetData<IEntityShulkerPet> CLOSE = PetData.of("closed", IEntityShulkerPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/25c4d24affdd48102620361527d2156e18c223bae5189ac439815643f3cff9d").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/1433a4b73273a64c8ab2830b0fff777a61a488c92f60f83bfb3e421f428a44").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setShulkerClosed(!entityPet.isShulkerClosed()))
                .value(IEntityShulkerPet::isShulkerClosed).build();
    }

    interface Sniffer {
        PetData<IEntitySnifferPet> STATE = PetData.of("state", IEntitySnifferPet.class)
                .defaultValue(SnifferState.IDLING)
                .items(SnifferState.values(), value -> value.getIcon().withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setSnifferState(PetData.cycleForward(entityPet.getSnifferState(), SnifferState.values())))
                .onRightClick(entityPet -> entityPet.setSnifferState(PetData.cycleBackward(entityPet.getSnifferState(), SnifferState.values())))
                .value(IEntitySnifferPet::getSnifferState).build();
    }

    interface Snowman {
        PetData<IEntitySnowmanPet> PUMPKIN = PetData.of("pumpkin", IEntitySnowmanPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/4224b25796529ef58a36da6f227dd3ef40a842172d91f396aabeed7e04dbd5b1").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/11136616d8c4a87a54ce78a97b551610c2b2c8f6d410bc38b858f974b113b208").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setHasPumpkin(!entityPet.hasPumpkin()))
                .value(IEntitySnowmanPet::hasPumpkin).build();
    }

    interface TropicalFish {
        PetData<IEntityTropicalFishPet> BODY_COLOR = new BodyColorData();
        PetData<IEntityTropicalFishPet> PATTERN = new PatternData();
        PetData<IEntityTropicalFishPet> PATTERN_COLOR = new PatternColorData();
    }

    interface Villager {
        PetData<IProfession> PROFESSION = PetData.of("profession", IProfession.class)
                .defaultValue(VillagerType.NONE)
                .items(VillagerType.values(), value -> value.getIcon().withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setVillagerType(PetData.cycleForward(entityPet.getVillagerType(), VillagerType.values())))
                .onRightClick(entityPet -> entityPet.setVillagerType(PetData.cycleBackward(entityPet.getVillagerType(), VillagerType.values())))
                .value(IProfession::getVillagerType).build();

        PetData<IProfession> BIOME = PetData.of("biome", IProfession.class)
                .defaultValue(BiomeType.PLAINS)
                .items(BiomeType.values(), value -> value.getIcon().withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setBiome(PetData.cycleForward(entityPet.getBiome(), BiomeType.values())))
                .onRightClick(entityPet -> entityPet.setBiome(PetData.cycleBackward(entityPet.getBiome(), BiomeType.values())))
                .value(IProfession::getBiome).build();

        PetData<IProfession> LEVEL = PetData.of("level", IProfession.class)
                .defaultValue(VillagerLevel.NOVICE)
                .items(VillagerLevel.values(), value -> value.getIcon().withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setMasteryLevel(PetData.cycleForward(entityPet.getMasteryLevel(), VillagerLevel.values())))
                .onRightClick(entityPet -> entityPet.setMasteryLevel(PetData.cycleBackward(entityPet.getMasteryLevel(), VillagerLevel.values())))
                .value(IProfession::getMasteryLevel).build();
    }

    interface Vindicator {
        PetData<IEntityVindicatorPet> JOHNNY = PetData.of("johnny", IEntityVindicatorPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.of(Material.IRON_AXE).withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.of(Material.IRON_AXE).withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setJohnny(!entityPet.isJohnny()))
                .value(IEntityVindicatorPet::isJohnny).build();
    }

    interface Warden {
        PetData<IEntityWardenPet> ANGER = PetData.of("anger-level", IEntityWardenPet.class)
                .defaultValue(WardenAnger.CALM)
                .items(WardenAnger.values(), value -> ItemBuilder.playerSkull("http://textures.minecraft.net/texture/1dfd13ca08bf973bfef0293d770704a11ef5a9fe20d40671fb066724d3e18d8")
                        .withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setAngerLevel(PetData.cycleForward(entityPet.getAngerLevel(), WardenAnger.values())))
                .onRightClick(entityPet -> entityPet.setAngerLevel(PetData.cycleBackward(entityPet.getAngerLevel(), WardenAnger.values())))
                .value(IEntityWardenPet::getAngerLevel).build();

        PetData<IEntityWardenPet> VIBRATION = PetData.of("vibration", IEntityWardenPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/c6f74361fb00490a0a98eeb814544ecdd775cb55633dbb114e60d27004cb1020").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/1dfd13ca08bf973bfef0293d770704a11ef5a9fe20d40671fb066724d3e18d8").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setVibrationEffect(!entityPet.getVibrationEffect()))
                .value(IEntityWardenPet::getVibrationEffect).build();
    }

    interface Witch {
        PetData<IEntityWitchPet> POTION = PetData.of("potion", IEntityWitchPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.of(Material.POTION).withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.of(Material.GLASS_BOTTLE).withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setDrinkingPotion(!entityPet.isDrinkingPotion()))
                .value(IEntityWitchPet::isDrinkingPotion).build();
    }

    interface Wither {
        PetData<IEntityWitherPet> SHIELD = PetData.of("shielded", IEntityWitherPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/a435164c05cea299a3f016bbbed05706ebb720dac912ce4351c2296626aecd9a").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/ee280cefe946911ea90e87ded1b3e18330c63a23af5129dfcfe9a8e166588041").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setShielded(!entityPet.isShielded()))
                .value(IEntityWitherPet::isShielded).build();

        PetData<IEntityWitherPet> SMALL = PetData.of("small", IEntityWitherPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.of(Material.NETHER_STAR).withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.of(Material.NETHER_STAR).withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setSmall(!entityPet.isSmall()))
                .value(IEntityWitherPet::isSmall).build();
    }

    interface Wolf {
        PetData<IEntityWolfPet> ANGRY = PetData.of("angry", IEntityWolfPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/8d1aa7e3b9564b3846f1dea14f1b1ccbf399bbb23b952dbd7eec41802a289c96").withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/69d1d3113ec43ac2961dd59f28175fb4718873c6c448dfca8722317d67").withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setAngry(!entityPet.isAngry()))
                .value(IEntityWolfPet::isAngry).build();

        PetData<IEntityWolfPet> TILT = PetData.of("tilted", IEntityWolfPet.class)
                .defaultValue(false)
                .item(true, ItemBuilder.of(Material.SKELETON_SKULL).withName("&#c8c8c8{name}: &atrue"))
                .item(false, ItemBuilder.of(Material.SKELETON_SKULL).withName("&#c8c8c8{name}: &cfalse"))
                .onToggle(entityPet -> entityPet.setHeadTilted(!entityPet.isHeadTilted()))
                .value(IEntityWolfPet::isHeadTilted).build();

        PetData<IEntityWolfPet> VARIANT = PetData.of("type", IEntityWolfPet.class)
                .defaultValue(WolfVariant.PALE)
                .items(WolfVariant.values(), value -> value.getIcon().withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setWolfType(PetData.cycleForward(entityPet.getWolfType(), WolfVariant.values())))
                .onRightClick(entityPet -> entityPet.setWolfType(PetData.cycleBackward(entityPet.getWolfType(), WolfVariant.values())))
                .value(IEntityWolfPet::getWolfType).build();
    }

    interface ZombieNautilus {
        PetData<IEntityZombieNautilusPet> VARIANT = PetData.of("variant", IEntityZombieNautilusPet.class)
                .defaultValue(ZombieNautilusVariant.TEMPERATE)
                .items(ZombieNautilusVariant.values(), value -> value.getIcon().withName("&#c8c8c8{name}: &a" + value.name()))
                .onLeftClick(entityPet -> entityPet.setVariant(PetData.cycleForward(entityPet.getVariant(), ZombieNautilusVariant.values())))
                .onRightClick(entityPet -> entityPet.setVariant(PetData.cycleBackward(entityPet.getVariant(), ZombieNautilusVariant.values())))
                .value(IEntityZombieNautilusPet::getVariant).build();
    }
}
