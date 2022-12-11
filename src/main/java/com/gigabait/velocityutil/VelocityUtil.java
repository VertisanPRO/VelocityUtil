package com.gigabait.velocityutil;

import com.gigabait.commands.ReloadCommand;
import com.gigabait.config.GeneralConfig;
import com.gigabait.config.Lang;
import com.gigabait.rconlib.server.RconServer;
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
//    private static RconServer rconServer;

    @Inject
    public VelocityUtil(ProxyServer server, @DataDirectory Path dataDirectory) {
        VelocityUtil.server = server;
        VelocityUtil.rootPath = dataDirectory;
    }

    public static void loadPlugin() {
        GeneralConfig.initialise(rootPath.toFile());
        Lang.LangConfig.initialise(rootPath.toFile());
        Message.info("...");
        Modules.registerCommand("vutilreload", "vureload", new ReloadCommand());
        Message.info("VelocityUtil loading modules...");
        Modules.load();
//        GeneralConfig.getModules().forEach(VelocityUtil::runModules);
    }


//    private static void runModules(String module) {
//
//        if (GeneralConfig.getModules(module)) {
//            if (module.equals("rcon-manager")) {
//                RconManagerConfig.initialise(rootPath.toFile());
//                registerCommand("rcon", "vurcon", new RconManagerCommand());
//                Message.info("Rcon Manager module enabled");
//            }
//
//            if (module.equals("rcon-server")) {
//                RconServerConfig.initialise(rootPath.toFile());
//                startRconListener();
//                Message.info("Rcon Server module enabled");
//            }
//
//            if (module.equals("php-runner")) {
//                registerCommand("php", "vuphp", new PHPCommand());
//                Util.createDir(rootPath.toString() + File.separator + "php");
//                PHPConfig.load();
//                Message.info("PHP Runner module enabled");
//            }
//
//            if (module.equals("bash-runner")) {
//                registerCommand("bash", "vubash", new BASHCommand());
//                Util.createDir(rootPath.toString() + File.separator + "bash");
//                BASHConfig.load();
//                Message.info("BASH Runner module enabled");
//            }
//
//            if (module.equals("events-manager")) {
//                EventsConfig.initialise(rootPath.toFile());
//                EventManager.reload();
//                Message.info("Events Manager module enabled");
//            }
//        } else {
//            if (module.equals("rcon-manager")) {
//                RconManagerCommand.unregister();
//            }
//            if (module.equals("rcon-server")) {
//                stopRconListener();
//            }
//            if (module.equals("php-runner")) {
//                PHPCommand.unregister();
//            }
//            if (module.equals("bash-runner")) {
//                BASHCommand.unregister();
//            }
//            if (module.equals("events-manager")) {
//                EventManager.reload();
//            }
//        }
//    }


//    public static void registerCommand(String command, String alias, SimpleCommand CommandClass) {
//        CommandManager commandManager = server.getCommandManager();
//        CommandMeta commandMeta = commandManager.metaBuilder(command)
//                .aliases(alias)
//                .plugin(server)
//                .build();
//        commandManager.register(commandMeta, CommandClass);
//
//    }








//    private static void startRconListener() {
//        InetSocketAddress address = new InetSocketAddress(RconServerConfig.getPort());
//        rconServer = new RconServer(server, RconServerConfig.getPass());
//        ChannelFuture future = rconServer.bind(address);
//        Channel channel = future.awaitUninterruptibly().channel();
//        if (!channel.isActive()) {
//            stopRconListener();
//        }
//        Message.info("Binding rcon to address: " + address.getHostName() + ":" + address.getPort());
//    }
//    private static void stopRconListener() {
//        if (rconServer != null) {
//            Message.info("Trying to stop RCON listener");
//            rconServer.shutdown();
//        }
//    }





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
