package me.jiogo18.noisymobs.common.engine.distribution;

public enum SoundDistribution {
    VANILLA("vanilla"), // Vanilla distribution : Rayleigh with σ² = 986.27
    DISCUSSION("discussion"), // Lots of sounds in a row, then silence for a longer time
    ;

    private final String key;

    SoundDistribution(String key) {
        this.key = key;
    }

    public String getTranslateKey() {
        return "noisymobs.sound_distribution." + key;
    }

    public AbstractDistribution create() {
        switch (this) {
            case VANILLA:
                return new VanillaDistribution();
            case DISCUSSION:
                return new DiscussionDistribution();
            default:
                return null;
        }
    }
}