package com.gigabait.config;

import com.gigabait.config.squishyyaml.YamlConfiguration;
import java.io.File;
import java.util.List;

public class GeneralConfig extends YamlConfiguration {

    private static GeneralConfig config;

    public GeneralConfig(File folder) {
        super(folder, "config.yml");
        this.load();
    }

    public static void initialise(File folder) {
        GeneralConfig.config = new GeneralConfig(folder);
    }

    public static void reload() {
        GeneralConfig.config.load();
    }

    public static GeneralConfig get() {
        return GeneralConfig.config;
    }

    public static List<String> getModules() {
        return GeneralConfig.get().getSection("modules").getKeys();
    }

    public static boolean getModules(String identifier) {
        return GeneralConfig.get().getSection("modules").getBoolean(identifier, true);
    }

    public static String getLang(){
        return GeneralConfig.get().getString("language");
    }
}
