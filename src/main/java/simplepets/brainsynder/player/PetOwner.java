package simplepets.brainsynder.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.nbt.StorageTagCompound;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.nms.anvil.AnvilGUI;
import simplepets.brainsynder.nms.anvil.AnvilSlot;
import simplepets.brainsynder.nms.entities.type.main.IEntityControllerPet;
import simplepets.brainsynder.nms.entities.type.main.ITameable;
import simplepets.brainsynder.pet.*;
import simplepets.brainsynder.reflection.PerVersion;
import simplepets.brainsynder.utils.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PetOwner {
    static Map<UUID, PetOwner> ownerMap = new HashMap<>();
    /**
     * JSONArray contains all the pets the player has owned while Vault was Enabled.
     */
    @Getter()
    JSONArray ownedPets = new JSONArray();
    /**
     * Players Pet name, Will return null if empty.
     */
    @Getter
    String petName = null;
    /**
     * Will return the players active pet, Will return null if there is no pet.
     */
    @Getter
    IPet pet = null;
    /**
     * Will return an instance of the Player (Pets Owner)
     */
    @Getter
    Player player = null;
    /**
     * Returns the OwnerFile, Where all the information is stored.
     */
    @Getter
    OwnerFile file = null;
    /**
     * This little boolean is for checking if a player is renaming their pet via chat.
     */
    @Getter
    @Setter
    boolean renaming = false;
    /**
     * Handles Pet respawning when the player teleports, dies, etc...
     */
    @Setter
    StorageTagCompound petToRespawn = null;

    private PetOwner(Player player) {
        Valid.notNull(player, "Player can not be null");
        this.player = player;
        reloadData();
    }

    private PetOwner(String name) {
        this(Bukkit.getPlayerExact(name));
    }

    /**
     * @param name Player username
     * @return PetOwner Instance
     */
    public static PetOwner getPetOwner(String name) {
        Valid.notNull(name, "PlayerName can not be null");
        return getPetOwner(Bukkit.getPlayerExact(name));
    }

    /**
     * @param player org.bukkit.entity.Player instance
     * @return PetOwner Instance
     */
    public static PetOwner getPetOwner(Player player) {
        Valid.notNull(player, "Player can not be null");
        if (ownerMap.containsKey(player.getUniqueId())) {
            return ownerMap.get(player.getUniqueId());
        }
        PetOwner owner = new PetOwner(player);
        ownerMap.put(player.getUniqueId(), owner);
        return ownerMap.get(player.getUniqueId());
    }

    /**
     * Activates a Data Reload for the OwnerFile.
     */
    public void reloadData() {
        if (file == null) this.file = new OwnerFile(this);
        file.reload();
    }

    void setRawPetName(String name) {
        this.petName = name;
    }

    void setRawOwned(JSONArray ownedPets) {
        this.ownedPets = ownedPets;
    }

    /**
     * This changes the Pets name, Will fire the PetNameChangeEvent Event.
     *
     * @param name Pets New name
     */
    public void setPetName(String name) {
        setPetName(name, false);
    }

    public void setPetName(String name, boolean override) {
        boolean hasLimit = PetCore.get().getConfiguration().getBoolean("PetToggles.Rename.Limit-Number-Of-Characters");
        boolean color = PetCore.get().getConfiguration().getBoolean("ColorCodes");
        boolean k = PetCore.get().getConfiguration().getBoolean("Use&k");
        name = name.replace("~", " ");

        if (!override) {
            if (hasLimit && (!player.hasPermission("Pet.name.bypassLimit"))) {
                int limit = PetCore.get().getConfiguration().getInt("PetToggles.Rename.CharacterLimit");
                if (name.length() > limit) {
                    name = name.substring(0, limit);
                }
            }

            PetNameChangeEvent event = new PetNameChangeEvent(player, name, color, k);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                play(player.getEyeLocation(), ParticleMaker.Particle.VILLAGER_ANGRY, 0.5F, 0.5F, 0.5F);
                SoundMaker.BLOCK_ANVIL_LAND.playSound(player.getLocation(), 0.5F, 0.5F);
                player.sendMessage(PetCore.get().getMessages().getString("Pet-RenameFailure", true).replace("{name}", ChatColor.translateAlternateColorCodes('&', name)));
                return;
            }
            name = event.getNewName();
            if (event.canUseColor()) {
                name = ChatColor.translateAlternateColorCodes('&', event.canUseMagic() ? name : name.replace("&k", "k"));
            }
            player.sendMessage(PetCore.get().getMessages().getString("Pet-Name-Changed", true).replace("%petname%", name).replace("%player%", player.getName()));
        } else {
            name = ChatColor.translateAlternateColorCodes('&', k ? name : name.replace("&k", "k"));
        }
        petName = name;
        if (!hasPet()) {
            return;
        }
        pet.getEntity().getEntity().setCustomName(name.replace("%player%", player.getName()));
        if (PetCore.get().getConfiguration().getBoolean("ShowParticles") && (!override)) {
            play(pet.getEntity().getEntity().getLocation(), ParticleMaker.Particle.VILLAGER_HAPPY, 1.0F, 1.0F, 1.0F);
        }
    }

    /**
     * This method is normally used as a Backend code method for spawning Pets.
     *
     * @param pet instance if IPet class
     */
    public void setPet(IPet pet) {
        this.pet = pet;
        if (pet != null) {
            String name = petName;
            if (name == null || name.equals("null")) {
                name = PetCore.get().getDefaultPetName(pet.getPetType(), player);
            }
            PerVersion.handlePathfinders(player, pet.getEntity().getEntity(), pet.getPetType().getSpeed());
            if (pet.getEntity() instanceof ITameable) {
                ITameable wolf = (ITameable) pet.getEntity();
                wolf.setTamed(true);
            }

            if (name == null || name.equals("null")) name = PetCore.get().getDefaultPetName(pet.getPetType(), player);
            pet.getEntity().getEntity().setCustomNameVisible(true);
            pet.getEntity().getEntity().setCustomName(PetCore.get().translateName(name));
        }
    }

    /**
     * @return true= "Player has a Pet" | false= "Player does not have a pet"
     */
    public boolean hasPet() {
        return (pet != null);
    }

    public void addPurchasedPet(String petName) {
        if (!ownedPets.contains(petName))
            ownedPets.add(petName);
    }

    /**
     * Removes the players current pet, if the player does not have a pet method will do nothing.
     */
    public void removePet() {
        if (hasPet()) {
            PetRemoveEvent removeEvent = new PetRemoveEvent((Pet) pet);
            Bukkit.getServer().getPluginManager().callEvent(removeEvent);
            if (removeEvent.isCancelled()) {
                play(player.getEyeLocation(), ParticleMaker.Particle.VILLAGER_ANGRY, 0.5F, 0.5F, 0.5F);
                SoundMaker.BLOCK_ANVIL_LAND.playSound(player.getLocation(), 0.5F, 0.5F);
                return;
            }
            if (pet.getEntity().getEntity().getPassenger() instanceof Player) {
                pet.getEntity().getEntity().eject();
            }

            if (PetCore.get().getConfiguration().getBoolean("ShowParticles")) {
                play(pet.getEntity().getEntity().getLocation(), ParticleMaker.Particle.LAVA, 1.0F, 1.0F, 1.0F);
            }
            if (pet.getEntity() instanceof IEntityControllerPet) {
                ((IEntityControllerPet) pet.getEntity()).remove();
            } else {
                pet.getEntity().getEntity().remove();
            }
            this.pet = null;
        }
    }

    /**
     * Opens a Anvil GUI which allows the owner to rename their pet.
     */
    public void renamePet() {
        if (PetCore.get().getConfiguration().getBoolean("PetToggles.Rename.ViaAnvil")) {
            AnvilGUI gui = new AnvilGUI(PetCore.get(), player, event -> {
                event.setCanceled(true);
                if (event.getSlot() != AnvilSlot.OUTPUT) {
                    event.setWillClose(false);
                    event.setWillDestroy(false);
                    return;
                }
                event.setWillClose(true);
                event.setWillDestroy(true);
                setPetName(event.getName(), false);
            });
            gui.setSlot(AnvilSlot.INPUT_LEFT, new ItemStack(Material.NAME_TAG));
            gui.open();
            player.sendMessage(PetCore.get().getMessages().getString("Pet-RenameViaAnvil", true));
            return;
        }
        if (renaming) {
            renaming = false;
            return;
        }
        renaming = true;
        player.sendMessage(PetCore.get().getMessages().getString("Pet-RenameViaChat", true));
    }

    public void respawnPet() {
        if (hasPet()) return;
        if (!hasPetToRespawn()) return;
        if (!petToRespawn.hasKey("PetType")) return;

        PetType type = PetType.getByName(petToRespawn.getString("PetType"));
        if (type == null) {
            PetCore.get().debug(2, "Could not fetch pet type from value: " + petToRespawn.getString("PetType"));
            return;
        }
        type.setPet(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (hasPet()) {
                    getPet().getVisableEntity().applyCompound(petToRespawn);
                    petToRespawn = null;
                }
            }
        }.runTaskLater(PetCore.get(), 2);
    }

    public boolean hasPetToRespawn() {
        return (petToRespawn != null) && (pet == null);
    }

    void play(Location location, ParticleMaker.Particle effect, float offsetX, float offsetY, float offsetZ) {
        ParticleMaker maker = new ParticleMaker(effect, 20, offsetX, offsetY, offsetZ);
        maker.sendToLocation(location);
    }

    void play(Location location, ParticleMaker.Particle effect, int amount) {
        ParticleMaker maker = new ParticleMaker(effect, amount, 0.5, 0.5, 0.5);
        maker.sendToLocation(location);
    }
}
