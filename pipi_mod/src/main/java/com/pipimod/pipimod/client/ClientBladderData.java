package com.pipimod.pipimod.client;

public class ClientBladderData {
    private static float level;
    private static float capacity = 1f;
    private static boolean urinating;
    private static int discomfortStage;

    public static void update(float lvl, float cap, boolean isUrinating, int stage) {
        level = lvl;
        capacity = Math.max(1f, cap);
        urinating = isUrinating;
        discomfortStage = stage;
    }

    public static float getFillRatio() {
        return Math.min(1.25f, level / Math.max(1f, capacity));
    }

    public static float getLevel() {
        return level;
    }

    public static float getCapacity() {
        return capacity;
    }

    public static boolean isUrinating() {
        return urinating;
    }

    public static int getDiscomfortStage() {
        return discomfortStage;
    }
}
