package simplepets.brainsynder.pet;

import simplepets.brainsynder.menu.menuItems.*;
import simplepets.brainsynder.menu.menuItems.base.MenuItem;
import simplepets.brainsynder.menu.menuItems.sizes.Size1;
import simplepets.brainsynder.menu.menuItems.sizes.Size2;
import simplepets.brainsynder.menu.menuItems.sizes.Size3;
import simplepets.brainsynder.menu.menuItems.sizes.Size4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum PetData {
    AGE(Age.class),
    BAT(Hang.class),
    WITHER(WitherShield.class, WitherSize.class),
    BLAZE(Burning.class),
    SCREAM(Scream.class),
    PUMPKIN(Pumpkin.class),
    VILLAGER(Age.class, Profession.class),
    PIG(Age.class, Saddle.class),
    RABBIT(Age.class, RabbitColor.class),
    OCELOT(Age.class, Ocelot.class, Sitting.class, Tame.class),
    LLAMA(Age.class, Chested.class,Color.class, LlamaColor.class),
    SLIME(Size1.class, Size2.class, Size3.class, Size4.class),
    POLAR_BEAR(Age.class, Stand.class),
    POWERED(Powered.class),
    SHULKER(Rainbow.class, ShulkerClosed.class, ShulkerColor.class),
    WOLF(Age.class, Angry.class, Color.class,  Sitting.class, Tame.class, Tilt.class),
    HORSE(Age.class, HorseArmor.class, HorseColor.class, HorseStyle.class, Saddle.class),
    MULE(Age.class, Chested.class, Saddle.class),
    HORSE_OTHER(Age.class, Saddle.class),
    ARMOR_STAND(Clone.class, Stand.class),
    SHEEP(Age.class, Color.class, Rainbow.class, Shear.class),
    PARROT(ParrotColor.class, Rainbow.class, Sitting.class),
    WIZARD(Spell.class),
    JOHNNY(Johnny.class),
    ZOMBIE_VILLAGER(Age.class);

    private List<Class<? extends MenuItem>> itemsClasses = new ArrayList<>();


    @SafeVarargs
    PetData(Class<? extends MenuItem>... items) {
        Collections.addAll(this.itemsClasses, items);
    }

    public List<Class<? extends MenuItem>> getItemClasses() {
        return itemsClasses;
    }




}
