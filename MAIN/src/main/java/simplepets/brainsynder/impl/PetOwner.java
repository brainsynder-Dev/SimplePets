package simplepets.brainsynder.impl;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.nbt.StorageTagList;
import lib.brainsynder.nbt.StorageTagString;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.user.PetNameChangeEvent;
import simplepets.brainsynder.api.pet.IPetConfig;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.utils.Keys;
import simplepets.brainsynder.utils.UUIDDataType;

import java.util.*;

public class PetOwner implements PetUser {
    private final OfflinePlayer player;
    private final UUID uuid;

    private final List<PetType> ownedPets;
    private final Map<PetType, IEntityPet> petMap;
    private final Map<PetType, String> nameMap;

    public PetOwner(OfflinePlayer player) {
        Validate.notNull(player, "Player can not be null (They Offline?)");
        this.player = player;
        this.uuid = player.getUniqueId();

        petMap = new HashMap<>();
        nameMap = new HashMap<>();
        ownedPets = new ArrayList<>();
    }

    public PetOwner(String username) {
        this(Bukkit.getPlayerExact(username));
    }

    public StorageTagCompound toCompound() {
        StorageTagCompound compound = new StorageTagCompound();
        compound.setUniqueId("owner_uuid", uuid);

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
        petMap.forEach((type, entityPet) -> {
            StorageTagCompound tag = new StorageTagCompound();
            tag.setEnum("type", type);
            tag.setTag("data", entityPet.asCompound());
            pets.appendTag(tag);
        });
        compound.setTag("spawned_pets", pets);

        return compound;
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
    }

    @Override
    public void removeOwnedPet(PetType type) {
        ownedPets.remove(type);
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

        nameMap.put(type, name);
        getPetEntity(type).ifPresent(entity -> {

            PetNameChangeEvent event = new PetNameChangeEvent(PetOwner.this, entity, name);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) return;

            entity.setPetName(name);
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
        petMap.get(type).getEntities().forEach(Entity::remove);
        petMap.remove(type);
        return true;
    }

    @Override
    public boolean removePets() {
        if (petMap.isEmpty()) return false;
        petMap.forEach((type, entityPet) -> entityPet.getEntities().forEach(Entity::remove));
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
        getPetEntity(entity.getPetType()).ifPresent(entityPet -> entityPet.getEntities().forEach(Entity::remove));
        petMap.put(entity.getPetType(), entity);

        PersistentDataContainer container = entity.getEntity().getPersistentDataContainer();
        container.set(Keys.ENTITY_OWNER, new UUIDDataType(), player.getUniqueId());
        container.set(Keys.ENTITY_TYPE, PersistentDataType.STRING, entity.getPetType().getName());

        getPetName(entity.getPetType()).ifPresent(entity::setPetName);
    }

    @Override
    public boolean isPetHat() {
        return false;
    }

    @Override
    public void setPetHat(boolean hat) {

    }

    @Override
    public boolean isPetVehicle() {
        return false;
    }

    @Override
    public boolean setPetVehicle(boolean vehicle, boolean byEvent) {
        return false;
    }

    @Override
    public void updateDataMenu() {

    }
}
