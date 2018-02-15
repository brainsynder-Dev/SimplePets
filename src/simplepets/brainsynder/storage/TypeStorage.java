package simplepets.brainsynder.storage;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TypeStorage {
    private String displayName;
    private String noColorName;
    private String defaultName;
    private ItemStack item;
    private List<String> description;

    public String getDisplayName() {return this.displayName;}

    public String getNoColorName() {return this.noColorName;}

    public String getDefaultName() {return this.defaultName;}

    public ItemStack getItem() {return this.item;}

    public List<String> getDescription() {return this.description;}

    public void setDisplayName(String displayName) {this.displayName = displayName; }

    public void setNoColorName(String noColorName) {this.noColorName = noColorName; }

    public void setDefaultName(String defaultName) {this.defaultName = defaultName; }

    public void setItem(ItemStack item) {this.item = item; }

    public void setDescription(List<String> description) {this.description = description; }
}
