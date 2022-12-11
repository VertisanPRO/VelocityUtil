package com.gigabait.velocityutil;

import com.gigabait.config.GeneralConfig;
import com.gigabait.config.Lang;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import java.nio.file.Path;

@Plugin(
        id = "velocityutil",
        name = "VelocityUtil",
        version = "1.0.1",
        description = "Velocity Util",
        url = "vertisanpro.com",
        authors = {"GIGABAIT"}
)

public class VelocityUtil {

    public static ProxyServer server;
    public static Path rootPath;

    @Inject
    public VelocityUtil(ProxyServer server, @DataDirectory Path dataDirectory) {
        VelocityUtil.server = server;
        VelocityUtil.rootPath = dataDirectory;
    }

    public static void loadPlugin() {
        GeneralConfig.initialise(rootPath.toFile());
        Lang.LangConfig.initialise(rootPath.toFile());
        Modules.load();
    }






    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        EventManager.onPlayerJoin(event);
    }
    @Subscribe
    public void onPlayerLeave(DisconnectEvent event) {
        EventManager.onPlayerLeave(event);
    }
    @Subscribe
    public void onPlayerKick(KickedFromServerEvent event) {
        EventManager.onPlayerKick(event);
    }
    @Subscribe
    public void onServerSwitch(ServerConnectedEvent event) {
        EventManager.onServerSwitch(event);
    }




    @Subscribe
    public void onEnable(ProxyInitializeEvent event) {
        loadPlugin();
        Message.logHeader();
    }
    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        Modules.stopRconListener();
    }

}
