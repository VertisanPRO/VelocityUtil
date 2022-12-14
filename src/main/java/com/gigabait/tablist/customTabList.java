package com.gigabait.tablist;

import com.gigabait.velocityutil.VelocityUtil;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.player.TabList;
import com.velocitypowered.api.proxy.player.TabListEntry;
import com.velocitypowered.api.util.GameProfile;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class customTabList implements TabList {

    private static ProxyServer server;
    public customTabList(){
        server = VelocityUtil.server;
        server.getScheduler().buildTask(this, this::update)
                .repeat(Duration.ofSeconds(10))
                .schedule();
    }


    private synchronized void update() {
        server.getAllPlayers().forEach(onlinePlayer -> {

        });
    }



    @Override
    public void setHeaderAndFooter(Component header, Component footer) {

    }

    @Override
    public void clearHeaderAndFooter() {

    }

    @Override
    public void addEntry(TabListEntry entry) {

    }

    @Override
    public Optional<TabListEntry> removeEntry(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public boolean containsEntry(UUID uuid) {
        return false;
    }

    @Override
    public Collection<TabListEntry> getEntries() {
        return null;
    }

    @Override
    public TabListEntry buildEntry(GameProfile profile, @Nullable Component displayName, int latency, int gameMode) {
        return null;
    }
}
