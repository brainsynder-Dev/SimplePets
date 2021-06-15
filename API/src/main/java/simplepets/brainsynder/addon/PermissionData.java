package simplepets.brainsynder.addon;

public class PermissionData {
    private final String permission;
    private AddonPermissions.AllowType type = AddonPermissions.AllowType.NONE;
    private String description = "";

    public PermissionData(String permission) {this.permission = permission;}

    public PermissionData setType(AddonPermissions.AllowType type) {
        this.type = type;
        return this;
    }

    public PermissionData setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AddonPermissions.AllowType getType() {
        return type;
    }

    public String getPermission() {
        return permission;
    }

    @Override
    public String toString() {
        return "PermissionData{" +
                "permission='" + permission + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                '}';
    }
}
