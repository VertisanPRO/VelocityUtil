package com.gigabait.config;

import com.gigabait.config.squishyyaml.YamlConfiguration;

import java.io.File;
import java.util.regex.Pattern;

public class RconServerConfig extends YamlConfiguration {

    public static final char COLOR_CHAR = '\u00A7';
    public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-FK-OR]");
    public static final Pattern STRIP_MC_COLOR_PATTERN = Pattern.compile("§[0-8abcdefklmnor]");
    private static RconServerConfig config;

    public RconServerConfig(File folder) {
        super(folder, "rcon" + File.separator + "rcon-server.yml");
        this.load();
    }

    public static void initialise(File folder) {
        RconServerConfig.config = new RconServerConfig(folder);
    }

    public static void reload() {
        RconServerConfig.config.load();
    }

    public static RconServerConfig get() {
        return RconServerConfig.config;
    }

    public static Integer getPort(){
        return (Integer) RconServerConfig.get().getInteger("port");
    }

    public static String getPass(){
        return (String) RconServerConfig.get().getString("password");
    }

    public static boolean isColored(){ return RconServerConfig.get().getBoolean("colored"); }



    public static String stripColor(final String input) {
        if (input == null) {
            return null;
        }

        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static String stripMcColor(final String input) {
        if (input == null) {
            return null;
        }

        return STRIP_MC_COLOR_PATTERN.matcher(input).replaceAll("");
    }


    public static boolean isInteger(String str) {
        return str.matches("-?\\d+");
    }
}
