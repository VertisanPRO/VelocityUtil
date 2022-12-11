package com.gigabait.config;

import com.gigabait.config.squishyyaml.ConfigurationSection;
import com.gigabait.config.squishyyaml.YamlConfiguration;
import java.io.File;
import java.util.List;

public class EventsConfig extends YamlConfiguration {

    private static EventsConfig config;
    public EventsConfig(File folder) {
        super(folder, "events.yml");
        this.load();
    }
    public static void initialise(File folder) {
        EventsConfig.config = new EventsConfig(folder);
    }

    public static void reload() {
        EventsConfig.config.load();
    }

    public static EventsConfig get() {
        return EventsConfig.config;
    }

    public static ConfigurationSection getEvents() {
        return EventsConfig.get().getSection("events");
    }

    public enum Events {
        on_join_commands("on_join_commands"), on_leave_commands("on_leave_commands"), on_server_switch("on_server_switch"), on_server_kick("on_server_kick");
        private final String key;
        Events(String key){
            this.key = key;
        }

        public boolean enabled(){
            return EventsConfig.getEvents().getSection(this.key).getBoolean("enabled");
        }

        public List commands(){
            return EventsConfig.getEvents().getSection(this.key).getList("commands");
        }
    }
}
