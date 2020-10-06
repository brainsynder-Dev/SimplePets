package simplepets.brainsynder.commands.sub;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.nbt.*;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.commands.annotations.Permission;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.files.Commands;
import simplepets.brainsynder.utils.AdditionalData;
import simplepets.brainsynder.utils.Utilities;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@ICommand(
        name = "info",
        usage = "&r &r &6[] &7/pet info <player> [debug]",
        description = "Collects information on a players pet/data"
)
@Permission(permission = "info")
@AdditionalData(otherPermissions = "pet.commands.info.debug")
public class Info_SubCommand extends PetSubCommand {
    public Info_SubCommand() {
        registerCompletion(1, getOnlinePlayers());
        registerCompletion(2, (commandSender, list, s) -> {
            list.add("debug");
            return commandSender.hasPermission(getPermission()+".debug");
        });
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
        } else {
            Commands commands = PetCore.get().getCommands();
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(PetCore.get().getMessages().getString("Player-Not-Found", true).replace("%player%", args[0]));
                return;
            }
            PetOwner owner = PetOwner.getPetOwner(target);
            if (owner == null) return;

            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("debug")) {
                    String JSON = findAll(owner).toJSONString();
                    Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
                    String jsonString = gson.toJson(new JsonParser().parse(JSON));
                    CompletableFuture.runAsync(() -> {
                        String url = Utilities.saveTextToHastebin(jsonString);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (url != null) {
                                    sender.sendMessage("§6[§eSimplePets§6] §7" + target.getName() + "'s Pet Data: §e"+ChatColor.GOLD + url+".json");
                                }else{
                                    sender.sendMessage("§cFailed to upload data to Hastebin... Outputting data to Console/Logs...");
                                    System.out.println(jsonString);
                                }
                            }
                        }.runTask(PetCore.get());
                    });
                    return;
                }
            }
            if (!owner.hasPet()) {
                sender.sendMessage(PetCore.get().getMessages().getString("Player-No-Pet", true).replace("%player%", args[0]));
                return;
            }

            sender.sendMessage(commands.getString("Info.Pet-Data-Header")
                    .replace("%player%", args[0]));
            IEntityPet entity = owner.getPet().getVisableEntity();
            StorageTagCompound compound = entity.asCompound();
            compound.getKeySet().forEach(key -> {
                if (!commands.getStringList("Info.Excluded-Data-Values").contains(key)) {
                    sender.sendMessage(commands.getString("Info.Pet-Data-Values")
                            .replace("%key%", WordUtils.capitalize(key.toLowerCase()))
                            .replace("%value%", WordUtils.capitalize(fetchValue(compound.getTag(key)).toLowerCase())));
                }
            });
        }
    }

    private JSONObject findAll (PetOwner owner) {
        JSONObject json = new JSONObject();

        if (owner.hasPet()) {
            JSONObject spawned = new JSONObject();
            IEntityPet entity = owner.getPet().getVisableEntity();
            StorageTagCompound compound = entity.asCompound();
            JSONObject entityData = new JSONObject();
            compound.getKeySet().forEach(key -> entityData.put(key, fetchValue(compound.getTag(key))));
            spawned.put("entity", entityData);

            JSONObject otherData = new JSONObject();
            otherData.put("vehicle", entity.getPet().isVehicle());
            otherData.put("hat", entity.getPet().isHat());
            otherData.put("type-data", getPetTypeData(entity.getPet().getPetType()));
            spawned.put("other-data", otherData);
            json.put("current-pet", spawned);
        }

        if (owner.hasPetToRespawn()) {
            JSONObject respawn = new JSONObject();
            StorageTagCompound compound = owner.getPetToRespawn();
            JSONObject entity = new JSONObject();
            compound.getKeySet().forEach(key -> entity.put(key, fetchValue(compound.getTag(key))));
            respawn.put("entity", entity);

            JSONObject otherData = new JSONObject();
            otherData.put("type-data", getPetTypeData(PetCore.get().getTypeManager().getType(compound.getString("PetType"))));
            respawn.put("other-data", otherData);
            json.put("respawn-pet", respawn);
        }

        if (!owner.getSavedPets().isEmpty()) {
            JSONArray array = new JSONArray();
            owner.getSavedPets().forEach(compound -> {
                JSONObject respawn = new JSONObject();
                JSONObject entity = new JSONObject();
                compound.getKeySet().forEach(key -> entity.put(key, fetchValue(compound.getTag(key))));
                respawn.put("entity", entity);

                JSONObject otherData = new JSONObject();
                otherData.put("type-data", getPetTypeData(PetCore.get().getTypeManager().getType(compound.getString("PetType"))));
                respawn.put("other-data", otherData);
                array.add(respawn);

            });
            json.put("saved-pets", array);
        }

        return json;
    }

    private JSONObject getPetTypeData (PetType data) {
        JSONObject json = new JSONObject();
        JSONObject strings = new JSONObject();
        JSONObject numbers = new JSONObject();
        JSONObject lists = new JSONObject();
        JSONObject booleans = new JSONObject();

        booleans.put("enabled", data.isEnabled());
        booleans.put("supported", data.isSupported());
        json.put("booleans", booleans);

        strings.put("config-name", data.getConfigName());
        strings.put("default-name", data.getDefaultName());
        strings.put("display-name", data.getDisplayName());
        json.put("strings", strings);

        numbers.put("ride-speed", data.getRideSpeed());
        numbers.put("walk-speed", data.getSpeed());
        json.put("numbers", numbers);

        JSONArray array = new JSONArray();
        array.addAll(data.getCommands());
        lists.put("commands", array);
        json.put("lists", lists);
        return json;
    }

    private String fetchValue(StorageBase base) {
        if (base instanceof StorageTagByte) {
            byte tagByte = ((StorageTagByte) base).getByte();
            if ((tagByte == 0) || (tagByte == 1))
                return String.valueOf(tagByte == 1);
            return String.valueOf(tagByte);
        }
        if (base instanceof StorageTagByteArray)
            return Arrays.toString(((StorageTagByteArray) base).getByteArray());
        if (base instanceof StorageTagDouble)
            return String.valueOf(((StorageTagDouble) base).getDouble());
        if (base instanceof StorageTagFloat)
            return String.valueOf(((StorageTagFloat) base).getFloat());
        if (base instanceof StorageTagInt)
            return String.valueOf(((StorageTagInt) base).getInt());
        if (base instanceof StorageTagIntArray)
            return Arrays.toString(((StorageTagIntArray) base).getIntArray());
        if (base instanceof StorageTagLong)
            return String.valueOf(((StorageTagLong) base).getLong());
        if (base instanceof StorageTagShort)
            return String.valueOf(((StorageTagShort) base).getShort());
        if (base instanceof StorageTagString)
            return String.valueOf(((StorageTagString) base).getString());
        if (base instanceof StorageTagList) {
            StorageTagList list = (StorageTagList) base;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < list.tagCount(); i++) {
                builder.append(fetchValue(list.get(i)));
            }
            return builder.toString();
        }

        return "";
    }
}
