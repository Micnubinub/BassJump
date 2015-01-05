package tbs.jumpsnew;

public class GameValues {
    // VALUES FOR GLOBAL USE:

    // PLATFORMS:
    public static final int PLATFORM_HEIGHT = (int) (Screen.height / 3.25f);
    public static final int PLATFORM_WIDTH = (int) (PLATFORM_HEIGHT / 4.5f);
    public static final int SPIKE_HEIGHT = (int) (PLATFORM_WIDTH / 1.65f);
    // PAINT
    public static final int PAINT_THICKNESS = PLATFORM_WIDTH / 24;
    public static final int PAINT_GLOW_SCALE = PAINT_THICKNESS / 2;
    public static final int PAINT_OUTER_GLOW_SCALE = PAINT_GLOW_SCALE * 3;
    // PLAYER:
    public static final int PLAYER_SCALE = (int) (Screen.height / 16);
    public static final int PLAYER_POSITION = (int) (Screen.height - (PLAYER_SCALE * 4.5f));
    public static final int PAINT_HEIGHT = PLAYER_SCALE;
    // SPLASH PARTICLES
    public static final int SPLASH_MIN_SCALE = PLAYER_SCALE / 26;
    public static final int SPLASH_MAX_SCALE = (int) (SPLASH_MIN_SCALE * 5.25f);
    public static final int PLAYER_JUMP_SPEED_MULT = 3;
    public static final int DEATH_GAP = (int) Screen.width;
    public static final int STROKE_WIDTH = Screen.width / 50;
    // INTERFACE:
    public static final int BUTTON_SCALE = (int) (Screen.width / 6.0f);
    public static final int BUTTON_PADDING = (int) (BUTTON_SCALE / 4.5f);
    // SPEED PARTICLES
    public static final int SPEED_PARTICLE_HEIGHT = (int) (Screen.height / 2);
    public static final int SPEED_PARTICLE_WIDTH = SPEED_PARTICLE_HEIGHT / 84;
    // SOUND FREQUENCY CALC:
    public static final int FREQ_MAX_HEIGHT = (int) (Screen.width / 5.5f);
    public static final int FREQ_WIDTH = FREQ_MAX_HEIGHT / 10;
    public static final int FREQ_MAX = 800;
    // INTRO
    public static final int LOADING_BAR_WIDTH = (int) (Screen.width / 1.75f);
    // GENERAL
    public static float SPEED_FACTOR_ORIGINAL = 0;
    public static int SPEED_FACTOR = (int) SPEED_FACTOR_ORIGINAL;
    public static float SPEED_BONUS = 1;
    public static int PLAYER_JUMP_SPEED = 0;

}
