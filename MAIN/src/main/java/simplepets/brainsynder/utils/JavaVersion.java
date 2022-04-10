package simplepets.brainsynder.utils;

import java.util.ArrayList;
import java.util.List;

public enum JavaVersion {
    VERSION_1_8, VERSION_1_9, VERSION_1_10, VERSION_11,
    VERSION_12, VERSION_13, VERSION_14, VERSION_15,
    VERSION_16, VERSION_17, VERSION_18, VERSION_19,
    VERSION_20, VERSION_HIGHER;

    private static final int FIRST_MAJOR_VERSION_ORDINAL = 10;
    private static JavaVersion currentJavaVersion;
    private final String versionName;

    JavaVersion() {
        this.versionName = ordinal() >= FIRST_MAJOR_VERSION_ORDINAL ? getMajorVersion() : "1." + getMajorVersion();
    }

    public static JavaVersion toVersion(Object value) throws IllegalArgumentException {
        if (value == null) {
            return null;
        }
        if (value instanceof JavaVersion) {
            return (JavaVersion) value;
        }
        if (value instanceof Integer) {
            return getVersionForMajor((Integer) value);
        }

        String name = value.toString();

        int firstNonVersionCharIndex = findFirstNonVersionCharIndex(name);

        String[] versionStrings = name.substring(0, firstNonVersionCharIndex).split("\\.");
        List<Integer> versions = convertToNumber(name, versionStrings);

        if (isLegacyVersion(versions)) {
            assertTrue(name, versions.get(1) > 0);
            return getVersionForMajor(versions.get(1));
        } else {
            return getVersionForMajor(versions.get(0));
        }
    }

    public static JavaVersion current() {
        if (currentJavaVersion == null) {
            currentJavaVersion = toVersion(System.getProperty("java.version"));
        }
        return currentJavaVersion;
    }

    public boolean isCompatibleWith(JavaVersion otherVersion) {
        return this.compareTo(otherVersion) >= 0;
    }

    @Override
    public String toString() {
        return versionName;
    }

    public String getMajorVersion() {
        return String.valueOf(ordinal() + 1);
    }

    private static JavaVersion getVersionForMajor(int major) {
        return major >= values().length ? JavaVersion.VERSION_HIGHER : values()[major - 1];
    }

    private static void assertTrue(String value, boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException("Could not determine java version from '" + value + "'.");
        }
    }

    private static boolean isLegacyVersion(List<Integer> versions) {
        return 1 == versions.get(0) && versions.size() > 1;
    }

    private static List<Integer> convertToNumber(String value, String[] versionStrs) {
        List<Integer> result = new ArrayList<Integer>();
        for (String s : versionStrs) {
            assertTrue(value, !isNumberStartingWithZero(s));
            try {
                result.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                assertTrue(value, false);
            }
        }
        assertTrue(value, !result.isEmpty() && result.get(0) > 0);
        return result;
    }

    private static boolean isNumberStartingWithZero(String number) {
        return number.length() > 1 && number.startsWith("0");
    }

    private static int findFirstNonVersionCharIndex(String s) {
        assertTrue(s, s.length() != 0);

        for (int i = 0; i < s.length(); ++i) {
            if (!isDigitOrPeriod(s.charAt(i))) {
                assertTrue(s, i != 0);
                return i;
            }
        }

        return s.length();
    }

    private static boolean isDigitOrPeriod(char c) {
        return (c >= '0' && c <= '9') || c == '.';
    }
}