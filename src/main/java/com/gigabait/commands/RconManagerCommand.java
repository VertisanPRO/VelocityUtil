package com.gigabait.commands;

import com.gigabait.config.Lang;
import com.gigabait.config.RconManagerConfig;
import com.gigabait.config.Util;
import com.gigabait.rconlib.AuthenticationException;
import com.gigabait.rconlib.Rcon;
import com.gigabait.velocityutil.VelocityUtil;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RconManagerCommand implements SimpleCommand {

    @Override
    public void execute(final SimpleCommand.Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if (!hasPermission(invocation)){
            sender.sendMessage(Lang.no_perms.get());
            return;
        }

        if (args.length == 1 && args[0].equals("reload")) {
            if (hasPermission(invocation, "reload")){
                RconManagerConfig.reload();
                sender.sendMessage(Lang.rcon_manager_reload.get());
            } else {
                sender.sendMessage(Lang.no_perms.get());
            }
            return;
        }

        if (args.length <= 1) {
            sender.sendMessage(Lang.rcon_usage.get());
            return;
        }

        String server = args[0];
        String command = "";

        for (String arg : args) {
            if (!arg.equals(server)) {
                command = (command + " " + arg).trim();
            }
        }


        if (server.equals("all")) {
            for (String server_name : RconManagerConfig.getServers()) {
                if (hasPermission(invocation, "all") || hasPermission(invocation, server_name)){
                    try {
                        Rcon rcon = new Rcon(RconManagerConfig.getIP(server_name), RconManagerConfig.getPort(server_name), RconManagerConfig.getPass(server_name).getBytes());
                        String result = rcon.command(command.trim());
                        if (result.length() == 0){
                            result = Lang.rcon_response_empty.getOrigin();
                        }
                        sender.sendMessage(Lang.rcon_response.replace("{server}", Util.capitalize(server_name), "{response}", result));
                    } catch (IOException | AuthenticationException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    sender.sendMessage(Lang.no_perms.get());
                }

            }
        } else if (RconManagerConfig.serverIs(server)) {
            if (hasPermission(invocation, "all") || hasPermission(invocation, server)){
                try {
                    Rcon rcon = new Rcon(RconManagerConfig.getIP(server), RconManagerConfig.getPort(server), RconManagerConfig.getPass(server).getBytes());
                    String result = rcon.command(command.trim());
                    if (result.length() == 0){
                        result = Lang.rcon_response_empty.getOrigin();
                    }
                    sender.sendMessage(Lang.rcon_response.replace("{server}", Util.capitalize(server), "{response}", result));
                } catch (IOException | AuthenticationException e) {
                    throw new RuntimeException(e);
                }
            } else {
                sender.sendMessage(Lang.no_perms.get());
            }

        }
    }
    @Override
    public boolean hasPermission(final SimpleCommand.Invocation invocation) {
        return invocation.source().hasPermission("velocityutil.rcon");
    }
    public boolean hasPermission(final SimpleCommand.Invocation invocation, String server) {
        return invocation.source().hasPermission("velocityutil.rcon." + server);
    }
    @Override
    public CompletableFuture<List<String>> suggestAsync(final SimpleCommand.Invocation invocation) {
        ArrayList<String> args = new ArrayList<>();
        int argNum = invocation.arguments().length;
        if (argNum == 0){
            args = (ArrayList<String>) RconManagerConfig.getServers();
            args.add("all");
            args.add("reload");
        }
        if (argNum == 2){
            args.addAll(RconManagerConfig.getCommandArgs());
        }
        if (argNum > 2) {
            for (Player player : VelocityUtil.server.getAllPlayers()){
                args.add(player.getUsername().trim());
            }
        }
        return CompletableFuture.completedFuture(args);

    }
    public static void unregister(){
        CommandManager manager = VelocityUtil.server.getCommandManager();
        String[] commands = {"vurcon", "rcon", "velocityrcon"};
        for (String command : commands) {
            manager.unregister(command);
        }
    }

}
