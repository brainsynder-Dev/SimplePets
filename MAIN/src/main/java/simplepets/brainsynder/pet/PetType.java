package simplepets.brainsynder.pet;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.VersionRestricted;
import lib.brainsynder.files.JsonFile;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagTools;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItem;
import simplepets.brainsynder.pet.types.ShulkerPet;
import simplepets.brainsynder.reflection.ReflectionUtil;
import simplepets.brainsynder.storage.files.Config;
import simplepets.brainsynder.wrapper.EntityWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class PetType extends JsonFile implements VersionRestricted {
    private ItemBuilder _BUILDER_;
    private final SoundMaker sound;
    private SoundMaker _SOUND_ = null;
    private boolean _FLY_, _HAT_, _MOUNT_, _ENABLED_, _FLOAT_DOWN_;
    private double _RIDE_SPEED_, _SPEED_;
    private JsonArray _COMMANDS_ = new JsonArray();
    private final String fileName;
    private String _DISPLAY_NAME_;
    private String SUMMON_NAME;
    private JsonObject _DATA_ITEMS_ = new JsonObject();

    private final EntityWrapper type;
    private final PetCore plugin;

    public PetType(PetCore plugin, String name, SoundMaker sound, EntityWrapper type) {
        super(new File(new File(plugin.getDataFolder().toString()+File.separator+"Pets"), name+".json"), false);
        if (isSupported()) reload();
        this.fileName = name;
        this.plugin = plugin;
        this.sound = sound;
        this.type = type;
    }

    @Override
    public void loadDefaults() {
        super.loadDefaults();
        setDefault("enabled", true);
        setDefault("hat", true);
        setDefault("mount", true);
        setDefault("float_down", false);
        setDefault("fly", canFlyDefault());

        setDefault("ride_speed", 0.2700000238418579);
        setDefault("speed", 0.6000000238418579D);

        setDefault("item", StorageTagTools.toJsonObject(getDefaultItem().toCompound()));
   //     setDefault("summon_name", WordUtils.capitalizeFully(fileName.replace("_", " ")));

        setDefault("on_summon", new JsonArray());
        try {
            JsonObject dataArrays = ReflectionUtil.getMenuItemsJSON(getPetData().getItemClasses(), this);
            setDefault("data-items", dataArrays);
        } catch (NullPointerException ignored) { // In case there is no pet data
        }
    }

    public void setPet (Player player) {
        new Pet(player.getUniqueId(), this, PetCore.get(), false);
    }

    public void setPet(Player player, boolean restricted) {
        new Pet(player.getUniqueId(), this, PetCore.get(), restricted);
    }

    public void load () {
        try {
            _BUILDER_ = ItemBuilder.fromCompound(StorageTagTools.fromJsonObject((JsonObject) getValue("item")));
        } catch (IllegalArgumentException ex) {
            PetCore.get().getLogger().warning("Error thrown when creating item for " + fileName + ", please check the " + fileName + ".json file in the Pets folder.");
            _BUILDER_ = getDefaultItem();
        }


        _ENABLED_ = getBoolean("enabled");
        _FLY_ = getBoolean("fly");
        _HAT_ = getBoolean("hat");
        _MOUNT_ = getBoolean("mount");
        _FLOAT_DOWN_ = getBoolean("float_down");

        _RIDE_SPEED_ = getDouble("ride_speed");
        _SPEED_ = getDouble("speed");

        _DISPLAY_NAME_ = getString("display_name");
        SUMMON_NAME = getString("summon_name");

        _COMMANDS_ = (JsonArray) getValue("on_summon");

        _DATA_ITEMS_ = (JsonObject) getValue("data-items");

        if (hasKey("sound")) {
            String sound = getString("sound");
            if (!sound.equalsIgnoreCase("off")) _SOUND_ = SoundMaker.valueOf(sound);
        }
    }

    public String getDisplayName() {
        return SUMMON_NAME;
    }

    public String getDefaultName() {
        return _DISPLAY_NAME_;
    }

    public String getConfigName() {
        return fileName;
    }

    public PetCore getPlugin() {
        return plugin;
    }

    public ItemBuilder getItemBuilder() {
        return _BUILDER_;
    }

    public boolean hasPermission(Player player) {
        if (!PetCore.get().needsPermissions()) return true;
        return player.hasPermission(getPermission());
    }

    public boolean canHat(Player player) {
        if (!PetCore.get().getConfiguration().getBoolean(Config.HATS)) return false;
        if (_HAT_) {
            if (PetCore.hasPerm(player, "Pet.PetToHat")) return true;
            if (PetCore.hasPerm(player, getPermission() + ".*")) return true;
            return PetCore.hasPerm(player, getPermission() + ".hat");
        }
        return false;
    }

    public boolean canMount(Player player) {
        if (this instanceof ShulkerPet) return false;
        if (!PetCore.get().getConfiguration().getBoolean(Config.MOUNTABLE)) return false;

        if (_MOUNT_) {
            if (PetCore.hasPerm(player, "Pet.PetToMount")) return true;
            if (PetCore.hasPerm(player, getPermission() + ".*")) return true;
            return PetCore.hasPerm(player, getPermission() + ".mount");
        }
        return false;
    }

    public boolean canFly(Player player) {
        if (_FLY_) {
            if (PetCore.hasPerm(player, "Pet.FlyAll")) return true;
            if (PetCore.hasPerm(player, getPermission() + ".*")) return true;
            return PetCore.hasPerm(player, getPermission() + ".fly");
        }
        return false;
    }



    @Deprecated public abstract ItemBuilder getDefaultItem ();
    @Deprecated public ServerVersion getAllowedVersion () {
        return ServerVersion.v1_8_R3;
    }
    @Deprecated public abstract Class<? extends IEntityPet> getEntityClass ();

    public EntityWrapper getEntityType () {
        return type;
    }

    public boolean canFlyDefault() {
        return false;
    }

    public double getRideSpeed() {
        return _RIDE_SPEED_;
    }

    public double getSpeed() {
        return _SPEED_;
    }

    public SoundMaker getSound() {
        return _SOUND_;
    }

    public SoundMaker getDefaultSound() {
        return sound;
    }

    public List<String> getCommands() {
        List<String> list = new ArrayList<>();
        if (!_COMMANDS_.isEmpty()){
            _COMMANDS_.forEach(o -> list.add(String.valueOf(o)));
        }
        return list;
    }

    public ItemBuilder getDataItemByName(String name, int index) {
        Map<String, MenuItem> menuItemMap = ReflectionUtil.getMenuItems(getPetData().getItemClasses(), this);
        if (_DATA_ITEMS_ != null) {
            if (_DATA_ITEMS_.names().contains(name)) {
                JsonArray items = ((JsonArray) _DATA_ITEMS_.get(name));
                if (items.isEmpty()) {
                    if (menuItemMap.containsKey(name)) {
                        items = ReflectionUtil.getItemsArray(menuItemMap.get(name));
                        _DATA_ITEMS_.add(name, items);
                        set("data-items", _DATA_ITEMS_);
                        save();
                        PetCore.get().debug(getName()+" Could not find the DataItem '"+name+"', adding item defaults");
                    }
                }
                return ItemBuilder.fromCompound(StorageTagTools.fromJsonObject((JsonObject) items.get(index)));
            }else{
                try {
                    if (menuItemMap.containsKey(name)) {
                        JsonArray items = ReflectionUtil.getItemsArray(menuItemMap.get(name));
                        _DATA_ITEMS_.add(name, items);
                        set("data-items", _DATA_ITEMS_);
                        save();
                        PetCore.get().debug(getName()+" Could not find the DataItem '"+name+"', adding item defaults");
                        return ItemBuilder.fromCompound(StorageTagTools.fromJsonObject((JsonObject) items.get(index)));
                    }
                } catch (NullPointerException ignored) { // In case there is no pet data
                }
            }
        }

        PetCore.get().debug(getName()+" Could not find the DataItem '"+name+"', adding item defaults");
        JsonArray items;

        if (_DATA_ITEMS_.names().contains(name)) {
            items = ((JsonArray) _DATA_ITEMS_.get(name));
        }else{
            items = ReflectionUtil.getItemsArray(menuItemMap.get(name));
        }


        if (menuItemMap.containsKey(name)) {
            _DATA_ITEMS_.add(name, items);
            set("data-items", _DATA_ITEMS_);
            save();
            return ItemBuilder.fromCompound(StorageTagTools.fromJsonObject((JsonObject) items.get(index)));
        }else{
            for (MenuItem menuItem : menuItemMap.values()) {
                if (menuItem.getTargetName().equals(name)) {
                    items = ReflectionUtil.getItemsArray(menuItem);
                    _DATA_ITEMS_.add(name, items);
                    set("data-items", _DATA_ITEMS_);
                    save();
                    return ItemBuilder.fromCompound(StorageTagTools.fromJsonObject((JsonObject) items.get(index)));
                }
            }
        }
        return null;
    }

    public boolean isEnabled() {
        return _ENABLED_;
    }

    public boolean canFloat() {
        return _FLOAT_DOWN_;
    }

    public String getPermission() {
        return "Pet.type."+fileName.replace("_", "");
    }
    public PetData getPetData() {
        return PetData.SILENT;
    }
}
