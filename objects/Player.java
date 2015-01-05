package tbs.jumpsnew.objects;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

import tbs.jumpsnew.Game;
import tbs.jumpsnew.GameMode;
import tbs.jumpsnew.GameValues;
import tbs.jumpsnew.MainActivity;
import tbs.jumpsnew.Screen;
import tbs.jumpsnew.levels.Level;
import tbs.jumpsnew.levels.Platform;
import tbs.jumpsnew.ui.CustomDialog;
import tbs.jumpsnew.utility.GameObject;
import tbs.jumpsnew.utility.Utility;

public class Player extends GameObject {
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // Color
    public static int paintIndex;
    public static PlayerShape playerShape;
    // Todo fix with scaling
    public static int paintTrailOffset;
    private static int[] points;
    // Michael's quick fix
    private static int cx, cy, l, angleOffSet, initRotation, rotationStep;
    private static double playerJumpDistance, playerJumpPercentage;
    // TMP till final fix
    private static int xOffset;
    private static boolean isStarShape;
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
    public int gamesPlayed; // GAMES PLAYED
    public int tmpCoins; // COINS WHEN PLAYING ONLY
    // SPECIAL ACHIEVEMENTS
    public int gamesHund; // Games over 100;
    // PAINT TRAIL
    public ArrayList<PaintParticle> paintTrail;

    public Player() {
        Utility.log("Player Initialized");
        splashParticles1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            splashParticles1.add(new Particle());
        }
        splashParticles2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            splashParticles2.add(new Particle());
        }
        playerJumpDistance = Screen.width - (GameValues.PLATFORM_WIDTH * 2)
                - GameValues.PLAYER_SCALE;
        Utility.equipShape(Game.context, Utility.getEquippedShape(Game.context));
    }

    public static void setPlayerShape(PlayerShape playerShape) {
        Player.playerShape = playerShape;
        l = Math.round((GameValues.PLAYER_SCALE / 2) / 0.7071f) - ((GameValues.PAINT_THICKNESS + 16) / 2);
        xOffset = 0;
        switch (playerShape) {
            case RECT:
                initRectAngle();
                break;
            case TRIANGLE:
                initTriangle();
                break;
            case PENTAGON:
                initPentagon();
                break;
            case HEXAGON:
                initHexagon();
                break;
            case CIRCLE:
                // l = (GameValues.PLAYER_SCALE / 2) - GameValues.PAINT_THICKNESS;
                break;
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((GameValues.PAINT_THICKNESS + 16));
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    private static void initRectAngle() {
        points = new int[8];
        initRotation = 45;
        rotationStep = 90;
        angleOffSet = 0;
        // calcPolyL();
    }

    private static void initTriangle() {
        points = new int[6];
        initRotation = 90;
        rotationStep = 120;
        angleOffSet = 30;
        // calcPolyL();
        xOffset = (int) (GameValues.PLAYER_SCALE * 0.1f);
    }

    private static void initPentagon() {
        points = new int[10];
        initRotation = 0;
        rotationStep = 72;
        angleOffSet = 72;
        // calcPolyL();
    }

    // private static void calcPolyL() {
    // final int n = points.length / 2;
    // final int cosAngle = ((n - 2) * 180) / (n * 2);
    // l = (int) ((GameValues.PLAYER_SCALE * 0.5f) /
    // Math.cos(Math.toRadians(cosAngle)));
    // }

    private static void initHexagon() {
        points = new int[12];
        initRotation = 30;
        rotationStep = 60;
        angleOffSet = 0;
        // calcPolyL();
    }

    public static void setShapeRotation(double rotation) {
        if (points == null || points.length <= 5)
            return;
        rotation += angleOffSet;

        if (isStarShape) {
            for (int i = 0; i < points.length; i += 4) {
                points[i] = cx + (int) (l * Math.cos(Math.toRadians(initRotation + (rotationStep * i / 2) + rotation)));
                points[i + 1] = cy + (int) (l * Math.sin(Math.toRadians(initRotation + (rotationStep * i / 2) + rotation)));

                points[i + 2] = cx + (int) ((l / 3) * Math.cos(Math.toRadians(initRotation + (rotationStep * i / 2) + rotation + (rotationStep / 2))));
                points[i + 3] = cy + (int) ((l / 3) * Math.sin(Math.toRadians(initRotation + (rotationStep * i / 2) + rotation + (rotationStep / 2))));
            }
        } else {
            for (int i = 0; i < points.length; i += 2) {
                points[i] = cx
                        + (int) (l * Math.cos(Math.toRadians(initRotation
                        + (rotationStep * i / 2) + rotation)));
                points[i + 1] = cy
                        + (int) (l * Math.sin(Math.toRadians(initRotation
                        + (rotationStep * i / 2) + rotation)));
            }
        }
    }

    public static void drawCircle(Canvas canvas) {
        canvas.drawCircle(cx, cy, l - ((GameValues.PAINT_THICKNESS + 16) / 2), paint);
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
        yPos = Screen.height; // START OFF SCREEN
        xPos = GameValues.PLATFORM_WIDTH;

        // OTHER
        state = PlayerState.ON_GROUND;
        score = 0;

        // PAINT:
        paintIndex = 0;
        paintTrail = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            paintTrail.add(new PaintParticle());
        }
        activatePaint(true);

        Utility.equipShape(Game.context, Utility.getEquippedShape(Game.context));
        tmpCoins = 0;
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
                paintTrail.get(i).yPos += GameValues.SPEED_FACTOR;
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
                    int speed = GameValues.SPEED_FACTOR;
                    if (Game.mode == GameMode.Recruit)
                        speed /= 1.5f;
                    if (speed < 1)
                        speed = 1;
                    paintTrail.get(paintIndex).height += speed;
                    paintTrail.get(paintIndex).yPos = (yPos + (GameValues.PLAYER_SCALE / 4));
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
                    if (xPos > Screen.width + GameValues.DEATH_GAP) {
                        deathActual();
                        Game.setupGame();
                    }
                }

                break;
            case JUMPING:
                if (goingRight) { // RIGHT
                    xPos += GameValues.PLAYER_JUMP_SPEED;
                    if ((xPos + scale) >= (Screen.width - GameValues.PLATFORM_WIDTH)) {
                        goingRight = (xPos < (Screen.width / 2));
                        if (isAlive(true))
                            land(true);
                        else
                            startDying();
                    }
                } else { // LEFT
                    xPos -= GameValues.PLAYER_JUMP_SPEED;
                    if (xPos <= (GameValues.PLATFORM_WIDTH)) {
                        goingRight = (xPos < (Screen.width / 2));
                        if (isAlive(true))
                            land(false);
                        else
                            startDying();
                    }
                }
                break;
        }
        playerJumpPercentage = (xPos - GameValues.PLATFORM_WIDTH)
                / playerJumpDistance;
    }

    public void startDying() {
        state = PlayerState.DYING;
    }

    public void deathActual() {

        // SCORE
        if (Game.mode == GameMode.Arcade) {
            if (highScoreA < score) {
                MainActivity.preferences.put("hScore", String.valueOf(score));
                highScoreA = score;
            }
        } else {
            if (highScoreR < score) {
                MainActivity.preferences.put("hScoreR", String.valueOf(score));
                highScoreR = score;
            }
        }

        // PLAYED AND DEATHS
        if (MainActivity.preferences.getString("gPlayed") != null) {
            gamesPlayed = Integer.parseInt(MainActivity.preferences
                    .getString("gPlayed")) + 1;
        } else {
            gamesPlayed = 0;
        }
        MainActivity.preferences.put("gPlayed", String.valueOf(gamesPlayed));

        if (score > 100 && Game.mode == GameMode.Arcade) {
            gamesHund += 1;
            if (gamesHund >= 10) {
                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQDg");
            }
        } else if (Game.mode == GameMode.Arcade) {
            gamesHund = 0;
        }

        // SAVE COINS
        Utility.saveCoins(Game.context, Utility.getCoins(Game.context)
                + tmpCoins);
        CustomDialog.setNumCoins(Utility.getCoins(Game.context));
    }

    public boolean isAlive(boolean j) {
        boolean fine = false;
        if (goingRight) {
            for (Platform p : Game.level.platformsRight) {
                if (yPos + scale >= p.yPos
                        && yPos + scale <= p.yPos + GameValues.PLATFORM_HEIGHT
                        * 1.15f) {
                    fine = true;
                    p.landedOn = true;
                }
                if (yPos >= p.yPos
                        && yPos <= p.yPos + GameValues.PLATFORM_HEIGHT * 1.15f) {
                    fine = true;
                    p.landedOn = true;
                }
            }
        } else {
            for (Platform p : Game.level.platformsLeft) {
                if (yPos + scale >= p.yPos
                        && yPos + scale <= p.yPos + GameValues.PLATFORM_HEIGHT
                        * 1.15f) {
                    fine = true;
                    p.landedOn = true;
                }
                if (yPos >= p.yPos
                        && yPos <= p.yPos + GameValues.PLATFORM_HEIGHT * 1.15f) {
                    fine = true;
                    p.landedOn = true;
                }
            }
        }
        return fine;
    }

    public boolean canPaint() {
        boolean fine = false;
        if (goingRight) {
            for (Platform p : Game.level.platformsRight) {
                if (yPos >= p.yPos
                        && yPos + scale <= p.yPos + GameValues.PLATFORM_HEIGHT
                        * 1.15f) {
                    fine = true;
                }
            }
        } else {
            for (Platform p : Game.level.platformsLeft) {
                if (yPos >= p.yPos
                        && yPos + scale <= p.yPos + GameValues.PLATFORM_HEIGHT
                        * 1.15f) {
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

        activatePaint(false);
    }

    public void activatePaint(boolean start) {
        // PAINT ACTIVATE:
        int moveIndex = 0;
        if (!(paintIndex == paintTrail.size() - 1)) {
            moveIndex = paintIndex + 1;
        }
        paintTrail.get(moveIndex).active = true;
        paintTrail.get(moveIndex).height = 0;
        paintTrail.get(moveIndex).yPos = yPos;
        if (!start) {
            if (!goingRight) {
                paintTrail.get(moveIndex).xPos = GameValues.PLATFORM_WIDTH
                        - GameValues.PAINT_THICKNESS;
            } else {
                paintTrail.get(moveIndex).xPos = (Screen.width - GameValues.PLATFORM_WIDTH);
            }
        } else {
            paintTrail.get(moveIndex).xPos = GameValues.PLATFORM_WIDTH
                    - GameValues.PAINT_THICKNESS;
        }
        paintIndex = moveIndex;
    }

    public void land(boolean right) {
        Game.lowBeat(310);
        Game.alphaM = 255; // BLITZ
        tmpCoins += 1;
        score += 1;
        // if (score % 5 == 0)
        Game.color = Game.colors[Utility.randInt(0, Game.colors.length - 1)];
        state = PlayerState.ON_GROUND;
        if (right) {
            xPos = ((Screen.width - GameValues.PLATFORM_WIDTH) - scale);
        } else {
            xPos = (GameValues.PLATFORM_WIDTH);
        }
        if (score % 2 == 0)
            GameValues.SPEED_BONUS += .005f;

        if (score % 18 == 0) {
            Level.powerCountdown = 4;
        }
        // / PARTICLES:
        showParticles();
        Game.showCircle(scale, getXCenter(), getYCenter(), 160, false);
    }

    public int getXCenter() {
        cx = xPos + (scale / 2);
        return cx;
    }

    public int getYCenter() {
        cy = yPos + (scale / 2);
        return cy;
    }

    public boolean IsInBox(int x1, int y1, int width1, int height1, int x2,
                           int y2, int width2, int height2) {
        int right1 = x1 + width1;
        int right2 = x2 + width2;
        int bottom1 = y1 + height1;
        int bottom2 = y2 + height2;

        // Check if top-left point is in box chexk && y2 >= y2
        if (x2 >= x1 && x2 <= right1 && y2 <= bottom1)
            return true;
        // Check if bottom-right point is in box
        return (right2 >= x1 && right2 <= right1 && bottom2 >= y2 && bottom2 <= bottom1);
    }

    public void showParticles() {
        if (goingRight) {
            for (int i = 0; i < splashParticles1.size(); i++) {
                splashParticles1.get(i).setup(xPos, getYCenter(), true);
            }
        } else {
            for (int i = 0; i < splashParticles2.size(); i++) {
                splashParticles2.get(i)
                        .setup(xPos + scale, getYCenter(), false);
            }
        }
    }

    public void drawPolygon(Canvas canvas) {
        if (points == null || points.length <= 5)
            return;
        for (int i = 0; i < points.length; i += 2) {
            // canvas.drawLine(points[i], points[i + 1], points[(i + 2)
            // % points.length], points[(i + 3) % points.length], paint);
            canvas.drawLine(goingRight ? points[i] - xOffset : points[i]
                            + xOffset, points[i + 1], goingRight ? points[(i + 2)
                            % points.length]
                            - xOffset : points[(i + 2) % points.length] + xOffset,
                    points[(i + 3) % points.length], paint);

        }
    }


    public void draw(Canvas canvas) {
        setShapeRotation(playerJumpPercentage * 180);
        paint.setColor(Game.color);
        switch (playerShape) {
            case CIRCLE:
                drawCircle(canvas);
                break;
            default:
                drawPolygon(canvas);
                break;
        }
    }

    public enum PlayerShape {
        CIRCLE, RECT, TRIANGLE, HEXAGON, PENTAGON, RECT_STAR, TRIANGLE_STAR, HEXAGON_STAR, PENTAGON_STAR
    }
}
