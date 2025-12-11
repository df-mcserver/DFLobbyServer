package uk.co.nikodem.Config;

import com.moandjiezana.toml.Toml;
import uk.co.nikodem.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class ConfigManager {
    private final String configName = "server.toml";

    public boolean getExists() {
        Path source;
        try {
            source = getConfigFilePath();
        } catch (URISyntaxException | NullPointerException e) {
            return false;
        }

        return source.toFile().exists();
    }

    public boolean getIsValidConfiguration() {
        Path source;
        try {
            source = getConfigFilePath();
        } catch (URISyntaxException e) {
            return false;
        }

        return true;
    }

    public Path getConfigFilePath() throws URISyntaxException, NullPointerException {
        Path path = Path.of(configName);
        return path;
    }

    public Config update() {
        try {
            Path source = getConfigFilePath();
            File file = source.toFile();

            Toml defaultConfiguration = new Toml().read(Main.class.getResourceAsStream("/"+configName));
            Toml parsedConfiguration = new Toml(defaultConfiguration).read(file);

            return parsedConfiguration.to(Config.class);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean create() {
        try {
            InputStream in = Main.class.getResourceAsStream("/"+configName);
            FileOutputStream out = new FileOutputStream(configName);
            out.write(in.readAllBytes());
            out.close();
            in.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
