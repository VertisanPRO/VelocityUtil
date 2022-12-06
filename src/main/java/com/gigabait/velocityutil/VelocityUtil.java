package com.gigabait.velocityutil;

import com.gigabait.commands.RconManagerCommand;
import com.gigabait.commands.ReloadCommand;
import com.gigabait.config.GeneralConfig;
import com.gigabait.config.Lang;
import com.gigabait.config.RconManagerConfig;
import com.gigabait.config.RconServerConfig;
import com.gigabait.rconlib.server.RconServer;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import java.net.InetSocketAddress;
import java.nio.file.Path;

@Plugin(
        id = "velocityutil",
        name = "VelocityUtil",
        version = "1.0.0",
        description = "Velocity Util",
        url = "vertisanpro.com",
        authors = {"GIGABAIT"}
)
public class VelocityUtil {

    public static ProxyServer server;
    public static Path rootPath;

    private static RconServer rconServer;

    @Inject
    public VelocityUtil(ProxyServer server, @DataDirectory Path dataDirectory) {
        VelocityUtil.server = server;
        VelocityUtil.rootPath = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        loadPlugin();
        registerCommand("vutilreload", "vureload", new ReloadCommand());
        Message.logHeader();
    }

    public static void loadPlugin() {
        GeneralConfig.initialise(rootPath.toFile());
        Lang.initialise(rootPath.toFile());
        Message.info("...");
        Message.info("VelocityUtil load modules...");
        for (String module : GeneralConfig.getModules()) {
            if (GeneralConfig.getModules(module)) {
                if (module.equals("rcon-manager")){
                    RconManagerConfig.initialise(rootPath.toFile());
                    registerCommand("rcon", "vurcon", new RconManagerCommand());
                    Message.info("Rcon Manager module enabled");
                }

                if (module.equals("rcon-server")){
                    RconServerConfig.initialise(rootPath.toFile());
                    startRconListener();
                    Message.info("Rcon Server module enabled");
                }
            } else {
                if (module.equals("rcon-server")){
                    stopRconListener();
                }
            }
        }
    }

    public static ProxyServer getServer() {
        return VelocityUtil.server;
    }

    public static void registerCommand(String command, String alias, SimpleCommand CommandClass) {
        CommandManager commandManager = server.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder(command)
                .aliases(alias)
                .plugin(VelocityUtil.class)
                .build();
        commandManager.register(commandMeta, CommandClass);
    }

    private static void startRconListener() {
        InetSocketAddress address = new InetSocketAddress(RconServerConfig.getPort());
        rconServer = new RconServer(server, RconServerConfig.getPass());
        ChannelFuture future = rconServer.bind(address);
        Channel channel = future.awaitUninterruptibly().channel();
        if (!channel.isActive()) {
            stopRconListener();
        }
        Message.info("Binding rcon to address: " + address.getHostName() + ":" + address.getPort());
    }
    private static void stopRconListener() {
        if (rconServer != null) {
            Message.info("Trying to stop RCON listener");
            rconServer.shutdown();
        }
    }
    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        stopRconListener();
    }
}
