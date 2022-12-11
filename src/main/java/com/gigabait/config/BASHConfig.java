package com.gigabait.config;

import com.gigabait.commands.BASHCommand;
import com.gigabait.velocityutil.Message;
import com.gigabait.velocityutil.VelocityUtil;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class BASHConfig {
    public static Map<String, Util.ScriptsData> scriptsData;
    public static ArrayList<String> scriptsList;
    public static final Path dir = Path.of(VelocityUtil.rootPath + File.separator + "bash");

    public static void load(){
        if (System.getProperty("os.name").contains("Windows")){
            Message.warn("The BASH Runner module cannot be run on your operating system");
            disable();
            return;
        }
        scriptsData = Util.getScriptsData(dir, "run.sh");
        scriptsList = Util.getScripts(dir);
        Util.copyFile(dir.toString(), "run.sh");
        Message.info("BASH Runner module enabled");
    }
    public static void enable(){
        Util.registerCommand("bash", "vubash", new BASHCommand());
        Util.createDir(VelocityUtil.rootPath.toString() + File.separator + "bash");
        load();
    }
    public static void disable(){
        BASHCommand.unregister();
        BASHConfig.scriptsList = null;
        BASHConfig.scriptsData = null;
    }

}
