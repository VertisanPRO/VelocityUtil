package com.gigabait.config;

import com.gigabait.config.squishyyaml.YamlConfiguration;
import com.gigabait.velocityutil.Message;
import net.kyori.adventure.text.Component;
import java.io.File;

public class Lang extends YamlConfiguration {

    public static String prefix;
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
        Lang.prefix = getOriginText("prefix");
    }

    public static void reload() {
        Lang.config.load();
    }

    public static Lang get() {
        return Lang.config;
    }

    public static Component getKey(String key) {
        return Message.convert(prefix + get().getString(key));
    }

    public static Component getKey(String text, boolean isText) {
        if (isText){
            return Message.convert(prefix + text);
        }
        return Message.convert(prefix + get().getString(text));
    }

    public static Component getKey(String key, String[] replaceList){
        String resp = get().getString(key);
        int length = replaceList.length;
        int i = 0;
        if (!dividesByTwo(length)){
            return Message.convert(prefix + resp);
        }
        for (String arg : replaceList) {
            if (dividesByTwo(i)){
                int to = i + 1;
                resp = resp.replace(arg, replaceList[to]);
                if (to >= (length - 1)){
                    return Message.convert(prefix + resp);
                }
            }
            i++;
        }
        return Message.convert(prefix + resp);
    }

    public static String getOriginText(String key) {
        return get().getString(key);
    }

    private static boolean dividesByTwo(int a){
        return (a%2==0);
    }
}
