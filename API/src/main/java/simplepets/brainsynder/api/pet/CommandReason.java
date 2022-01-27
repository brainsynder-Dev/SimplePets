package simplepets.brainsynder.api.pet;

import java.util.Optional;

public enum CommandReason {
    SPAWN, // When the pet is successfully spawned
    REVOKE, // When the pet is removed
    RIDE, // Command is run when the pet is toggled as a mount
    RIDE_DISMOUNT, // Command is run when the player dismounts the pet
    HAT, // Command is run when the pet is toggled as a hat
    TELEPORT, // When the pet teleports back to the player
    FAILED; // If a task fails to actually run


    public static Optional<CommandReason> getReason(String name) {
        try {
            return Optional.of(valueOf(CommandReason.class, name.toUpperCase().trim()));
        } catch (Exception ignored) {
        }
        return Optional.empty();
    }
}
