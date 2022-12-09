package com.gigabait.config;

import com.gigabait.config.squishyyaml.YamlConfiguration;
import com.gigabait.velocityutil.Message;
import net.kyori.adventure.text.Component;
import java.io.File;

public enum Lang {
    debug("debug"), prefix("prefix"), no_perms("no_perms"), unknown_error("unknown_error"), unknown_request("unknown_request"),
    error_executing("error_executing"), no_command("no_command"), reload("reload"), rcon_manager_reload("rcon_manager_reload"),
    rcon_usage("rcon_usage"), rcon_response("rcon_response"), rcon_response_empty("rcon_response_empty"), bash_usage("bash_usage"),
    php_usage("php_usage"), bash_out_script("bash_out_script"), bash_runner_reload("bash_runner_reload"),
    php_runner_reload("php_runner_reload"), php_out_script("php_out_script");

    private final String key;
    Lang(String key){
        this.key = key;
    }

    public Component get(){
        return LangConfig.getKey(this.key);
    }

    public String getOrigin(){
        return LangConfig.getOriginText(this.key);
    }

    /**
     * @Example: "old", "new", "old", "new", "old", "new", "old", "new"...
     */
    public Component replace(String... list){
        return LangConfig.getKey(this.key, list);
    }

    public Component text(String text){
        return Message.convert(text);
    }

    public static class LangConfig extends YamlConfiguration {

        public static String prefix;
        private static Lang.LangConfig config;

        public LangConfig(File folder) {
            super(folder, "lang" + File.separator + getLangFile() + ".yml");
            this.load();
        }

        public static void initialise(File folder) {
            config = new LangConfig(folder);
            prefix = Lang.prefix.getOrigin();
        }

        private static String getLangFile(){
            if (GeneralConfig.getLang().isEmpty()){
                return "en.yml";
            }
            return GeneralConfig.getLang();
        }

        public static Lang.LangConfig get() {
            return Lang.LangConfig.config;
        }

        public static Component getKey(String key) {
            return Message.convert(prefix + get().getString(key));
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

}

