package simplepets.brainsynder.utils;

import com.jeff_media.updatechecker.UpdateCheckSource;

public class Premium {
    public static String RESOURCE_ID = "%%__RESOURCE__%%";
    public static String USER_ID = "%%__USER__%%";
    public static String UNIQUE_DOWNLOAD_ID = "%%__NONCE__%%";

    public static DownloadType getDownloadType () {
        if ("%%__POLYMART__%%".equals("1")) return DownloadType.POLYMART;
        if (!USER_ID.contains("_USER_")) return DownloadType.SPIGOT;
        return DownloadType.JENKINS;
    }

    public static boolean isPremium () {
        return getDownloadType() != DownloadType.JENKINS;
    }

    public enum DownloadType {
        JENKINS,
        SPIGOT,
        POLYMART;

        public UpdateCheckSource toSource () {
            if (this == JENKINS) return UpdateCheckSource.CUSTOM_URL;
            return UpdateCheckSource.valueOf(name());
        }
    }
}
