package none.alchemy.cld;

import none.alchemy.cld.platform.Services;

public final class ControlledLeafDecay {
    private ControlledLeafDecay() {
    }

    public static void init() {
        Constants.LOG.info("Initializing {} on {}", Constants.MOD_NAME, Services.PLATFORM.getPlatformName());
    }
}
