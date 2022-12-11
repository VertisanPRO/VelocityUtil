package com.gigabait.velocityutil;

import com.gigabait.config.GeneralConfig;
import com.gigabait.config.Util;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static com.gigabait.config.EventsConfig.Events.*;

public class EventManager {

    private static boolean eventsEnabled = GeneralConfig.getModules("events-manager");

    public static void reload(){
        eventsEnabled = GeneralConfig.getModules("events-manager");
    }

    private static void sendCommand(Player player, String command, boolean console){
        if (console){
            Util.executeCommand(command);
        } else {
            Util.executeCommand(player, command);
        }
    }

    private static List<String> commandsPrepare (List<String> commands, String player, String server, String preServer)
    {
        List<String> cmd = new ArrayList<>();
        for (Object command : commands){
            cmd.add(command.toString().replace("{player}", player).replace("{server}", server).replace("{fromServer}", preServer));
        }
        return cmd;
    }

    private static void runnable(Player player, List<String> commands, String currentServerName, String preServer){
        Thread t = new Thread(() -> {
            for(String command : commandsPrepare(commands, player.getUsername(), currentServerName, preServer))
            {
                if(command.contains("[delay]")) {
                    int i = Integer.parseInt(command.replace("[delay]", "").trim());
                    try {
                        TimeUnit.SECONDS.sleep(i);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    continue;
                }
                if(command.contains("[console]")) {
                    command = command.replace("[console]", "").trim();
                    sendCommand(player, command, true);
                } else {
                    sendCommand(player, command, false);
                }
            }
        });
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    public static void onPlayerJoin(PostLoginEvent event) {
        if (!eventsEnabled || !on_join_commands.enabled()){return;}
        String currentServerName = "";
        if (event.getPlayer().getCurrentServer().isPresent()) {
            currentServerName = event.getPlayer().getCurrentServer().get().getServerInfo().getName();
        }
        runnable(event.getPlayer(), on_join_commands.commands(), currentServerName, "");
    }

    public static void onPlayerLeave(DisconnectEvent event) {
        if (!eventsEnabled || !on_leave_commands.enabled()){return;}
        String currentServerName = "";
        if (event.getPlayer().getCurrentServer().isPresent()) {
            currentServerName = event.getPlayer().getCurrentServer().get().getServerInfo().getName();
        }
        runnable(event.getPlayer(), on_leave_commands.commands(), currentServerName, "");
    }

    public static void onPlayerKick(KickedFromServerEvent event) {
        if (!eventsEnabled || !on_server_kick.enabled()){return;}
        String currentServerName = event.getServer().getServerInfo().getName();
        runnable(event.getPlayer(), on_server_kick.commands(), currentServerName, "");
    }

    public static void onServerSwitch(ServerConnectedEvent event) {
        if (!eventsEnabled || !on_server_switch.enabled() || event.getPreviousServer().isEmpty()){return;}
        String currentServerName = event.getServer().getServerInfo().getName();
        String preServer = "";
        if (event.getPreviousServer().isPresent())
            preServer = event.getPreviousServer().get().getServerInfo().getName();
        runnable(event.getPlayer(), on_server_switch.commands(), currentServerName, preServer);
    }

}