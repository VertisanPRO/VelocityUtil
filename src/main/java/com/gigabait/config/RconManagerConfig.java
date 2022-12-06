package com.gigabait.config;

import com.gigabait.config.squishyyaml.YamlConfiguration;

import java.io.File;
import java.util.List;

public class RconManagerConfig extends YamlConfiguration {

    private static RconManagerConfig config;

    public RconManagerConfig(File folder) {
        super(folder, "rcon" + File.separator + "rcon-manager.yml");
        this.load();
    }

    public static void initialise(File folder) {
        RconManagerConfig.config = new RconManagerConfig(folder);
    }

    public static void reload() {
        RconManagerConfig.config.load();
    }

    public static RconManagerConfig get() {
        return RconManagerConfig.config;
    }

    public  static boolean serverIs (String server) {
        if (get().getSection("servers").getKeys(server).isEmpty()){
            return false;
        }
        return true;
    }

    public static List<String> getServers() {
        return RconManagerConfig.get().getSection("servers").getKeys();
    }

    public static Object getServer(String server) {
        return RconManagerConfig.get().getSection("servers").getSection(server).getData();
    }

    public static Integer getPort(String server){
            return (Integer) RconManagerConfig.get().getSection("servers").getSection(server).getInteger("port");
    }

    public static String getIP(String server){
        return (String) RconManagerConfig.get().getSection("servers").getSection(server).getString("ip");
    }

    public static String getPass(String server){
        return (String) RconManagerConfig.get().getSection("servers").getSection(server).getString("pass");
    }
}
