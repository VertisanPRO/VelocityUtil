package com.gigabait.velocityutil;

import com.velocitypowered.api.plugin.Plugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class Message {

    public static String prefix = "&f[&3VelocityUtil&f] &7";
    public static Component convert(String message) {
        return Component.text()
                .append(LegacyComponentSerializer.legacyAmpersand().deserialize(message))
                .build();
    }
    public static void info(String message) {
        message = prefix + message;

        for (String string : message.split("\n")) {
            VelocityUtil.server.getConsoleCommandSource().sendMessage(Message.convert(string));
        }
    }
    public static void warn(String message) {
        message = prefix + "&e[WARNING] &6" + message;
        for (String string : message.split("\n")) {
            VelocityUtil.server.getConsoleCommandSource().sendMessage(Message.convert(string));
        }
    }
    public static void error(String message) {
        message = prefix + "&e[ERROR] &4" + message;
        for (String string : message.split("\n")) {
            VelocityUtil.server.getConsoleCommandSource().sendMessage(Message.convert(string));
        }
    }
    public static void logHeader() {
        Message.info("&a----------------------------------");
        Message.info("&a    +==================+");
        Message.info("&a    |   VelocityUtil   |");
        Message.info("&a    +==================+");
        Message.info("&a----------------------------------");
        Message.info("&a    &3Current version: &a"+VelocityUtil.class.getAnnotation(Plugin.class).version());
        Message.info("&a    &3Author: &aGIGABAIT");
        Message.info("&a----------------------------------");

    }
}
