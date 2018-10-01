package simplepets.brainsynder.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.material.MaterialData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simple.brainsynder.reflection.FieldAccessor;
import simple.brainsynder.utils.Base64Wrapper;
import simple.brainsynder.utils.Reflection;
import simple.brainsynder.utils.ServerVersion;
import simple.brainsynder.utils.Valid;

import java.util.*;

@SuppressWarnings("ALL")
public class ItemBuilder {
    private JSONObject JSON;
    private ItemStack is;
    private ItemMeta im;

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        JSON = new JSONObject(new LinkedHashMap());
        JSON.put("material", material.name());
        if (amount != 1) JSON.put("amount", amount);
        this.is = new ItemStack(material, amount);
        this.im = is.getItemMeta();
    }

    public static ItemBuilder fromJSON(JSONObject json) {
        if (!json.containsKey("material"))
            throw new NullPointerException("JSONObject seems to be missing the material");

        int amount = 1;
        if (json.containsKey("amount")) amount = Integer.parseInt(String.valueOf(json.get("amount")));
        Material material = Utilities.findMaterial(String.valueOf(json.get("material")));
        ItemBuilder builder = new ItemBuilder(material, amount);
        int data = 0;
        if (json.containsKey("data")) {
            data = Integer.parseInt(String.valueOf(json.get("data")));
            if (json.containsKey("entity")) builder = Utilities.translate113(builder, json);
        }

        if (material.name().contains("SKULL_ITEM") && (ServerVersion.getVersion().getIntVersion() >= ServerVersion.v1_13_R1.getIntVersion())) {
            builder = Utilities.getSkullMaterial(Utilities.SkullType.values()[data]).toBuilder(amount);
        }

        if (json.containsKey("name")) builder.withName(String.valueOf(json.get("name")));
        if (json.containsKey("lore")) {
            List<String> lore = new ArrayList<>();
            lore.addAll(((JSONArray) json.get("lore")));
            builder.withLore(lore);
        }

        if (json.containsKey("enchants") && (material.name().contains("SKULL"))) {
            JSONArray array = (JSONArray) json.get("enchants");
            for (Object o : array) {
                try {
                    String[] args = String.valueOf(o).split(" ~~ ");
                    Enchantment enchant = Enchantment.getByName(args[0]);
                    int level = Integer.parseInt(args[1]);
                    builder.withEnchant(enchant, level);
                } catch (Exception ignored) {
                }
            }
        }
        if (json.containsKey("flags")) {
            JSONArray array = (JSONArray) json.get("flags");
            for (Object o : array) {
                ItemFlag flag = ItemFlag.valueOf(String.valueOf(o));
                builder.withFlag(flag);
            }
        }

        if (json.containsKey("entity")) {
            builder.withEntity(EntityType.valueOf(String.valueOf(json.get("entity"))));
        }

        if (json.containsKey("skullData")) {
            JSONObject skull = (JSONObject) json.get("skullData");

            if (skull.containsKey("texture")) builder.setTexture(String.valueOf(skull.get("texture")));
            if (skull.containsKey("owner")) builder.setOwner(String.valueOf(skull.get("owner")));
        }

        return builder;
    }

    public ItemBuilder clone() {
        return fromJSON(toJSON());
    }

    ItemStack getstack() {
        return is;
    }

    public String getName() {
        if (JSON.containsKey("name")) return String.valueOf(JSON.get("name"));
        return null;
    }

    public ItemBuilder withName(String name) {
        if (name.equals(" ")) name = "&c" + name;
        JSON.put("name", name);
        if (im == null) im = is.getItemMeta();
        im.setDisplayName(translate(name));
        return this;
    }

    public ItemBuilder withLore(List<String> lore) {
        JSONArray LORE = new JSONArray();
        LORE.addAll(lore);
        JSON.put("lore", LORE);

        im.setLore(translate(lore));
        return this;
    }

    public ItemBuilder addLore(String... lore) {
        JSONArray LORE = new JSONArray();
        if (JSON.containsKey("lore")) LORE = (JSONArray) JSON.get("lore");
        List<String> itemLore = new ArrayList<>();
        if (im.hasLore()) itemLore = im.getLore();

        LORE.addAll(Arrays.asList(lore));
        JSON.put("lore", LORE);
        List<String> finalItemLore = itemLore;
        Arrays.asList(lore).forEach(s -> finalItemLore.add(translate(s)));
        im.setLore(finalItemLore);
        return this;
    }

    public ItemBuilder clearLore() {
        if (JSON.containsKey("lore")) JSON.remove("lore");
        if (im.hasLore()) im.getLore().clear();
        return this;
    }

    public ItemBuilder removeLore(String lore) {
        List<String> itemLore = new ArrayList<>();
        if (im.hasLore()) itemLore = im.getLore();
        if (JSON.containsKey("lore")) {
            JSONArray LORE = (JSONArray) JSON.get("lore");
            LORE.stream().filter(o -> String.valueOf(o).startsWith(lore)).forEach(o -> LORE.remove(o));
            if (LORE.isEmpty()) {
                JSON.remove("lore");
            } else {
                JSON.put("lore", LORE);
            }
        }
        itemLore.remove(translate(lore));
        im.setLore(itemLore);
        return this;
    }

    @Deprecated
    public ItemBuilder withData(int data) {
        if (data == -1) return this;
        if (ServerVersion.getVersion().getIntVersion() >= ServerVersion.v1_13_R1.getIntVersion()) return this;
        JSON.put("data", data);
        is.setDurability((short) data);
        return this;
    }

    public ItemBuilder withData(MaterialData data) {
        // Checks from the Bukkit API - Start
        Material mat = is.getType();
        if (data != null && mat != null && mat.getData() != null) {
            if (data.getClass() != mat.getData() && data.getClass() != MaterialData.class) {
                throw new IllegalArgumentException("Provided data is not of type " + mat.getData().getName() + ", found " + data.getClass().getName());
            }

            is.setData(data);
        } else {
            is.setData(data);
        }
        // Checks - end

        JSON.put("material", data.getItemType());
        JSON.put("data", data.getData()); // MaterialData#getData is depricated... but it is still in the 1.13 Bukkit API Preview

        return this;
    }

    public ItemBuilder withEnchant(Enchantment enchant, int level) {
        JSONArray ENCHANTS = new JSONArray();
        if (JSON.containsKey("enchants")) ENCHANTS = (JSONArray) JSON.get("enchants");
        ENCHANTS.add(enchant.getName() + " ~~ " + level);
        JSON.put("enchants", ENCHANTS);
        im.addEnchant(enchant, level, true);
        return this;
    }

    public ItemBuilder withEntity(EntityType type) {
        JSON.put("entity", type.name());

        if (is.getType().name().equals("MONSTER_EGG") || is.getType().name().contains("SPAWN_EGG")) {
            SpawnEggMeta spawnEggMeta = (SpawnEggMeta) im;
            spawnEggMeta.setSpawnedType(type);
            im = spawnEggMeta;
        }

        return this;
    }

    public ItemBuilder removeEnchant(Enchantment enchant) {
        if (JSON.containsKey("enchants")) {
            JSONArray ENCHANTS = (JSONArray) JSON.get("enchants");
            ENCHANTS.stream().filter(o -> String.valueOf(o).startsWith(enchant.getName())).forEach(o -> ENCHANTS.remove(o));
            if (ENCHANTS.isEmpty()) {
                JSON.remove("enchants");
            } else {
                JSON.put("enchants", ENCHANTS);
            }
        }

        is.removeEnchantment(enchant);
        return this;
    }

    public ItemBuilder withFlag(ItemFlag flag) {
        JSONArray FLAGS = new JSONArray();
        if (JSON.containsKey("flags")) FLAGS = (JSONArray) JSON.get("flags");
        FLAGS.add(flag.name());
        JSON.put("flags", FLAGS);
        im.addItemFlags(flag);
        return this;
    }

    public ItemBuilder removeFlag(ItemFlag flag) {
        if (JSON.containsKey("flags")) {
            JSONArray FLAGS = (JSONArray) JSON.get("flags");
            FLAGS.stream().filter(o -> String.valueOf(o).equals(flag.name())).forEach(o -> FLAGS.remove(o));

            if (FLAGS.isEmpty()) {
                JSON.remove("flags");
            } else {
                JSON.put("flags", FLAGS);
            }
        }

        im.removeItemFlags(flag);
        return this;
    }

    public JSONObject toJSON() {
        return JSON;
    }

    public ItemStack build() {
        is.setItemMeta(im);
        return is;
    }

    public ItemBuilder setOwner(String owner) {
        JSONObject SKULL = (JSONObject) JSON.getOrDefault("skullData", new JSONObject());
        SKULL.put("owner", owner);
        JSON.put("skullData", SKULL);

        boolean run = false;

        if (is.getType().name().equals("PLAYER_HEAD")) run = true;
        if (is.getType().name().equals("SKULL_ITEM") && is.getDurability() == 3) run = true;

        if (run) {
            SkullMeta meta = (SkullMeta) im;
            meta.setOwner(owner);
            im = meta;
        }
        return this;
    }

    public ItemBuilder setTexture(String textureURL) {
        if (textureURL.startsWith("http"))
            textureURL = Base64Wrapper.encodeString("{\"textures\":{\"SKIN\":{\"url\":\"" + textureURL + "\"}}}");

        JSONObject SKULL = (JSONObject) JSON.getOrDefault("skullData", new JSONObject());
        SKULL.put("texture", textureURL);
        JSON.put("skullData", SKULL);

        boolean run = false;

        if (is.getType().name().equals("PLAYER_HEAD")) run = true;
        if (is.getType().name().equals("SKULL_ITEM") && is.getDurability() == 3) run = true;

        if (run) {
            SkullMeta meta = (SkullMeta) im;

            if (textureURL.length() > 17) {
                im = applyTextureToMeta(meta, createProfile(textureURL));
            } else {
                meta.setOwner(textureURL);
                im = meta;
            }
        }
        return this;
    }

    public boolean isSimilar(ItemStack check) {
        List<Boolean> values = new ArrayList<>();
        if (check == null) return false;
        ItemStack main = build();
        if (main.getType() == check.getType()) {
            if (check.hasItemMeta() && main.hasItemMeta()) {
                ItemMeta mainMeta = main.getItemMeta();
                ItemMeta checkMeta = check.getItemMeta();
                if (mainMeta.hasDisplayName() && checkMeta.hasDisplayName()) {
                    values.add(mainMeta.getDisplayName().equals(checkMeta.getDisplayName()));
                }

                if (mainMeta.hasLore() && checkMeta.hasLore()) {
                    values.add(mainMeta.getLore().equals(checkMeta.getLore()));
                }

                if (mainMeta.hasEnchants() && checkMeta.hasEnchants()) {
                    values.add(mainMeta.getEnchants().equals(checkMeta.getEnchants()));
                }

                if ((mainMeta instanceof SkullMeta) && (checkMeta instanceof SkullMeta)) {
                    SkullMeta mainSkullMeta = (SkullMeta) mainMeta;
                    SkullMeta checkSkullMeta = (SkullMeta) checkMeta;

                    try { // This is just to ignore any NPE errors that might happen if using regular skulls
                        if (mainSkullMeta.hasOwner() && checkSkullMeta.hasOwner()) {
                            values.add(mainSkullMeta.getOwner().equals(checkSkullMeta.getOwner()));
                        }
                        values.add(getTexture(getGameProfile(mainSkullMeta)).equals(getTexture(getGameProfile(checkSkullMeta))));
                    } catch (Exception ignored) {
                    }
                }
                if ((mainMeta instanceof SpawnEggMeta) && (checkMeta instanceof SpawnEggMeta)) {
                    SpawnEggMeta mainSkullMeta = (SpawnEggMeta) mainMeta;
                    SpawnEggMeta checkSkullMeta = (SpawnEggMeta) checkMeta;

                    values.add(mainSkullMeta.getSpawnedType() == (checkSkullMeta.getSpawnedType()));
                }

                if (!values.isEmpty()) return !values.contains(false);
            }
        }

        return main.isSimilar(check);
    }

    private String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private List<String> translate(List<String> message) {
        ArrayList<String> newLore = new ArrayList<>();
        message.forEach(msg -> newLore.add(translate(msg)));
        return newLore;
    }

    private GameProfile createProfile(String data) {
        Valid.notNull(data, "data can not be null");

        JSONObject SKULL = (JSONObject) JSON.getOrDefault("skullData", new JSONObject());
        try {
            GameProfile profile = new GameProfile(UUID.randomUUID(), String.valueOf(SKULL.getOrDefault("owner", "Steve")));
            PropertyMap propertyMap = profile.getProperties();
            Property property = new Property("textures", data);
            propertyMap.put("textures", property);
            return profile;
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    private SkullMeta applyTextureToMeta(SkullMeta meta, GameProfile profile) {
        Valid.notNull(meta, "meta cannot be null");
        Valid.notNull(profile, "profile cannot be null");
        Class craftMetaSkull = Reflection.getCBCClass("inventory.CraftMetaSkull");
        Class c = craftMetaSkull.cast(meta).getClass();
        FieldAccessor field = FieldAccessor.getField(c, "profile", GameProfile.class);
        field.set(meta, profile);
        return meta;
    }

    private GameProfile getGameProfile(SkullMeta meta) {
        Valid.notNull(meta, "meta cannot be null");
        Class craftMetaSkull = Reflection.getCBCClass("inventory.CraftMetaSkull");
        Class c = craftMetaSkull.cast(meta).getClass();
        FieldAccessor<GameProfile> field = FieldAccessor.getField(c, "profile", GameProfile.class);
        return field.get(meta);
    }

    private String getTexture(GameProfile profile) {
        PropertyMap propertyMap = profile.getProperties();
        Collection<Property> properties = propertyMap.get("textures");
        String text = "";

        for (Property property : properties) {
            if (property.getName().equals("textures")) {
                text = property.getValue();
                break;
            }
        }
        return text;
    }
}