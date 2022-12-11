package com.gigabait.velocityutil;

import com.gigabait.commands.BASHCommand;
import com.gigabait.commands.PHPCommand;
import com.gigabait.commands.RconManagerCommand;
import com.gigabait.config.*;
import com.gigabait.rconlib.server.RconServer;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.file.Path;

public class Modules {
    private static RconServer rconServer;
    private static final ProxyServer server = VelocityUtil.server;
    private final Path rootPath = VelocityUtil.rootPath;
    public Modules(){
        GeneralConfig.getModules().forEach(this::runModules);
    }

    public static void load(){
        new Modules();
    }

    private void runModules(String module) {

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
                Util.createDir(rootPath.toString() + File.separator + "php");
                PHPConfig.load();
                Message.info("PHP Runner module enabled");
            }

            if (module.equals("bash-runner")) {
                registerCommand("bash", "vubash", new BASHCommand());
                Util.createDir(rootPath.toString() + File.separator + "bash");
                BASHConfig.load();
                Message.info("BASH Runner module enabled");
            }

            if (module.equals("events-manager")) {
                EventsConfig.initialise(rootPath.toFile());
                EventManager.reload();
                Message.info("Events Manager module enabled");
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
            if (module.equals("events-manager")) {
                EventManager.reload();
            }
        }
    }












    public static void registerCommand(String command, String alias, SimpleCommand CommandClass) {
        CommandManager commandManager = server.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder(command)
                .aliases(alias)
                .plugin(server)
                .build();
        commandManager.register(commandMeta, CommandClass);

    }
    public static void startRconListener() {
        InetSocketAddress address = new InetSocketAddress(RconServerConfig.getPort());
        rconServer = new RconServer(server, RconServerConfig.getPass());
        ChannelFuture future = rconServer.bind(address);
        Channel channel = future.awaitUninterruptibly().channel();
        if (!channel.isActive()) {
            stopRconListener();
        }
        Message.info("Binding rcon to address: " + address.getHostName() + ":" + address.getPort());
    }
    public static void stopRconListener() {
        if (rconServer != null) {
            Message.info("Trying to stop RCON listener");
            rconServer.shutdown();
        }
    }


}
