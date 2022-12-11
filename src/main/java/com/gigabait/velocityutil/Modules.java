package com.gigabait.velocityutil;

import com.gigabait.commands.ReloadCommand;
import com.gigabait.config.*;

public class Modules {
    public Modules(){
        Util.registerCommand("vutilreload", "vureload", new ReloadCommand());
        Message.info("...");
        Message.info("VelocityUtil loading modules...");
        GeneralConfig.getModules().forEach(this::loadModules);
    }

    public static void load(){
        new Modules();
    }

    private void loadModules(String module) {
        if (GeneralConfig.getModules(module)) {
            isEnable(module);
        } else {
            isDisable(module);
        }
    }

    private void isEnable(String module){
        switch (module) {
            case "rcon-manager" -> RconManagerConfig.enable();
            case "rcon-server" -> RconServerConfig.enable();
            case "php-runner" -> PHPConfig.enable();
            case "bash-runner" -> BASHConfig.enable();
            case "events-manager" -> EventsConfig.enable();
        }

    }

    private void isDisable(String module){
        switch (module) {
            case "rcon-manager" -> RconManagerConfig.disable();
            case "rcon-server" -> RconServerConfig.disable();
            case "php-runner" -> PHPConfig.disable();
            case "bash-runner" -> BASHConfig.disable();
            case "events-manager" -> EventsConfig.disable();
        }

    }
}
