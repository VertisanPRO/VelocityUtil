package com.gigabait.rconlib.server;

import com.gigabait.commands.RconCommandSource;
import com.gigabait.config.Lang;
import com.gigabait.config.RconServerConfig;
import com.gigabait.velocityutil.Message;
import com.gigabait.velocityutil.VelocityUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class RconHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final byte FAILURE = -1;
    private static final byte TYPE_RESPONSE = 0;
    private static final byte TYPE_COMMAND = 2;
    private static final byte TYPE_LOGIN = 3;

    private final String password;

    private boolean loggedIn = false;

    private final RconServer rconServer;

    private final RconCommandSource commandSender;

    public RconHandler(RconServer rconServer, String password) {
        this.rconServer = rconServer;
        this.password = password;
        this.commandSender = new RconCommandSource(rconServer.getServer());
    }

    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) {
        buf = buf.order(ByteOrder.LITTLE_ENDIAN);
        if (buf.readableBytes() < 8) {
            return;
        }

        int requestId = buf.readInt();
        int type = buf.readInt();

        byte[] payloadData = new byte[buf.readableBytes() - 2];
        buf.readBytes(payloadData);
        String payload = new String(payloadData, StandardCharsets.UTF_8);

        buf.readBytes(2); // two byte padding

        if (type == TYPE_LOGIN) {
            handleLogin(ctx, payload, requestId);
        } else if (type == TYPE_COMMAND) {
            handleCommand(ctx, payload, requestId);
        } else {
            sendLargeResponse(ctx, requestId, Lang.unknown_request.getOrigin() + " " + Integer.toHexString(type));
        }
    }

    private void handleLogin(ChannelHandlerContext ctx, String payload, int requestId) {
        if (password.equals(payload)) {
            loggedIn = true;
            sendResponse(ctx, requestId, TYPE_COMMAND, "");
        } else {
            loggedIn = false;
            sendResponse(ctx, FAILURE, TYPE_COMMAND, "");
        }
    }

    private void handleCommand(ChannelHandlerContext ctx, String payload, int requestId) {
        if (!loggedIn) {
            sendResponse(ctx, FAILURE, TYPE_COMMAND, "");
            return;
        }
        boolean stop = false;
        boolean success;
        String message;
        String ip = ctx.channel().remoteAddress().toString().replace("/", "");
        Message.info(Lang.rcon_connect_notify.getOrigin().replace("{address}", ip).replace("{command}", payload));

        VelocityUtil.server.getAllPlayers().forEach(p -> {
            if (p.getPermissionValue("velocityutil.rcon.notify").asBoolean()) {
                p.sendMessage(Lang.rcon_connect_notify.replace("{address}", ip, "{command}", payload));
            }
        });

        if (payload.equalsIgnoreCase("end") || payload.equalsIgnoreCase("stop")) {
            stop = true;
            success = true;
            message = "Shutting down the proxy...";
        } else {
            try {
                success = rconServer.getServer().getCommandManager().executeAsync(commandSender, payload).join();
                if (success) {
                    message = commandSender.flush();
                } else {
                    message = Lang.no_command.getOrigin();
                }
            } catch (Exception e) {
                e.printStackTrace();
                success = false;
                message = Lang.unknown_error.getOrigin();
            }
        }

        if (!success) {
            message = String.format(Lang.error_executing.getOrigin() + " %s (%s)", payload, message);
        }

        if (!RconServerConfig.isColored()) {
            message = RconServerConfig.stripColor(message);
        }

        sendLargeResponse(ctx, requestId, message);

        if (stop) {
            VelocityUtil.server.shutdown();
        }
    }

    private void sendResponse(ChannelHandlerContext ctx, int requestId, int type, String payload) {
        ByteBuf buf = ctx.alloc().buffer().order(ByteOrder.LITTLE_ENDIAN);
        buf.writeInt(requestId);
        buf.writeInt(type);
        buf.writeBytes(payload.getBytes(StandardCharsets.UTF_8));
        buf.writeByte(0);
        buf.writeByte(0);
        ctx.write(buf);
    }

    private void sendLargeResponse(ChannelHandlerContext ctx, int requestId, String payload) {
        if (payload.length() == 0) {
            sendResponse(ctx, requestId, TYPE_RESPONSE, "");
            return;
        }

        int start = 0;
        while (start < payload.length()) {
            int length = payload.length() - start;
            int truncated = Math.min(length, 2048);

            sendResponse(ctx, requestId, TYPE_RESPONSE, payload.substring(start, truncated));
            start += truncated;
        }
    }
}
