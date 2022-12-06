package com.gigabait.commands;

import com.gigabait.config.Lang;
import com.gigabait.config.RconManagerConfig;
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
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!hasPermission(invocation)){
            source.sendMessage(Lang.getKey("no_perms"));
            return;
        }

        if (args.length == 1 && args[0].equals("reload")) {
            if (hasPermission(invocation, "reload")){
                RconManagerConfig.reload();
                source.sendMessage(Lang.getKey("rcon_manager_reload"));
            } else {
                source.sendMessage(Lang.getKey("no_perms"));
            }
            return;
        }

        if (args.length <= 1) {
            source.sendMessage(Lang.getKey("rcon_usage"));
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
                            result = Lang.getOriginText("rcon_response_empty");
                        }
                        source.sendMessage(Lang.getKey("rcon_response", new String[]{"{server}", capitalize(server_name), "{response}", result}));
                    } catch (IOException | AuthenticationException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    source.sendMessage(Lang.getKey("no_perms"));
                }

            }
        } else if (RconManagerConfig.serverIs(server)) {
            if (hasPermission(invocation, "all") || hasPermission(invocation, server)){
                try {
                    Rcon rcon = new Rcon(RconManagerConfig.getIP(server), RconManagerConfig.getPort(server), RconManagerConfig.getPass(server).getBytes());
                    String result = rcon.command(command.trim());
                    if (result.length() == 0){
                        result = Lang.getOriginText("rcon_response_empty");
                    }
                    source.sendMessage(Lang.getKey("rcon_response", new String[]{"{server}", capitalize(server), "{response}", result}));
                } catch (IOException | AuthenticationException e) {
                    throw new RuntimeException(e);
                }
            } else {
                source.sendMessage(Lang.getKey("no_perms"));
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
            args.add("tps");
            args.add("list");
        }
        if (argNum > 2) {
            for (Player player : VelocityUtil.getServer().getAllPlayers()){
                args.add(player.getUsername().trim());
            }
        }
        return CompletableFuture.completedFuture(args);

    }

    public static void unregister(){
        CommandManager manager = VelocityUtil.getServer().getCommandManager();
        manager.unregister("rcon");
    }

    public static String capitalize(String text){
        return text.toUpperCase().charAt(0)+text.substring(1);
    }
}
