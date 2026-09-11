package simplepets.brainsynder.api.wrappers;

import org.bsdevelopment.pluginutils.inventory.ItemBuilder;

public enum GolemCrackLevel implements Iconable {
    NONE("89091d79ea0f59ef7ef94d7bba6e5f17f2f7d4572c44f90f76c4819a714", 1.0D),
    LOW("6f3a80a2e39962f81e49e1f6ed8cb0fb8d22e6532476b00fb40c1c72ccc89ac4", 0.65D),
    MEDIUM("fcecba31f26919d92a3d6420cd2fa9112f8e108ac04e3fc71da7329cd10fe5ca", 0.4D),
    HIGH("9d09015cecd3ef65d7f5a8f86437a7ea8cd34ab62a7d6d850f9a1787a627afa9", 0.15D);

    private final String texture;
    private final double healthModifier;

    GolemCrackLevel(String texture, double healthModifier) {
        this.texture = "http://textures.minecraft.net/texture/" + texture;
        this.healthModifier = healthModifier;
    }

    public static GolemCrackLevel getByHealthModifier(double modifier) {
        if (modifier < 0.25D) return HIGH;
        if (modifier < 0.5D) return MEDIUM;
        if (modifier < 0.75D) return LOW;
        return NONE;
    }

    public double getHealthModifier() {
        return healthModifier;
    }

    @Override
    public ItemBuilder getIcon() {
        return ItemBuilder.playerSkull(texture);
    }
}
