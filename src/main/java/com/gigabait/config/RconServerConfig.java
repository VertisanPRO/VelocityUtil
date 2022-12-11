package com.gigabait.config;

import com.gigabait.config.squishyyaml.YamlConfiguration;
import com.gigabait.rconlib.server.RconServer;
import com.gigabait.velocityutil.Message;
import com.gigabait.velocityutil.VelocityUtil;
import com.velocitypowered.api.proxy.ProxyServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

public class RconServerConfig extends YamlConfiguration {

    private static RconServer rconServer;
    private static final ProxyServer server = VelocityUtil.server;
    public static final char COLOR_CHAR = '\u00A7';
    public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-FK-OR]");
    public static final Pattern STRIP_MC_COLOR_PATTERN = Pattern.compile("§[0-8abcdefklmnor]");
    private static RconServerConfig config;
    public RconServerConfig(File folder) {
        super(folder, "rcon" + File.separator + "rcon-server.yml");
        this.load();
    }
    public static void initialise(File folder) {
        RconServerConfig.config = new RconServerConfig(folder);
    }
    public static void reload() {
        RconServerConfig.config.load();
    }
    public static RconServerConfig get() {
        return RconServerConfig.config;
    }
    public static Integer getPort(){
        return (Integer) RconServerConfig.get().getInteger("port");
    }
    public static String getPass(){
        return (String) RconServerConfig.get().getString("password");
    }
    public static boolean isColored(){ return RconServerConfig.get().getBoolean("colored"); }
    public static String stripColor(final String input) {
        if (input == null) {
            return null;
        }

        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
    public static String stripMcColor(final String input) {
        if (input == null) {
            return null;
        }

        return STRIP_MC_COLOR_PATTERN.matcher(input).replaceAll("");
    }
    public static boolean isInteger(String str) {
        return str.matches("-?\\d+");
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
    public static void enable(){
        initialise(VelocityUtil.rootPath.toFile());
        reload();
        Message.info("Rcon Server module enabled");
        startRconListener();
    }
    public static void disable(){
        stopRconListener();
        RconServerConfig.config = null;
    }



}
