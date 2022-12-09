package com.gigabait.config.squishyyaml;


import com.gigabait.velocityutil.VelocityUtil;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import static org.yaml.snakeyaml.DumperOptions.FlowStyle.BLOCK;


public class YamlConfiguration extends YamlConfigurationSection implements Configuration {


    private File file;

    private final File folder;

    private final String path;

    public YamlConfiguration(File file) {
        super(new HashMap<>());
        this.file = file;
        this.folder = file.getParentFile();
        this.path = file.getName();
    }

    public YamlConfiguration(File folder, String path) {
        super(new HashMap<>());
        this.folder = folder;
        this.path = path;
    }

    public String getAbsolutePath() {
        if (this.path == null || this.path.equals("")) {
            return this.folder.getAbsolutePath();
        }

        return this.folder.getAbsolutePath() + "/" + this.path;
    }

    @Override
    public boolean load() {
        this.file = new File(this.getAbsolutePath());

        // Create file if it doesn't exist
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) return false;
        }

        if (!file.exists()) {
            try (InputStream input = VelocityUtil.class.getResourceAsStream("/" + file.getName())) {

                if (input != null) {
                    Files.copy(input, file.toPath());
                } else {
                    file.createNewFile();
                }

            } catch (IOException exception) {
                exception.printStackTrace();
                return false;
            }
        }

        try (InputStream inputStream = new FileInputStream(this.file)) {

            Yaml yaml = new Yaml();
            this.data = yaml.load(inputStream);

            if (this.data == null) this.data = new HashMap<>();

        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean save() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setPrettyFlow(true);
        dumperOptions.setDefaultFlowStyle(BLOCK);

        Yaml yaml = new Yaml(dumperOptions);

        try {

            FileWriter writer = new FileWriter(this.file);
            yaml.dump(this.data, writer);

        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }

        return true;
    }
}