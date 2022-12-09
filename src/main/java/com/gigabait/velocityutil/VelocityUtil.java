package com.gigabait.velocityutil;

import com.gigabait.commands.BASHCommand;
import com.gigabait.commands.PHPCommand;
import com.gigabait.commands.RconManagerCommand;
import com.gigabait.commands.ReloadCommand;
import com.gigabait.config.*;
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
import java.io.File;
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
        Message.logHeader();
    }
    public static void loadPlugin() {
        GeneralConfig.initialise(rootPath.toFile());
        Lang.LangConfig.initialise(rootPath.toFile());
        Message.info("...");
        registerCommand("vutilreload", "vureload", new ReloadCommand());
        Message.info("VelocityUtil loading modules...");
        GeneralConfig.getModules().forEach(VelocityUtil::runModules);
    }
    private static void runModules(String module) {

        if (GeneralConfig.getModules(module)) {
            if (module.equals("rcon-manager")) {
                RconManagerConfig.initialise(rootPath.toFile());
                registerCommand("rcon", "vurcon", new RconManagerCommand());
                Message.info("Rcon Manager module enabled");
            }

            if (module.equals("rcon-server")) {
                RconServerConfig.initialise(rootPath.toFile());
                startRconListener();
                Message.info("Rcon Server module enabled");
            }

            if (module.equals("php-runner")) {
                registerCommand("php", "vuphp", new PHPCommand());
                createDir(rootPath.toString() + File.separator + "php");
                PHPConfig.load();
                Message.info("PHP Runner module enabled");
            }

            if (module.equals("bash-runner")) {
                registerCommand("bash", "vubash", new BASHCommand());
                createDir(rootPath.toString() + File.separator + "bash");
                BASHConfig.load();
                Message.info("BASH Runner module enabled");
            }
        } else {
            if (module.equals("rcon-manager")) {
                RconManagerCommand.unregister();
            }
            if (module.equals("rcon-server")) {
                stopRconListener();
            }
            if (module.equals("php-runner")) {
                PHPCommand.unregister();
            }
            if (module.equals("bash-runner")) {
                BASHCommand.unregister();
            }
        }
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
    public static void createDir(String path){
        File dir = Path.of(path).toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
