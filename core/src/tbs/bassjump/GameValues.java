package tbs.bassjump;

public class GameValues {
    // VALUES FOR GLOBAL USE:

    // PLATFORMS:
    public static int PLATFORM_HEIGHT;
    public static int PLATFORM_WIDTH;
    public static int SPIKE_HEIGHT;

    // PAINT
    public static int PAINT_THICKNESS;
    public static int PAINT_GLOW_SCALE;
    public static int PAINT_OUTER_GLOW_SCALE;

    // PLAYER:
    public static int PLAYER_SCALE;
    public static int PLAYER_POSITION;
    public static int PAINT_HEIGHT;

    // SPLASH PARTICLES
    public static int SPLASH_MIN_SCALE;
    public static int SPLASH_MAX_SCALE;
    public static int DEATH_GAP;
    public static int STROKE_WIDTH;
    // INTERFACE:
    public static int BUTTON_SCALE;
    public static int BUTTON_PADDING;
    public static int COIN_SCALE;
    // SPEED PARTICLES
    public static int SPEED_PARTICLE_HEIGHT;
    public static int SPEED_PARTICLE_WIDTH;
    // SOUND FREQUENCY CALC:
    public static int FREQ_MAX_HEIGHT;
    public static int FREQ_WIDTH;
    public static int FREQ_MAX;
    // INTRO
    public static int LOADING_BAR_WIDTH;
    // PURCHASES:
    public static String IAP_1_ID = "buy_coins_1";
    public static String IAP_2_ID = "buy_coins_2";
    public static String IAP_3_ID = "buy_coins_3";
    public static String IAP_4_ID = "buy_removeads";
    public static String IAP_5_ID = "coin_doubler";
    public static int PLAYER_JUMP_SPEED_MULT;
    // GENERAL
    public static float SPEED_FACTOR_ORIGINAL;
    public static int SPEED_FACTOR;
    public static float SPEED_BONUS;
    public static int PLAYER_JUMP_SPEED;

    //DIALOG
    public static int DIALOG_PADDING;
    public static int DIALOG_CLOSE_BUTTON_SCALE;
    public static int TITLE_HEIGHT;
    public static int CORNER_SCALE;
    public static int SHAPE_WIDTH;

    public static void init() {
        // PLATFORMS:
        PLATFORM_HEIGHT = (int) (Game.h / 3.25f);
        PLATFORM_WIDTH = (int) (PLATFORM_HEIGHT / 4.5f);
        SPIKE_HEIGHT = (int) (PLATFORM_WIDTH / 1.65f);

        // PAINT
        PAINT_THICKNESS = PLATFORM_WIDTH / 18;
        PAINT_GLOW_SCALE = PAINT_THICKNESS / 2;
        PAINT_OUTER_GLOW_SCALE = PAINT_GLOW_SCALE * 3;

        // PLAYER:
        PLAYER_SCALE = (int) (Game.h / 15f);
        PLAYER_POSITION = (int) (Game.h - (PLAYER_SCALE * 4.5f));
        PAINT_HEIGHT = PLAYER_SCALE;

        // SPLASH PARTICLES
        SPLASH_MIN_SCALE = PLAYER_SCALE / 26;
        SPLASH_MAX_SCALE = (int) (SPLASH_MIN_SCALE * 5.25f);
        DEATH_GAP = Game.w;
        STROKE_WIDTH = Game.w / 52;
        // INTERFACE:
        BUTTON_SCALE = (int) (Game.w / 6.0f);
        BUTTON_PADDING = (int) (BUTTON_SCALE / 4.5f);
        COIN_SCALE = BUTTON_SCALE / 6;
        // SPEED PARTICLES
        SPEED_PARTICLE_HEIGHT = (Game.h / 2);
        SPEED_PARTICLE_WIDTH = SPEED_PARTICLE_HEIGHT / 84;
        // SOUND FREQUENCY CALC:
        FREQ_MAX_HEIGHT = (Game.w / 6);
        FREQ_WIDTH = FREQ_MAX_HEIGHT / 10;
        FREQ_MAX = 800;
        // INTRO
        LOADING_BAR_WIDTH = (int) (Game.w / 1.75f);
        // GENERAL
        SPEED_FACTOR_ORIGINAL = ((float) Game.h / 600);
        SPEED_FACTOR = (int) SPEED_FACTOR_ORIGINAL;
        SPEED_BONUS = 1;
        PLAYER_JUMP_SPEED_MULT = 3;
        PLAYER_JUMP_SPEED = 0;

        DIALOG_PADDING = Game.w / 20;
        DIALOG_CLOSE_BUTTON_SCALE = Game.w / 12;
        TITLE_HEIGHT = Game.w / 13;
        SHAPE_WIDTH = Game.h / 8;
        CORNER_SCALE = Game.w / 42;
    }

}
