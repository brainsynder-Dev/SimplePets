package simplepets.brainsynder.pet;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItem;
import simplepets.brainsynder.pet.types.ShulkerDefault;
import simplepets.brainsynder.reflection.ReflectionUtil;
import simplepets.brainsynder.storage.files.base.JSONFile;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.EntityWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class PetDefault extends JSONFile {
    private ItemBuilder _BUILDER_;
    private SoundMaker sound, _SOUND_ = null;
    private boolean _FLY_, _HAT_, _MOUNT_, _ENABLED_, _FLOAT_DOWN_;
    private double _RIDE_SPEED_, _SPEED_;
    private JSONArray _COMMANDS_ = new JSONArray();
    private String fileName, _DISPLAY_NAME_,SUMMON_NAME;
    private JSONObject _DATA_ITEMS_ = new JSONObject();

    private EntityWrapper type;
    private PetCore plugin;

    public PetDefault(PetCore plugin, String name, SoundMaker sound, EntityWrapper type) {
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
        setDefault("enabled", "true");
        setDefault("hat", "true");
        setDefault("mount", "true");
        setDefault("float_down", "false");
        setDefault("fly", String.valueOf(canFlyDefault()));

        setDefault("ride_speed", 0.7000000238418579D);
        setDefault("speed", 0.6000000238418579D);

        setDefault("item", getDefaultItem().toJSON());
   //     setDefault("summon_name", WordUtils.capitalizeFully(fileName.replace("_", " ")));

        setDefault("on_summon", new JSONArray());
        try {
            JSONObject dataArrays = ReflectionUtil.getMenuItemsJSON(getPetData().getItemClasses(), this);
            setDefault("data-items", dataArrays);
        } catch (NullPointerException ignored) { // In case there is no pet data
        }
    }

    public void setPet (Player player) {
        new Pet(player.getUniqueId(), this, PetCore.get());
    }

    public void load () {
        _BUILDER_ = ItemBuilder.fromJSON(getObject("item"));

        _ENABLED_ = getBoolean("enabled");
        _FLY_ = getBoolean("fly");
        _HAT_ = getBoolean("hat");
        _MOUNT_ = getBoolean("mount");
        _FLOAT_DOWN_ = getBoolean("float_down");

        _RIDE_SPEED_ = getDouble("ride_speed");
        _SPEED_ = getDouble("speed");

        _DISPLAY_NAME_ = getString("display_name",false);
        SUMMON_NAME = getString("summon_name",false);

        _COMMANDS_ = getArray("on_summon");

        _DATA_ITEMS_ = getObject("data-items");

        if (hasKey("sound")) {
            String sound = getString("sound", false);
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
        return player.hasPermission("Pet.type.*") || player.hasPermission(getPermission());
    }

    public boolean canHat(Player player) {
        if (_HAT_) {
            if (PetCore.hasPerm(player, "Pet.PetToHat")) return true;
            if (PetCore.hasPerm(player, getPermission() + ".*")) return true;
            return PetCore.hasPerm(player, getPermission() + ".hat");
        }
        return false;
    }

    public boolean canMount(Player player) {
        if (this instanceof ShulkerDefault) return false;

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
        if (_DATA_ITEMS_ != null) {
            if (_DATA_ITEMS_.containsKey(name)) {
                JSONArray items = ((JSONArray) _DATA_ITEMS_.get(name));
                if (items.isEmpty()) {
                    Map<String, MenuItem> dataArrays = ReflectionUtil.getMenuItems(getPetData().getItemClasses(), this);
                    if (dataArrays.containsKey(name)) {
                        items = ReflectionUtil.getItemsArray(dataArrays.get(name));
                        _DATA_ITEMS_.put(name, items);
                        set("data-items", _DATA_ITEMS_);
                        save();
                        PetCore.get().debug(getName()+" Could not find the DataItem '"+name+"', adding item defaults");
                    }
                }
                return ItemBuilder.fromJSON((JSONObject) items.get(index));
            }else{
                try {
                    Map<String, MenuItem> dataArrays = ReflectionUtil.getMenuItems(getPetData().getItemClasses(), this);
                    if (dataArrays.containsKey(name)) {
                        JSONArray items = ReflectionUtil.getItemsArray(dataArrays.get(name));
                        _DATA_ITEMS_.put(name, items);
                        set("data-items", _DATA_ITEMS_);
                        save();
                        PetCore.get().debug(getName()+" Could not find the DataItem '"+name+"', adding item defaults");
                        return ItemBuilder.fromJSON((JSONObject) items.get(index));
                    }
                } catch (NullPointerException ignored) { // In case there is no pet data
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
        return null;
    }
    public boolean isSupported() {
        return (getAllowedVersion().getIntVersion() <= ServerVersion.getVersion().getIntVersion());
    }
}
