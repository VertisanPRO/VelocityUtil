package com.gigabait.config;

import com.gigabait.velocityutil.VelocityUtil;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class PHPConfig {
    public static Map<String, Util.ScriptsData> scriptsData;
    public static ArrayList<String> scriptsList;
    public static final Path dir = Path.of(VelocityUtil.rootPath + File.separator + "php");

    public static void load(){
        scriptsData = Util.getScriptsData(dir, "index.php");
        scriptsList = Util.getScripts(dir);
        Util.copyFile(dir.toString(), "index.php");
    }
}
