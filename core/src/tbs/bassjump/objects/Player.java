package tbs.bassjump.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

import tbs.bassjump.Game;
import tbs.bassjump.GameMode;
import tbs.bassjump.GameValues;
import tbs.bassjump.levels.Level;
import tbs.bassjump.levels.Platform;
import tbs.bassjump.managers.BitmapLoader;
import tbs.bassjump.utility.GameObject;
import tbs.bassjump.utility.Utility;
//Todo fix rotation
//Todo fix audio disabling
//Todo fix store
//Todo fix texture width
//Todo fix particles
//Todo add sprite 1x1 image of rain particles and walls sheet
//Todo add leader board
//Todo fix store click propagation

public class Player extends GameObject {
    // Color
    public static int paintIndex;
    public static Platform currentPlatform;
    // OTHER:
    static int speed;
    private static int cx, cy;
    private static double playerJumpDistance, playerJumpPercentage;
    // TMP till final fix
    private static Sprite playerTexture;
    // PARTICLES
    public final ArrayList<Particle> splashParticles1;
    public final ArrayList<Particle> splashParticles2;
    // MOVEMENT
    public boolean moving;
    public boolean canMove; // MAKE LEVEL MOVE INSTEAD
    public boolean goingRight; // 0 = LEFT, 1 = RIGHT
    // STATE
    public PlayerState state;
    // SCORE
    public int score;
    public int highScoreA; // ARCADE
    public int highScoreR; // RECRUIT
    public int highScoreU; // ULTRA
    public int highScoreS; // SINGUL
    public int highScoreS2; // SPEED
    public int gamesPlayed; // GAMES PLAYED
    public int tmpCoins; // COINS WHEN PLAYING ONLY
    // SPECIAL ACHIEVEMENTS
    public int gamesHund; // Games over 100;
    // PAINT TRAIL
    public ArrayList<PaintParticle> paintTrail;
//    private static boolean isTextureLoaded;


    public Player() {
        Utility.print("Player Initialized");
        splashParticles1 = new ArrayList<Particle>();
        for (int i = 0; i < 12; i++) {
            splashParticles1.add(new Particle());
        }
        splashParticles2 = new ArrayList<Particle>();
        for (int i = 0; i < 12; i++) {
            splashParticles2.add(new Particle());
        }
        playerJumpDistance = Game.w - (GameValues.PLATFORM_WIDTH * 2) - scale;
        Utility.equipShape(Utility.getEquippedShape());
    }

    public static void setPlayerSprite() {
        playerTexture = new Sprite(BitmapLoader.shapes[Utility.getEquippedShape()]);
    }

    public static void dispose() {

    }

    @Override
    public void setup() {
        super.setup();
        // MOVEMENT
        moving = false; // Initiated by Controller to make true
        canMove = true;
        goingRight = true; // GOING RIGHT

        // LOCATION & SCALE
        scale = GameValues.PLAYER_SCALE;
        yPos = Game.h; // START OFF SCREEN
        xPos = GameValues.PLATFORM_WIDTH;
        playerJumpPercentage = 0;

        // OTHER
        state = PlayerState.ON_GROUND;
        score = 0;

        // PAINT:
        paintIndex = 0;
        paintTrail = new ArrayList<PaintParticle>();
        for (int i = 0; i < 8; ++i) {
            paintTrail.add(new PaintParticle());
        }
        activatePaint(true, goingRight);

        tmpCoins = 0;

        setPlayerSprite();
    }

    public void update() {
        getXCenter();
        getYCenter();

        // STARTING ANIMATION
        if (yPos > GameValues.PLAYER_POSITION) {
            yPos -= GameValues.SPEED_FACTOR / 3;
            if (yPos < GameValues.PLAYER_POSITION)
                yPos = GameValues.PLAYER_POSITION;
        }

        // PAINT
        for (int i = 0; i < paintTrail.size(); ++i) {
            if (paintTrail.get(i).active) {
                speed = GameValues.SPEED_FACTOR;
                if (Game.mode == GameMode.Recruit) { // SLOW
                    speed /= 1.5f;
                    if (speed < 1)
                        speed = 1;
                } else if (Game.mode == GameMode.Ultra) { // ULTRA FAST
                    speed *= 1.35f;
                }
                paintTrail.get(i).yPos += speed;
            }
        }

        // PARTICLES:
        for (int i = 0; i < splashParticles1.size(); i++) {
            splashParticles1.get(i).update();
        }
        for (int i = 0; i < splashParticles2.size(); i++) {
            splashParticles2.get(i).update();
        }

        switch (state) {
            case ON_GROUND:
                if (!isAlive(false)) {
                    startDying();
                } else if (canPaint()) {
                    speed = GameValues.SPEED_FACTOR;
                    if (Game.mode == GameMode.Recruit) { // SLOW
                        speed /= 1.5f;
                        if (speed < 1)
                            speed = 1;
                    } else if (Game.mode == GameMode.Ultra) { // ULTRA FAST
                        speed *= 1.35f;
                    }
                    paintTrail.get(paintIndex).height += speed;
                    paintTrail.get(paintIndex).yPos = (yPos - (scale / 2));
                }
                break;
            case DYING:
                if (goingRight) {
                    xPos -= GameValues.PLAYER_JUMP_SPEED;
                    if (xPos + scale < -GameValues.DEATH_GAP) {
                        deathActual();
                        Game.setupGame();
                    }
                } else {
                    xPos += GameValues.PLAYER_JUMP_SPEED;
                    if (xPos > Game.w + GameValues.DEATH_GAP) {
                        deathActual();
                        Game.setupGame();
                    }
                }
                break;
            case JUMPING:
                playerJumpPercentage = (xPos - GameValues.PLATFORM_WIDTH) / playerJumpDistance;

                if (goingRight) { // RIGHT
                    xPos += GameValues.PLAYER_JUMP_SPEED;

                    if ((xPos) >= (Game.w - GameValues.PLATFORM_WIDTH + GameValues.PAINT_THICKNESS)) {
                        goingRight = (xPos < (Game.w / 2));
                        if (isAlive(true))
                            land(true);
                        else
                            startDying();
                    }
                } else { // LEFT
                    xPos -= GameValues.PLAYER_JUMP_SPEED;
                    if (xPos <= (GameValues.PLATFORM_WIDTH)) {
                        goingRight = (xPos < (Game.w / 2));
                        if (isAlive(true))
                            land(false);
                        else
                            startDying();
                    }
                }


                break;
        }
    }

    public void startDying() {
        state = PlayerState.DYING;
    }

    public void deathActual() {

        // SCORE
        if (Game.mode == GameMode.Arcade) {
            if (highScoreA < score) {
                Utility.saveInt("hScore", score);
                highScoreA = score;
            }
        } else if (Game.mode == GameMode.Recruit) {
            if (highScoreR < score) {
                Utility.saveInt("hScoreR", score);
                highScoreR = score;
            }
        } else if (Game.mode == GameMode.Ultra) {
            if (highScoreU < score) {
                Utility.saveInt("hScoreU", score);
                highScoreU = score;
            }
        } else if (Game.mode == GameMode.Singularity) {
            if (highScoreS < score) {
                Utility.saveInt("hScoreS", score);
                highScoreS = score;
            }
        } else if (Game.mode == GameMode.SpeedRunner) {
            if (highScoreS2 < score) {
                Utility.saveInt("hScoreS2", score);
                highScoreS2 = score;
            }
        }

        // PLAYED AND DEATHS

        gamesPlayed = Utility.getInt("gPlayed") + 1;
        Utility.saveInt("gPlayed", gamesPlayed);

        if (score > 100 && Game.mode == GameMode.Arcade) {
            gamesHund += 1;
            if (gamesHund >= 10) {
//                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQDg");
            }
        } else if (Game.mode == GameMode.Arcade) {
            gamesHund = 0;
        }

        // SAVE COINS
        Utility.saveCoins(Utility.getCoins() + tmpCoins);
    }

    public boolean isAlive(boolean j) {
        boolean fine = false;
        if (goingRight) {
            for (final Platform p : Game.level.platformsRight) {
                if (yPos + scale >= p.yPos
                        && yPos + scale <= p.yPos + GameValues.PLATFORM_HEIGHT
                        * 1.15f) {
                    fine = true;
                    p.landedOn = true;
                    currentPlatform = p;
                }
                if (yPos >= p.yPos
                        && yPos <= p.yPos + GameValues.PLATFORM_HEIGHT * 1.15f) {
                    fine = true;
                    p.landedOn = true;
                    currentPlatform = p;
                }
            }
        } else {
            for (final Platform p : Game.level.platformsLeft) {
                if (yPos + scale >= p.yPos
                        && yPos + scale <= p.yPos + GameValues.PLATFORM_HEIGHT
                        * 1.15f) {
                    fine = true;
                    p.landedOn = true;
                    currentPlatform = p;
                }
                if (yPos >= p.yPos
                        && yPos <= p.yPos + GameValues.PLATFORM_HEIGHT * 1.15f) {
                    fine = true;
                    p.landedOn = true;
                    currentPlatform = p;
                }
            }
        }
        return fine;
    }

    public boolean canPaint() {
        boolean fine = false;
        if (goingRight) {
            for (final Platform p : Game.level.platformsRight) {
                if (yPos >= p.yPos && yPos + scale <= p.yPos + GameValues.PLATFORM_HEIGHT * 1.15f) {
                    fine = true;
                }
            }
        } else {
            for (final Platform p : Game.level.platformsLeft) {
                if (yPos >= p.yPos && yPos + scale <= p.yPos + GameValues.PLATFORM_HEIGHT * 1.15f) {
                    fine = true;
                }
            }
        }
        return fine;
    }

    public void jump() {
        if (state == PlayerState.ON_GROUND) {
            state = PlayerState.JUMPING;
        }

        activatePaint(false, goingRight);
    }

    public void activatePaint(boolean start, boolean goingRight) {
        // PAINT ACTIVATE:

        int moveIndex = 0;
        if (!(paintIndex == paintTrail.size() - 1)) {
            moveIndex = paintIndex + 1;
        }
        final PaintParticle paintParticle = paintTrail.get(moveIndex);
        paintParticle.active = true;
        paintParticle.goingRight = goingRight;
        paintParticle.height = 0;
        paintParticle.yPos = yPos;
        if (!start) {
            if (!goingRight) {
                paintParticle.xPos = GameValues.PLATFORM_WIDTH;
            } else {
                paintParticle.xPos = (Game.w - GameValues.PLATFORM_WIDTH);
            }
        } else {
            paintParticle.xPos = GameValues.PLATFORM_WIDTH;
        }
        paintIndex = moveIndex;
    }

    public void land(boolean right) {
        int coinPluser = 1;
        Game.scoreTextMult = 1.5f;
        Game.alphaM = 255; // BLITZ
        score += 1;
        xPos = xPos < GameValues.PLATFORM_WIDTH ? GameValues.PLATFORM_WIDTH : xPos;
        xPos = xPos > Game.w - GameValues.PLATFORM_WIDTH + GameValues.PAINT_THICKNESS - scale ?
                Game.w - GameValues.PLATFORM_WIDTH + GameValues.STROKE_WIDTH - scale : xPos;

        // COIN BONUS:
        if (score >= 50) {
            coinPluser += 1;
        }
        if (score >= 100) {
            coinPluser += 2;
        }
        if (score >= 200) {
            coinPluser += 4;
        }
        tmpCoins += coinPluser;

        // if (score % 5 == 0)
        Game.color = Game.colors[Utility.randInt(0, Game.colors.length - 1)];
        state = PlayerState.ON_GROUND;

//        if (score % 2 == 0)
//            GameValues.SPEED_BONUS += .005f;

        if (score % 18 == 0) {
            Level.powerCountdown = 4;
        }
        // / PARTICLES:
        showParticles();

        // TEXT
        if (right) {
            playerJumpPercentage = 1;
            Game.showAnimatedText("+" + coinPluser, xPos, getYCenter(), (Game.h / 100), 12, 255, 0);
        } else {
            playerJumpPercentage = 0;
            Game.showAnimatedText("+" + coinPluser, xPos + GameValues.PLAYER_SCALE, getYCenter(),
                    (Game.h / 100), 12, 255, 0);
        }
    }

    public int getXCenter() {
        cx = xPos + (scale / 2);
        return cx;
    }

    public int getYCenter() {
        cy = yPos + (scale / 2);
        return cy;
    }

    public void showParticles() {
        if (goingRight) {
            for (int i = 0; i < splashParticles1.size(); i++) {
                splashParticles1.get(i).setup(xPos, getYCenter(), true);
            }
        } else {
            for (int i = 0; i < splashParticles2.size(); i++) {
                splashParticles2.get(i).setup(xPos + scale, getYCenter(), false);
            }
        }
    }

    public void draw(SpriteBatch canvas) {
        //Todo fix rotation
        final float rotation = (float) (playerJumpPercentage * 180);
        canvas.setShader(Game.shaderProgram);
        playerTexture.setOriginCenter();
        playerTexture.setRotation(rotation);
        playerTexture.setSize(scale, scale);
        playerTexture.setPosition(xPos, Game.h - yPos);
        playerTexture.draw(canvas);

        if ((Game.alphaM > 0)) {
            canvas.setShader(Game.flash);
            playerTexture.draw(canvas);
        }
        // SPLASH
        for (int i = 0; i < splashParticles1.size(); i++) {
            splashParticles1.get(i).draw(Game.spriteBatch);

        }
        for (int i = 0; i < splashParticles2.size(); i++) {
            splashParticles2.get(i).draw(Game.spriteBatch);
        }
    }


}
