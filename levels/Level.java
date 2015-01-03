package tbs.jumpsnew.levels;

import java.util.ArrayList;

import tbs.jumpsnew.Game;
import tbs.jumpsnew.GameMode;
import tbs.jumpsnew.GameState;
import tbs.jumpsnew.GameValues;
import tbs.jumpsnew.Screen;
import tbs.jumpsnew.objects.SpeedParticle;
import tbs.jumpsnew.utility.Utility;

public class Level {

    public static int powerCountdown;
    // PLATFORMS:
    public final ArrayList<Platform> platformsLeft;
    public final ArrayList<Platform> platformsRight;
    // PARTICLES
    public final ArrayList<SpeedParticle> speedParticles;
    private final int platformAmount = 5;
    public int platformIndexR;
    public int platformIndexL;
    public boolean gapRight;
    public int platformsPerSide;
    public boolean hadTwo;
    // MOVEMENT
    public boolean moving;
    public boolean canMove;

    public Level() {
        Utility.log("Level Initialized");
        platformIndexR = 0;
        platformIndexL = 0;
        platformsLeft = new ArrayList<Platform>(platformAmount);
        for (int i = 0; i < platformAmount; ++i) {
            Platform platform = new Platform();
            platform.height = GameValues.PLATFORM_HEIGHT;
            platform.width = GameValues.PLATFORM_WIDTH;
            platform.xPos = (Screen.width - GameValues.PLATFORM_WIDTH);
            platform.yPos = (i * GameValues.PLATFORM_HEIGHT);
            platformsLeft.add(platform);
        }
        platformsRight = new ArrayList<Platform>(platformAmount);
        for (int i = 0; i < platformAmount; ++i) {
            Platform platform = new Platform();
            platform.height = GameValues.PLATFORM_HEIGHT;
            platform.width = GameValues.PLATFORM_WIDTH;
            platform.xPos = 0;
            platform.yPos = (i * GameValues.PLATFORM_HEIGHT);
            platformsRight.add(platform);
        }
        speedParticles = new ArrayList<SpeedParticle>();
        for (int i = 0; i < 4; ++i) {
            SpeedParticle sp = new SpeedParticle();
            sp.xPos = (i * (Screen.width / 5));
            sp.xPos += Utility.randInt(-GameValues.SPEED_PARTICLE_WIDTH,
                    GameValues.SPEED_PARTICLE_WIDTH);
            speedParticles.add(sp);
        }
    }

    public void setup() {
        // MOVEMENT
        moving = false;
        canMove = true;

        // PLATFORMS
        hadTwo = false;
        powerCountdown = 0;
        gapRight = false;
        platformsPerSide = 0;
        platformIndexR = 0;
        platformIndexL = 0;
        for (int i = 0; i < platformAmount; ++i) {
            platformsLeft.get(i).yPos = (i * GameValues.PLATFORM_HEIGHT);
            platformsLeft.get(i).hasNext = true;
            platformsLeft.get(i).hasPrevious = true;
        }
        for (int i = 0; i < platformAmount; ++i) {
            platformsRight.get(i).yPos = (i * GameValues.PLATFORM_HEIGHT);
            platformsRight.get(i).hasNext = true;
            platformsRight.get(i).hasPrevious = true;
        }

        // SPEED PARTICLES
        for (int i = 0; i < speedParticles.size(); ++i) {
            speedParticles.get(i).yPos = Utility.randInt(-(Screen.height / 3),
                    Screen.height);
        }
    }

    public void update() {
        int speed = GameValues.SPEED_FACTOR;
        if (Game.mode == GameMode.Recruit)
            speed /= 1.5f;
        if (speed < 1)
            speed = 1;
        for (int i = 0; i < platformAmount; ++i) {
            platformsLeft.get(i).yPos += (speed);
            if (platformsLeft.get(platformIndexR).yPos >= 0) {
                int moveIndex;
                if (platformIndexR == 0) {
                    moveIndex = platformsLeft.size() - 1;
                } else {
                    moveIndex = platformIndexR - 1;
                }
                int gap = generateGap(true);
                if (gap > 0) {
                    platformsLeft.get(moveIndex).yPos = (platformsLeft
                            .get(platformIndexR).yPos - GameValues.PLATFORM_HEIGHT)
                            - gap;
                    platformsLeft.get(platformIndexR).hasNext = false;
                    platformsLeft.get(moveIndex).hasPrevious = false;
                    platformsLeft.get(moveIndex).hasSpikes = false;
                } else if (gap == 0) {
                    platformsLeft.get(moveIndex).yPos = (platformsLeft
                            .get(platformIndexR).yPos - GameValues.PLATFORM_HEIGHT);
                    platformsLeft.get(platformIndexR).hasNext = true; // NO GAP
                    platformsLeft.get(moveIndex).hasPrevious = true; // NO GAP
                    platformsLeft.get(moveIndex).hasSpikes = false;
                } else {
                    platformsLeft.get(moveIndex).yPos = (platformsLeft
                            .get(platformIndexR).yPos - GameValues.PLATFORM_HEIGHT);
                    platformsLeft.get(moveIndex).hasSpikes = true;
                }
                platformsLeft.get(moveIndex).landedOn = false;

                platformIndexR = moveIndex;
            }
        }
        for (int i = 0; i < platformAmount; ++i) {
            platformsRight.get(i).yPos += (speed);
            if (platformsRight.get(platformIndexL).yPos >= 0) {
                int moveIndex;
                if (platformIndexL == 0) {
                    moveIndex = platformsRight.size() - 1;
                } else {
                    moveIndex = platformIndexL - 1;
                }
                // Generate Gap:
                int gap = generateGap(false);
                if (gap > 0) {
                    platformsRight.get(moveIndex).yPos = (platformsRight
                            .get(platformIndexL).yPos - GameValues.PLATFORM_HEIGHT)
                            - gap;
                    platformsRight.get(platformIndexL).hasNext = false;
                    platformsRight.get(moveIndex).hasPrevious = false;
                    platformsRight.get(moveIndex).hasSpikes = false;
                } else if (gap == 0) {
                    platformsRight.get(moveIndex).yPos = (platformsRight
                            .get(platformIndexL).yPos - GameValues.PLATFORM_HEIGHT);
                    platformsRight.get(platformIndexL).hasNext = true; // NO GAP
                    platformsRight.get(moveIndex).hasPrevious = true; // NO GAP
                    platformsRight.get(moveIndex).hasSpikes = false;
                } else { // SPIKES
                    platformsRight.get(moveIndex).yPos = (platformsRight
                            .get(platformIndexL).yPos - GameValues.PLATFORM_HEIGHT);
                    platformsRight.get(moveIndex).hasSpikes = true;
                }

                platformIndexL = moveIndex;
            }
        }

        // SPEED PART:
        for (int i = 0; i < speedParticles.size(); ++i) {
            speedParticles.get(i).update();
        }
    }

    public int generateGap(boolean right) {
        if (Game.state == GameState.Playing) {
            int gap = 0;
            if (powerCountdown <= 0) {
                if (Game.state == GameState.Playing) {
                    if (right == gapRight)
                        platformsPerSide -= 1;
                    if (right == gapRight && platformsPerSide <= 0) {
                        if (Utility.randInt(0, 3) == 0) {
                            platformsPerSide = Utility.randInt(3, 4);
                        } else {
                            if (!hadTwo) {
                                platformsPerSide = Utility.randInt(2, 3);
                                hadTwo = (platformsPerSide == 2);

                            } else {
                                platformsPerSide = 3;
                            }
                        }

                        if (platformsPerSide > 1)
                            gap = Utility
                                    .randInt(
                                            (GameValues.PLATFORM_HEIGHT),
                                            ((GameValues.PLATFORM_HEIGHT) * (platformsPerSide - 1)));
                        else
                            gap = GameValues.PLATFORM_HEIGHT;
                        // if (Utility.randInt(0, 2) == 0) {
                        // gap = -1;
                        // }

                        gapRight = !gapRight;
                    }
                }
            } else {
                powerCountdown -= 1;
            }

            return gap;
        } else {
            return 0;
        }
    }
}
