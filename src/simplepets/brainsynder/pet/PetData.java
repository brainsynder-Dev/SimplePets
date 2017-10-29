package simplepets.brainsynder.pet;

import simplepets.brainsynder.menu.MenuItem;
import simplepets.brainsynder.menu.menuItems.*;
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
    OCELOT(Age.class, Ocelot.class, Tame.class, Sitting.class),
    LLAMA(Age.class, Color.class, LlamaColor.class, Chested.class),
    SLIME(Size1.class, Size2.class, Size3.class, Size4.class),
    POLAR_BEAR(Stand.class, Age.class),
    POWERED(Powered.class),
    SHULKER(ShulkerColor.class, ShulkerClosed.class),
    WOLF(Color.class, Age.class, Tame.class, Sitting.class, Tilt.class, Angry.class),
    HORSE(Saddle.class, Age.class, HorseArmor.class, HorseColor.class, HorseStyle.class),
    MULE(Saddle.class, Age.class, Chested.class),
    HORSE_OTHER(Saddle.class, Age.class),
    ARMOR_STAND(Stand.class, Clone.class),
    SHEEP(Color.class, Age.class, Shear.class),
    PARROT(ParrotColor.class, Sitting.class),
    WIZARD(Spell.class),
    JOHNNY(Johnny.class),
    ZOMBIE_VILLAGER(Age.class);

    private List<Class<? extends MenuItem>> items = new ArrayList<>();

    @SafeVarargs
    PetData(Class<? extends MenuItem>... items) {
        Collections.addAll(this.items, items);
    }

    public List<Class<? extends MenuItem>> getItemClasses() {
        return items;
    }
}
