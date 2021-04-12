package simplepets.brainsynder.api.pet;

public enum PetWeight {
    /**
     * Pet has no weight
     *      (remove effect)
     */
    NONE,
    /**
     * Is there even a pet here?
     *      Hardly any weight at all
     *      Slowness 1
     */
    LIGHT,
    /**
     * It weights a little bit
     *      Getting a little heavy
     *      Slowness 2
     */
    SLIGHTLY_HEAVY,
    /**
     * It is a bit heavy
     *      Ohhh no... starting to get even heavier...
     *      Slowness 3
     */
    HEAVY,
    /**
     * Does it need explaining...
     *    TOO HEAVY... Ohhhhhh my back...
     *    Slowness 4
     */
    YOUR_KILLING_ME
}
