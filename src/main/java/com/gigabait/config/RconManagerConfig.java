package com.gigabait.config;

import com.gigabait.commands.RconManagerCommand;
import com.gigabait.config.squishyyaml.YamlConfiguration;
import com.gigabait.velocityutil.Message;
import com.gigabait.velocityutil.VelocityUtil;

import java.io.File;
import java.util.ArrayList;
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
    public static Integer getPort(String server){
            return (Integer) RconManagerConfig.get().getSection("servers").getSection(server).getInteger("port");
    }
    public static String getIP(String server){
        return (String) RconManagerConfig.get().getSection("servers").getSection(server).getString("ip");
    }
    public static String getPass(String server){
        return (String) RconManagerConfig.get().getSection("servers").getSection(server).getString("pass");
    }
    public static ArrayList<String> getCommandArgs(){
        ArrayList<String> args = new ArrayList<>();
        RconManagerConfig.get().getList("tab-complete-list").forEach(arg -> {
            args.add((String) arg);
        });
        return args;
    }
    public static void enable(){
        initialise(VelocityUtil.rootPath.toFile());
        reload();
        Util.registerCommand("rcon", "vurcon", new RconManagerCommand());
        Message.info("Rcon Manager module enabled");
    }
    public static void disable(){
        RconManagerCommand.unregister();
        RconManagerConfig.config = null;
    }
}
