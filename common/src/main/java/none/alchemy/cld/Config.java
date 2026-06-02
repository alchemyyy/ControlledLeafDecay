package none.alchemy.cld;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import none.alchemy.cld.platform.Services;

public final class Config {
    private static final int DEFAULT_MINIMUM_DECAY_TIME = 8;
    private static final int DEFAULT_MAXIMUM_DECAY_TIME = 32;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Object LOCK = new Object();

    private static volatile int minimumDecayTime = DEFAULT_MINIMUM_DECAY_TIME;
    private static volatile int maximumDecayTime = DEFAULT_MAXIMUM_DECAY_TIME;
    private static volatile Path loadedPath;

    private Config() {
    }

    public static void ensureLoaded(MinecraftServer server) {
        Path path = configPath();
        if (path.equals(loadedPath)) {
            return;
        }

        synchronized (LOCK) {
            if (path.equals(loadedPath)) {
                return;
            }

            try {
                load(path);
            } catch (IOException exception) {
                Constants.LOG.error("Failed to load {} config; using defaults", Constants.MOD_ID, exception);
            }
        }
    }

    public static void reload(MinecraftServer server) throws IOException {
        synchronized (LOCK) {
            load(configPath());
        }
    }

    public static int randomDecayDelay(RandomSource random) {
        int minimum = minimumDecayTime;
        int maximum = maximumDecayTime;

        if (maximum <= minimum) {
            return minimum;
        }

        return minimum + random.nextInt(maximum - minimum);
    }

    public static Path loadedPath() {
        return loadedPath;
    }

    private static void load(Path path) throws IOException {
        Files.createDirectories(path.getParent());

        if (!Files.exists(path)) {
            write(path, DEFAULT_MINIMUM_DECAY_TIME, DEFAULT_MAXIMUM_DECAY_TIME);
        }

        Values values = read(path);
        minimumDecayTime = values.minimumDecayTime();
        maximumDecayTime = values.maximumDecayTime();
        loadedPath = path;

        if (values.corrected()) {
            write(path, minimumDecayTime, maximumDecayTime);
        }
    }

    private static Path configPath() {
        return Services.PLATFORM.getConfigDirectory().resolve(Constants.MOD_ID + ".json");
    }

    private static Values read(Path path) throws IOException {
        RawValues rawValues;
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            rawValues = GSON.fromJson(reader, RawValues.class);
        } catch (JsonParseException exception) {
            Constants.LOG.warn("Ignoring invalid JSON in {}", path, exception);
            rawValues = null;
        }

        int minimum = rawValues == null || rawValues.MinimumDecayTime == null
                ? DEFAULT_MINIMUM_DECAY_TIME
                : rawValues.MinimumDecayTime;
        int maximum = rawValues == null || rawValues.MaximumDecayTime == null
                ? DEFAULT_MAXIMUM_DECAY_TIME
                : rawValues.MaximumDecayTime;
        int correctedMinimum = Math.max(0, minimum);
        int correctedMaximum = Math.max(0, maximum);
        boolean corrected = minimum != correctedMinimum
                || maximum != correctedMaximum
                || rawValues == null
                || rawValues.MinimumDecayTime == null
                || rawValues.MaximumDecayTime == null;

        return new Values(correctedMinimum, correctedMaximum, corrected);
    }

    private static void write(Path path, int minimum, int maximum) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            GSON.toJson(new RawValues(minimum, maximum), writer);
            writer.write(System.lineSeparator());
        }
    }

    private static final class RawValues {
        private Integer MinimumDecayTime;
        private Integer MaximumDecayTime;

        private RawValues(int minimumDecayTime, int maximumDecayTime) {
            this.MinimumDecayTime = minimumDecayTime;
            this.MaximumDecayTime = maximumDecayTime;
        }
    }

    private record Values(int minimumDecayTime, int maximumDecayTime, boolean corrected) {
    }
}
