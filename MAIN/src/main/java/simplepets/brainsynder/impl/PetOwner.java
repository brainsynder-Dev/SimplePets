package simplepets.brainsynder.impl;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.nbt.StorageTagList;
import lib.brainsynder.nbt.StorageTagString;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.event.user.PetNameChangeEvent;
import simplepets.brainsynder.api.pet.IPetConfig;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.files.Config;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.sql.PlayerSQL;
import simplepets.brainsynder.utils.Keys;
import simplepets.brainsynder.utils.UUIDDataType;
import simplepets.brainsynder.utils.Utilities;

import java.util.*;

public class PetOwner implements PetUser {
    private final OfflinePlayer player;
    private final UUID uuid;

    private PetType vehicle = null;
    private final List<PetType> hatPets;

    private final List<StorageTagCompound> respawnPets;
    private final List<StorageTagCompound> savedPetData;
    private final List<PetType> ownedPets;
    private final Map<PetType, IEntityPet> petMap;
    private final Map<PetType, String> nameMap;

    public PetOwner(OfflinePlayer player) {
        Validate.notNull(player, "Player can not be null (They Offline?)");
        this.player = player;
        this.uuid = player.getUniqueId();

        respawnPets = new ArrayList<>();
        savedPetData = new ArrayList<>();
        hatPets = new ArrayList<>();
        petMap = new HashMap<>();
        nameMap = new HashMap<>();
        ownedPets = new ArrayList<>();

        loadCompound(PlayerSQL.getInstance().getCache(uuid));
    }

    public PetOwner(String username) {
        this(Bukkit.getPlayerExact(username));
    }

    public void loadCompound (StorageTagCompound compound) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (compound.hasKey("pet_names")) {
                    StorageTagList names = (StorageTagList) compound.getTag("pet_names");
                    names.getList().forEach(storageBase -> {
                        StorageTagCompound data = (StorageTagCompound) storageBase;
                        PetType.getPetType(data.getString("type", "unknown")).ifPresent(type -> {

                            nameMap.put(type, data.getString("name"));
                        });
                    });
                }

                if (compound.hasKey("owned_pets")) {
                    StorageTagList list = (StorageTagList) compound.getTag("owned_pets");
                    list.getList().forEach(storageBase -> {
                        StorageTagString string = (StorageTagString) storageBase;
                        PetType.getPetType(string.getString()).ifPresent(ownedPets::add);
                    });
                }

                if (compound.hasKey("spawned_pets") && PetCore.getInstance().getConfiguration().getBoolean("Respawn-Last-Pet-On-Login", true)) {
                    StorageTagList list = (StorageTagList) compound.getTag("spawned_pets");
                    ISpawnUtil spawnUtil = SimplePets.getSpawnUtil();
                    list.getList().forEach(storageBase -> {
                        StorageTagCompound tag = (StorageTagCompound) storageBase;
                        respawnPets.remove(tag.getCompoundTag("data"));
                        PetType.getPetType(tag.getString("type", "unknown")).ifPresent(type -> {
                            // Will prevent people from spawning pets they did not purchase if enabled
                            if ((!ownedPets.contains(type)) && PetCore.getInstance().getConfiguration().getBoolean(Config.ECONOMY_TOGGLE, false)) return;

                            SimplePets.getPetConfigManager().getPetConfig(type).ifPresent(config -> {
                                if (!config.isEnabled()) return;
                                if (!type.isSupported()) return;
                                if (!spawnUtil.isRegistered(type)) return;
                                if (player instanceof Player) {
                                    if (!config.hasPermission((Player) player)) return;
                                    spawnUtil.spawnEntityPet(type, PetOwner.this, tag.getCompoundTag("data"));
                                }
                            });
                        });
                    });
                }
            }
        }.runTask(PetCore.getInstance());
    }

    public StorageTagCompound toCompound() {
        StorageTagCompound compound = new StorageTagCompound();
        compound.setUniqueId("uuid", uuid);

        // Saves what pets the player has purchased.
        StorageTagList owned = new StorageTagList();
        ownedPets.forEach(type -> owned.appendTag(new StorageTagString(type.getName())));
        compound.setTag("owned_pets", owned);


        // Saves what names the player set for the type
        StorageTagList names = new StorageTagList();
        nameMap.forEach((type, s) -> {
            StorageTagCompound tag = new StorageTagCompound();
            tag.setEnum("type", type);
            tag.setString("name", s);
            names.appendTag(tag);
        });
        compound.setTag("pet_names", names);

        // Saves what pets the player has spawned
        StorageTagList pets = new StorageTagList();
        respawnPets.forEach(pets::appendTag);
        compound.setTag("spawned_pets", pets);

        // Saves what pets the player has saved
        StorageTagList saves = new StorageTagList();
        savedPetData.forEach(tag -> {
            StorageTagCompound storage = new StorageTagCompound();
            storage.setTag("data", tag);
            storage.setString("type", tag.getString("PetType"));
            saves.appendTag(storage);
        });
        compound.setTag("saved_pets", saves);

        return compound;
    }

    /**
     * Will save all the pets currently spawned, upload them to the database and then remove them
     */
    public void markForRespawn () {
        petMap.forEach((type, entityPet) -> {
            respawnPets.add(new StorageTagCompound()
                    .setTag("data", entityPet.asCompound())
                    .setString("type", type.getName())
            );
        });
        updateDatabase();
        removePets();
    }

    /**
     * Will update the data that is in the database
     */
    public void updateDatabase() {
        PlayerSQL.getInstance().uploadData(this);
    }

    @Override
    public OfflinePlayer getPlayer() {
        return player;
    }

    @Override
    public List<PetType> getOwnedPets() {
        return ownedPets;
    }

    @Override
    public void addOwnedPet(PetType type) {
        if (ownedPets.contains(type)) return;
        ownedPets.add(type);
        updateDatabase();
    }

    @Override
    public void removeOwnedPet(PetType type) {
        ownedPets.remove(type);
        updateDatabase();
    }

    @Override
    public Optional<String> getPetName(PetType type) {
        if (nameMap.containsKey(type)) return Optional.of(nameMap.get(type));
        Optional<IPetConfig> config = SimplePets.getPetConfigManager().getPetConfig(type);
        return config.map(IPetConfig::getDisplayName);
    }

    @Override
    public void setPetName(String name, PetType type) {
        setPetName(name, type, false);
    }

    @Override
    public void setPetName(String name, PetType type, boolean override) {
        if (override) return;

        if (name != null) nameMap.put(type, name);

        if (name == null) {
            Optional<IPetConfig> config = SimplePets.getPetConfigManager().getPetConfig(type);
            if (config.isPresent()) name = config.get().getDisplayName();
            nameMap.remove(type);
        }
        updateDatabase();
        String finalName = name;
        getPetEntity(type).ifPresent(entity -> {

            PetNameChangeEvent event = new PetNameChangeEvent(PetOwner.this, entity, finalName);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) return;

            entity.setPetName(finalName);
        });
    }

    @Override
    public boolean hasPets() {
        return !petMap.isEmpty();
    }

    @Override
    public boolean hasPet(PetType type) {
        return petMap.containsKey(type);
    }

    @Override
    public boolean removePet(PetType type) {
        if (!hasPet(type)) return false;
        if (isPetHat(type)) setPetHat(type, false);

        if (PetCore.getInstance().getConfiguration().getBoolean(Config.REMOVE_PARTICLE_TOGGLE, true)) {
            petMap.get(type).getEntities().forEach(entity -> {
                PetCore.getInstance().getParticleManager().getRemoveParticle().sendToLocation(entity.getLocation());
                entity.remove();
            });
        }
        petMap.remove(type);
        return true;
    }

    @Override
    public boolean removePets() {
        if (petMap.isEmpty()) return false;
        petMap.forEach((type, entityPet) -> {
            if (isPetHat(type)) setPetHat(type, false);

            if (PetCore.getInstance().getConfiguration().getBoolean(Config.REMOVE_PARTICLE_TOGGLE, true)) {
                entityPet.getEntities().forEach(entity -> {
                    PetCore.getInstance().getParticleManager().getRemoveParticle().sendToLocation(entity.getLocation());
                    entity.remove();
                });
            }
        });
        petMap.clear();
        return true;
    }

    @Override
    public Collection<IEntityPet> getPetEntities() {
        return petMap.values();
    }

    @Override
    public Optional<IEntityPet> getPetEntity(PetType type) {
        if (petMap.containsKey(type)) return Optional.of(petMap.get(type));
        return Optional.empty();
    }

    @Override
    public void setPet(IEntityPet entity) {
        if (entity == null) return;
        // This is another players entity pet
        if (!entity.getOwnerUUID().equals(player.getUniqueId())) return;

        // If the player has a duplicate pet being spawned it will remove the old one
        getPetEntity(entity.getPetType()).ifPresent(entityPet -> {
            removePet(entityPet.getPetType());
        });
        petMap.put(entity.getPetType(), entity);

        PersistentDataContainer container = entity.getEntity().getPersistentDataContainer();
        container.set(Keys.ENTITY_OWNER, new UUIDDataType(), player.getUniqueId());
        container.set(Keys.ENTITY_TYPE, PersistentDataType.STRING, entity.getPetType().getName());

        if (PetCore.getInstance().getConfiguration().getBoolean(Config.SPAWN_PARTICLE_TOGGLE, true)) {
            entity.getEntities().forEach(ent -> {
                PetCore.getInstance().getParticleManager().getSpawnParticle().sendToLocation(ent.getLocation());
            });
        }
        getPetName(entity.getPetType()).ifPresent(entity::setPetName);
    }

    @Override
    public boolean hasPetHat() {
        return !hatPets.isEmpty();
    }

    @Override
    public boolean isPetHat(PetType type) {
        if (hatPets.isEmpty()) return false;
        if (((Player) player).getPassengers().isEmpty()) return false;
        return hatPets.contains(type);
    }

    @Override
    public void setPetHat(PetType type, boolean hat) {
        Validate.notNull(type, "PetType can not be null");
        if (!hasPet(type)) return;

        int d = 1;
        if (isPetVehicle(type)) {
            setPetVehicle(type, false, false);
            d = 3;
        }
        int delay = d;


        getPetEntity(type).ifPresent(entityPet -> {
            Optional<IPetConfig> configOptional = SimplePets.getPetConfigManager().getPetConfig(type);
            if (!configOptional.isPresent()) return;

            Entity ent = entityPet.getEntity();
            if (entityPet instanceof IEntityControllerPet) {
                Optional<Entity> optional = ((IEntityControllerPet) entityPet).getDisplayEntity();
                if (optional.isPresent()) ent = optional.get();
            }
            IPetConfig config = configOptional.get();
            if (config.canHat((Player) player) && hat) {
                hatPets.add(type);
                // Set the pet as a hat
                Entity finalEnt = ent;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Utilities.setPassenger(getTopEntity((Player) player), finalEnt);
                    }
                }.runTaskLater(PetCore.getInstance(), delay);
            } else {
                // If pet is a hat, remove the hat from the player
                if (isPetHat(type)) {
                    Entity vehicle = ent.getVehicle();
                    Entity riderMob = getRiderEntity(ent);
                    hatPets.remove(type);
                    if (entityPet instanceof IEntityControllerPet) {
                        IEntityControllerPet controller = ((IEntityControllerPet) entityPet);
                        Optional<Entity> riderOptional = controller.getDisplayRider();

                        if (riderOptional.isPresent()) {
                            Entity rider = riderOptional.get();
                            vehicle.eject();
                            controller.getDisplayEntity().ifPresent(entity -> {
                                Utilities.sendMountPacket((Player) player, rider);
                                Utilities.resetRideCooldown(rider);
                                entity.setPassenger(rider);
                            });
                        } else {
                            Utilities.removePassenger(vehicle, ent);
                        }
                    } else {
                        Utilities.removePassenger(vehicle, ent);
                    }
                    if (riderMob != null)
                        Utilities.setPassenger(vehicle, riderMob);
                }
            }
        });
    }

    public Entity getTopEntity(Player player) {
        if (player.getPassengers().isEmpty()) return player;
        Entity entity = player.getPassengers().get(0);
        while (!entity.getPassengers().isEmpty()) entity = entity.getPassengers().get(0);
        return entity;
    }

    public Entity getRiderEntity(Entity entity) {
        if (entity.getPassengers().isEmpty()) return null;
        return entity.getPassengers().get(0);
    }

    @Override
    public boolean hasPetVehicle() {
        return (vehicle != null);
    }

    @Override
    public boolean isPetVehicle(PetType type) {
        return vehicle == type;
    }

    @Override
    public boolean setPetVehicle(PetType type, boolean vehicle, boolean byEvent) {
        if (hasPetVehicle()) {
            // Remove previous vehicle

        }

        this.vehicle = type;
        Optional<IPetConfig> configOptional = SimplePets.getPetConfigManager().getPetConfig(type);
        if (!configOptional.isPresent()) return false;

        return false;
    }

    @Override
    public void updateDataMenu() {
        InventoryManager.PET_DATA.update(this);
    }
}
