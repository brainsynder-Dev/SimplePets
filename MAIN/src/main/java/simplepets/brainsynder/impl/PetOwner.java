package simplepets.brainsynder.impl;

import com.google.common.collect.Lists;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.nbt.StorageTagList;
import lib.brainsynder.nbt.StorageTagString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.event.entity.*;
import simplepets.brainsynder.api.event.user.PetNameChangeEvent;
import simplepets.brainsynder.api.other.ParticleHandler;
import simplepets.brainsynder.api.pet.IPetConfig;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.files.Config;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.managers.ParticleManager;
import simplepets.brainsynder.sql.PlayerSQL;
import simplepets.brainsynder.sql.SQLManager;
import simplepets.brainsynder.utils.Keys;
import simplepets.brainsynder.utils.UUIDDataType;
import simplepets.brainsynder.utils.Utilities;

import java.util.*;

public class PetOwner implements PetUser {

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
        respawnPets.clear();
        savedPetData.clear();
        hatPets.clear();
        petMap.clear();
        nameMap.clear();
        ownedPets.clear();
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

                if (compound.hasKey("saved_pets")) {
                    StorageTagList list = (StorageTagList) compound.getTag("saved_pets");
                    list.getList().forEach(base -> {
                        StorageTagCompound tag = (StorageTagCompound) base;
                        PetType.getPetType(tag.getString("type", "unknown")).ifPresent(type -> {
                            savedPetData.add(tag.getCompoundTag("data"));
                        });
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
                                Player player = Bukkit.getPlayer(uuid);
                                if (player != null) {
                                    if (!Utilities.hasPermission(player, type.getPermission())) return;
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
        updateDatabase(callback -> {
            // Reset everything after we finish saving
            removePets();
            this.nameMap.clear();
            this.vehicle = null;
            this.savedPetData.clear();
            this.hatPets.clear();
            this.ownedPets.clear();
            this.petMap.clear();
            this.respawnPets.clear();
        });
    }

    /**
     * Will update the data that is in the database
     */
    public void updateDatabase(SQLManager.SQLCallback<Boolean> callback) {
        PlayerSQL.getInstance().uploadData(this, callback);
    }

    @Override
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    @Override
    public boolean hasPetSave(StorageTagCompound compound) {
        return savedPetData.contains(compound);
    }

    @Override
    public void removePetSave(StorageTagCompound compound) {
        if (!savedPetData.contains(compound)) return;
        savedPetData.remove(compound);
    }

    @Override
    public void addPetSave(StorageTagCompound compound) {
        if (savedPetData.contains(compound)) return;
        savedPetData.add(compound);
    }

    @Override
    public List<Entry<PetType, StorageTagCompound>> getSavedPets() {
        List<Entry<PetType, StorageTagCompound>> entryList = Lists.newArrayList();
        savedPetData.forEach(compound -> {
            PetType.getPetType(compound.getString("PetType")).ifPresent(type -> {
                entryList.add(new Entry(type, compound));
            });
        });
        return entryList;
    }

    @Override
    public List<PetType> getOwnedPets() {
        return ownedPets;
    }

    @Override
    public void addOwnedPet(PetType type) {
        if (ownedPets.contains(type)) return;
        ownedPets.add(type);
        updateDatabase(callback -> {});
    }

    @Override
    public void removeOwnedPet(PetType type) {
        ownedPets.remove(type);
        updateDatabase(callback -> {});
    }

    @Override
    public Optional<String> getPetName(PetType type) {
        if (nameMap.containsKey(type)) return Optional.of(nameMap.get(type));
        Optional<IPetConfig> config = SimplePets.getPetConfigManager().getPetConfig(type);
        return config.map(IPetConfig::getDisplayName);
    }

    @Override
    public void setPetName(String name, PetType type) {
        if (name != null) nameMap.put(type, name);

        if (name == null) {
            Optional<IPetConfig> config = SimplePets.getPetConfigManager().getPetConfig(type);
            if (config.isPresent()) name = config.get().getDisplayName();
            nameMap.remove(type);
        }
        updateDatabase(callback -> {});
        String finalName = name;
        getPetEntity(type).ifPresent(entity -> {

            PetNameChangeEvent event = new PetNameChangeEvent(PetOwner.this, entity, finalName);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) return;

            entity.setPetName(finalName);

            SimplePets.getParticleHandler().sendParticle(ParticleManager.Reason.RENAME, (Player) getPlayer(), entity.getEntity().getLocation());
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
        PetRemoveEvent event = new PetRemoveEvent(this, petMap.get(type));
        Bukkit.getPluginManager().callEvent(event);

        petMap.get(type).getEntities().forEach(entity -> {
            SimplePets.getParticleHandler().sendParticle(ParticleManager.Reason.REMOVE, (Player) getPlayer(), entity.getLocation());
            entity.remove();
        });
        petMap.remove(type);
        return true;
    }

    @Override
    public boolean removePets() {
        if (petMap.isEmpty()) return false;
        petMap.forEach((type, entityPet) -> {
            if (isPetHat(type)) setPetHat(type, false);
            PetRemoveEvent event = new PetRemoveEvent(this, entityPet);
            Bukkit.getPluginManager().callEvent(event);

            entityPet.getEntities().forEach(entity -> {
                SimplePets.getParticleHandler().sendParticle(ParticleManager.Reason.REMOVE, (Player) getPlayer(), entity.getLocation());
                entity.remove();
            });
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
        if (!entity.getOwnerUUID().equals(uuid)) return;

        // If the player has a duplicate pet being spawned it will remove the old one
        getPetEntity(entity.getPetType()).ifPresent(entityPet -> {
            PetDuplicateSpawnEvent event = new PetDuplicateSpawnEvent(this, entityPet);
            Bukkit.getPluginManager().callEvent(event);
            removePet(entityPet.getPetType());
        });
        petMap.put(entity.getPetType(), entity);

        PersistentDataContainer container = entity.getEntity().getPersistentDataContainer();
        container.set(Keys.ENTITY_OWNER, new UUIDDataType(), uuid);
        container.set(Keys.ENTITY_TYPE, PersistentDataType.STRING, entity.getPetType().getName());

        entity.getEntities().forEach(ent -> {
            SimplePets.getParticleHandler().sendParticle(ParticleManager.Reason.SPAWN, (Player) getPlayer(), ent.getLocation());
        });

        getPetName(entity.getPetType()).ifPresent(entity::setPetName);
    }

    @Override
    public List<PetType> getHatPets() {
        return hatPets;
    }

    @Override
    public boolean hasPetHat() {
        return !hatPets.isEmpty();
    }

    @Override
    public boolean isPetHat(PetType type) {
        if (hatPets.isEmpty()) return false;
        if (((Player) getPlayer()).getPassengers().isEmpty()) return false;
        return hatPets.contains(type);
    }

    @Override
    public void setPetHat(PetType type, boolean hat) {
        Validate.notNull(type, "PetType can not be null");
        if (!hasPet(type)) return;

        int d = 1;
        if (isPetVehicle(type)) {
            setPetVehicle(type, false);
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
            if (config.canHat((Player) getPlayer()) && hat) {
                PrePetHatEvent event = new PrePetHatEvent(this, entityPet, PrePetHatEvent.Type.SET);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    SimplePets.getParticleHandler().sendParticle(ParticleManager.Reason.TASK_FAILED, (Player) getPlayer(), ent.getLocation());
                    return;
                }
                hatPets.add(type);
                PostPetHatEvent hatEvent = new PostPetHatEvent (PetOwner.this, entityPet, PostPetHatEvent.Type.SET);
                Bukkit.getPluginManager().callEvent(hatEvent);
                // Set the pet as a hat
                Entity finalEnt = ent;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Utilities.setPassenger((Player) getPlayer(), getTopEntity((Player) getPlayer()), finalEnt);
                    }
                }.runTaskLater(PetCore.getInstance(), delay);
            } else {
                // If pet is a hat, remove the hat from the player
                if (isPetHat(type)) {
                    PrePetHatEvent event = new PrePetHatEvent(this, entityPet, PrePetHatEvent.Type.REMOVE);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) { // Don't know why someone would cancel removing the hat XD
                        SimplePets.getParticleHandler().sendParticle(ParticleManager.Reason.TASK_FAILED, (Player) getPlayer(), ent.getLocation());
                        return;
                    }
                    Entity vehicle = ent.getVehicle();
                    Entity riderMob = getRiderEntity(ent);
                    hatPets.remove(type);
                    PostPetHatEvent hatEvent = new PostPetHatEvent (PetOwner.this, entityPet, PostPetHatEvent.Type.REMOVE);
                    Bukkit.getPluginManager().callEvent(hatEvent);
                    if (entityPet instanceof IEntityControllerPet) {
                        IEntityControllerPet controller = ((IEntityControllerPet) entityPet);
                        Optional<Entity> riderOptional = controller.getDisplayRider();

                        if (riderOptional.isPresent()) {
                            Entity rider = riderOptional.get();
                            vehicle.eject();
                            controller.getDisplayEntity().ifPresent(entity -> {
                                Utilities.sendMountPacket((Player) getPlayer(), rider);
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
                        Utilities.setPassenger((Player) getPlayer(), vehicle, riderMob);
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
        return (vehicle != null) && getPetEntity(vehicle).isPresent();
    }

    @Override
    public boolean canSpawnMorePets() {
        int maxAmount = PetCore.getInstance().getConfiguration().getInt("PetToggles.Default-Spawn-Limit");
        if (!getPlayer().isOnline()) return false;
        for (PermissionAttachmentInfo permission : ((Player) getPlayer()).getEffectivePermissions()) {
            if (!permission.getValue()) continue;
            if (!permission.getPermission().startsWith("pet.amount.")) continue;
            String strAmount = permission.getPermission().substring(11);
            maxAmount = Integer.parseInt(strAmount);
        }
        return petMap.size() < maxAmount;
    }

    @Override
    public boolean isPetVehicle(PetType type) {
        return vehicle == type;
    }

    @Override
    public boolean setPetVehicle(PetType type, boolean vehicle) {
        if (!hasPet(type)) {
            if (this.vehicle == type) this.vehicle = null;
            return false;
        }
        Player player = (Player) getPlayer();

        if (hasPetVehicle()) {
            // Remove previous vehicle
            getPetEntity(this.vehicle).ifPresent(entityPet -> {
                PetDismountEvent event = new PetDismountEvent(entityPet);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    SimplePets.getParticleHandler().sendParticle(ParticleHandler.Reason.FAILED, player, entityPet.getEntity().getLocation());
                    return;
                }

                if (entityPet.getEntity().getPassenger() != null) {
                    if (entityPet instanceof IEntityControllerPet) {
                        ((IEntityControllerPet) entityPet).getDisplayEntity().ifPresent(Entity::eject);
                    } else {
                        entityPet.getEntity().eject();
                    }
                }
            });
        }

        if (!vehicle) return true;

        this.vehicle = type;
        Optional<IPetConfig> configOptional = SimplePets.getPetConfigManager().getPetConfig(type);
        if (!configOptional.isPresent()) return false;
        IPetConfig config = configOptional.get();

        getPetEntity(type).ifPresent(entityPet -> {
            if (!config.canMount((Player) getPlayer())) {
                SimplePets.getParticleHandler().sendParticle(ParticleHandler.Reason.FAILED, player, entityPet.getEntity().getLocation());
                return;
            }

            PetMountEvent event = new PetMountEvent(entityPet);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                SimplePets.getParticleHandler().sendParticle(ParticleHandler.Reason.FAILED, player, entityPet.getEntity().getLocation());
                return;
            }

            if (player.getLocation().getBlock() != null) {
                List<Material> blocks = Utilities.getBlacklistedMaterials();
                if (!blocks.contains(player.getLocation().getBlock().getType()) && !blocks.contains(player.getEyeLocation().getBlock().getType())) {
                    entityPet.teleportToOwner();
                }
            } else {
                entityPet.teleportToOwner();
            }


            new BukkitRunnable() {
                @Override
                public void run() {
                    if (entityPet instanceof IEntityControllerPet) {
                        ((IEntityControllerPet) entityPet).getDisplayEntity().ifPresent(entity -> entity.addPassenger(player));
                    } else {
                        entityPet.getEntity().setPassenger(player);
                    }

                }
            }.runTaskLater(PetCore.getInstance(), 2L);
        });
        return false;
    }

    @Override
    public void updateDataMenu() {
        InventoryManager.PET_DATA.update(this);
    }
}
