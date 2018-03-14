package simplepets.brainsynder.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TypeStorage {
    @Getter
    @Setter
    private String displayName;
    @Getter
    @Setter
    private String noColorName;
    @Getter
    @Setter
    private String defaultName;
    @Getter
    @Setter
    private ItemStack item;
    @Getter
    @Setter
    private List<String> description;
}
