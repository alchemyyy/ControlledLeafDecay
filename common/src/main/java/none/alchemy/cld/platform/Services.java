package none.alchemy.cld.platform;

import java.util.ServiceLoader;

import none.alchemy.cld.Constants;
import none.alchemy.cld.platform.services.IPlatformHelper;

public final class Services {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    private Services() {
    }

    private static <T> T load(Class<T> clazz) {
        T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
