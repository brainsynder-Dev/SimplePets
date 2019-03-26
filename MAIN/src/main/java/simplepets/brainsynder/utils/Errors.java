package simplepets.brainsynder.utils;

import simple.brainsynder.utils.Reflection;
import simplepets.brainsynder.PetCore;

public enum Errors {

    API_NEWER("Your Version of SimpleAPI ({cur}) is newer then the recommended version ({rec}) (conflicts are a possibility)"),
    API_OUT_OF_DATE("Your Version of SimpleAPI ({old}) is OutOfDate, Please update SimpleAPI to at least {new} https://www.spigotmc.org/resources/24671/",
            "Disabling SimplePets..."),
    NO_SPIGOT("-------------------------------------------",
            "          Error Type: CRITICAL",
            " - Please ensure you are using a version of Spigot. Either PaperSpigot, TacoSpigot, Spigot, or any other Spigot Software",
            " - SimplePets requires events in the Spigot Software that CraftBukkit does not offer.",
            "          Disabling SimplePets...",
            "-------------------------------------------"),
    JAVA_WARNING_WEAK("An error occurred when trying to get the simplified Java version for: " + System.getProperty("java.version") + " Please make sure you are using a recommended Java version (Java 8)"),
    JAVA_WARNING_CRITICAL("-------------------------------------------",
            "          Error Type: CRITICAL",
            "    An Internal Version Error Occurred",
            "SimplePets Requires Java 8+ in order to work. Please update Java.",
            "-------------------------------------------"),
    UNSUPPORTED_VERSION_CRITICAL("-------------------------------------------",
            "          Error Type: CRITICAL",
            "    An Internal Version Error Occurred",
            "SimplePets Does not support " + Reflection.getVersion() + ", Please Update your server.",
            "-------------------------------------------"),
    UNSUPPORTED_VERSION_WEAK("-------------------------------------------",
            "          Error Type: WARNING",
            "You seem to be on a version other than 1.12.1",
            "SimplePets works best on 1.12.1, Just saying :P",
            "-------------------------------------------"),
    RELOAD_DETECTED("-------------------------------------------",
            "          Error Type: CRITICAL",
            "The plugin has detected a reload",
            "If you encounter ANY strange issues then this will be the cause.",
            "To fix those, Simply RESTART your server",
            "-------------------------------------------"),
    CITIZENS("-------------------------------------------",
            "          Error Type: CRITICAL",
            "You're using the Citizens plugin as well as SimplePets.",
            "Because of this, SimplePets won't work as intended (i.e. vanishing pets)",
            "We're working on it, but we have no promises we can fix it...",
            "-------------------------------------------");

    String[] errMsg;
    Errors(String... s) {
        errMsg = s;
    }

    public Errors replace (ReturnValue<String> value) {
        for (int i = 0; i< errMsg.length; i++) {
            String string = errMsg[i];
            errMsg[i] = value.run(string);
        }
        return this;
    }

    public void print() {
        print(false);
    }

    public void print(boolean debug) {
        for (String line : errMsg) {
            if (debug) {
                PetCore.get().debug(-1, line);
                continue;
            }

            System.out.println(line);
        }
    }

    public interface ReturnValue<T> {
        T run(T var1);
    }
}