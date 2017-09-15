package simplepets.brainsynder.wrapper;

import com.mojang.authlib.GameProfile;

import java.util.UUID;

public class WrappedGameProfile extends AbstractWrapper<GameProfile> {
    private WrappedGameProfile() {
        super(GameProfile.class);
    }

    public WrappedGameProfile(UUID uuid, String name) {
        this(new GameProfile(uuid, name));
    }

    public WrappedGameProfile(GameProfile profile) {
        this();
        setHandle(profile);
    }

    public UUID getUUID() {
        return getHandle().getId();
    }

    public String getName() {
        return getHandle().getName();
    }
}
