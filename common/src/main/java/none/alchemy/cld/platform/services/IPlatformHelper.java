package none.alchemy.cld.platform.services;

import java.nio.file.Path;

public interface IPlatformHelper {
    String getPlatformName();

    Path getConfigDirectory();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
