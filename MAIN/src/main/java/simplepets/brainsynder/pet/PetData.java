package simplepets.brainsynder.pet;

import simplepets.brainsynder.menu.menuItems.*;
import simplepets.brainsynder.menu.menuItems.base.MenuItem;
import simplepets.brainsynder.menu.menuItems.cat.CatCollar;
import simplepets.brainsynder.menu.menuItems.cat.CatColor;
import simplepets.brainsynder.menu.menuItems.cat.CatTilt;
import simplepets.brainsynder.menu.menuItems.fox.FoxColor;
import simplepets.brainsynder.menu.menuItems.fox.FoxHeadRoll;
import simplepets.brainsynder.menu.menuItems.fox.FoxSit;
import simplepets.brainsynder.menu.menuItems.fox.FoxSleep;
import simplepets.brainsynder.menu.menuItems.panda.PandaSit;
import simplepets.brainsynder.menu.menuItems.panda.PandaSleep;
import simplepets.brainsynder.menu.menuItems.panda.PandaType;
import simplepets.brainsynder.menu.menuItems.sizes.Size1;
import simplepets.brainsynder.menu.menuItems.sizes.Size2;
import simplepets.brainsynder.menu.menuItems.sizes.Size3;
import simplepets.brainsynder.menu.menuItems.sizes.Size4;
import simplepets.brainsynder.menu.menuItems.tropical.BodyColor;
import simplepets.brainsynder.menu.menuItems.tropical.Pattern;
import simplepets.brainsynder.menu.menuItems.tropical.PatternColor;
import simplepets.brainsynder.menu.menuItems.villager.VillagerBiome;
import simplepets.brainsynder.menu.menuItems.villager.VillagerLevel;
import simplepets.brainsynder.menu.menuItems.villager.VillagerProfession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum PetData {
    SILENT(Silent.class),
    AGE(SILENT, Age.class),
    MOOSHROOM(AGE, MooshroomColor.class),
    ARMS(SILENT, Arms.class),
    BAT(SILENT, Hang.class),
    WITHER(SILENT, WitherShield.class, WitherSize.class),
    BLAZE(SILENT, Burning.class),
    GHAST_SCREAM(SILENT, GhastScream.class),
    SCREAM(SILENT, Scream.class),
    PUMPKIN(SILENT, Pumpkin.class),
    VILLAGER(AGE, Profession.class, VillagerProfession.class, VillagerBiome.class, VillagerLevel.class),
    PIG(AGE, Saddle.class),
    PANDA (AGE, PandaSleep.class, PandaType.class, PandaSit.class),
    FOX (AGE, FoxColor.class, FoxSit.class, FoxSleep.class, FoxHeadRoll.class),
    RABBIT(AGE, RabbitColor.class),
    OCELOT(AGE, Ocelot.class, Sitting.class, Tame.class),
    CAT(AGE, CatColor.class, CatCollar.class, CatTilt.class, Sitting.class, Tame.class),
    LLAMA(AGE, Chested.class,Color.class, LlamaColor.class),
    SIZE(SILENT, Size1.class, Size2.class, Size3.class, Size4.class),
    POLAR_BEAR(AGE, Stand.class),
    POWERED(SILENT, Powered.class),
    SHULKER(SILENT, Rainbow.class, ShulkerClosed.class, ShulkerColor.class),
    WOLF(AGE, Angry.class, Color.class,  Sitting.class, Tame.class, Tilt.class),
    HORSE(AGE, HorseArmor.class, HorseColor.class, HorseStyle.class, Saddle.class, Eating.class),
    MULE_DONKEY(AGE, Eating.class, Chested.class, Saddle.class),
    HORSE_OTHER(AGE, Eating.class, Saddle.class),
    ARMOR_STAND(SILENT, Clone.class, Stand.class),
    SHEEP(AGE, Color.class, Rainbow.class, Shear.class),
    PARROT(SILENT, ParrotColor.class, Rainbow.class, Sitting.class),
    WIZARD(SILENT, Spell.class),
    JOHNNY(SILENT, Johnny.class),
    POTION(SILENT, Potion.class),
    ZOMBIE(AGE, Arms.class, Shaking.class),
    ZOMBIE_VILLAGER(VILLAGER, Arms.class, Shaking.class),
    PUFFER_SIZE (SILENT, PufferSize.class),
    TROPICAL_FISH (SILENT, BodyColor.class, Pattern.class, PatternColor.class);

    private List<Class<? extends MenuItem>> itemsClasses = new ArrayList<>();


    @SafeVarargs
    PetData(PetData otherData, Class<? extends MenuItem>... items) {
        itemsClasses.addAll(otherData.itemsClasses);
        Collections.addAll(this.itemsClasses, items);
    }
    @SafeVarargs
    PetData(PetData[] otherData, Class<? extends MenuItem>... items) {
        for (PetData data : otherData) itemsClasses.addAll(data.itemsClasses);
        Collections.addAll(this.itemsClasses, items);
    }
    @SafeVarargs
    PetData(Class<? extends MenuItem>... items) {
        Collections.addAll(this.itemsClasses, items);
    }

    public List<Class<? extends MenuItem>> getItemClasses() {
        return itemsClasses;
    }




}
