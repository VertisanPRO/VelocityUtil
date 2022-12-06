package com.gigabait.config;

import com.gigabait.config.squishyyaml.YamlConfiguration;
import com.gigabait.velocityutil.Message;
import net.kyori.adventure.text.Component;
import java.io.File;

public class Lang extends YamlConfiguration {

    private static Lang config;

    private static String getLangFile(){
        if (GeneralConfig.getLang().isEmpty()){
            return "en.yml";
        }
        return GeneralConfig.getLang();
    }
    public Lang(File folder) {
        super(folder, "lang" + File.separator + getLangFile() + ".yml");
        this.load();
    }

    public static void initialise(File folder) {
        Lang.config = new Lang(folder);
    }

    public static void reload() {
        Lang.config.load();
    }

    public static Lang get() {
        return Lang.config;
    }

    public static Component getKey(String key) {
        return Message.convert(get().getString(key));
    }

    public static Component getKey(String key, boolean text) {
        if (text){
            return Message.convert(key);
        }
        return Message.convert(get().getString(key));
    }

    public static Component getKey(String key, String[] replaceList){
        String resp = get().getString(key);
        int length = replaceList.length;
        int i = 0;
        if (!dividesByTwo(length)){
            return Message.convert(resp);
        }
        for (String arg : replaceList) {
            if (dividesByTwo(i)){
                int to = i + 1;
                resp = resp.replace(arg, replaceList[to]);
                if (to >= (length - 1)){
                    return Message.convert(resp);
                }
            }
            i++;
        }
        return Message.convert(resp);
    }

    public static String getOriginText(String key) {
        return get().getString(key);
    }

    private static boolean dividesByTwo(int a){
        return (a%2==0);
    }
}
