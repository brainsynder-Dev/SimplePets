package simplepets.brainsynder.addon.fail;

public enum FailReason {
    /**
     * There is no reason it failed, means it loaded fine
     */
    NONE,

    /**
     * There was an unknown reason why it failed to load
     */
    UNKNOWN,

    /**
     * The addon was missing a dependency that it requires
     */
    MISSING_DEPENDENCY,

    /**
     * The addon had some malformed data (e.g Config or other file type issues)
     */
    INVALID_DATA,

    /**
     * The addon failed to load due to a custom reason the developer added
     */
    CUSTOM
}
