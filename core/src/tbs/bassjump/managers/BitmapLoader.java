package tbs.bassjump.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class BitmapLoader {
    // LOADING
    public static TextureRegion leader;
    public static TextureRegion sound;
    public static TextureRegion soundO;
    public static TextureRegion achiv;
    public static TextureRegion store;
    public static TextureRegion achievm;
    public static TextureRegion share;
    // MODES:
    public static TextureRegion modeArcade;
    public static TextureRegion modeRecruit;
    public static TextureRegion modeUltra;
    public static TextureRegion modeSingular;
    public static TextureRegion modeSpeed;

    public static TextureRegion particle;
    public static TextureRegion platform;
    public static TextureRegion platformFlash;
    public static TextureRegion paintFlash;
    public static TextureRegion intro;
    public static TextureRegion corner;
    public static TextureRegion close;
    public static TextureRegion rect;
    public static TextureRegion loadingBar;
    public static TextureRegion colorView;
    public static TextureRegion speedParticle;
    public static TextureRegion rectangle, circle, hexagon, triangle, pentagon, rhombus, pentagram;
    public static TextureRegion[] shapes;

    // COIN:
    public static TextureRegion coin;
    //Todo make texture atlas
    public static TextureAtlas atlas;

    //Todo add to atlas


    public BitmapLoader() {
        init();
    }

    public void init() {

        try {
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }

        atlas = new TextureAtlas(Gdx.files.internal("sprites"));

        // PLAYER:
        leader = atlas.findRegion("leader");
        sound = atlas.findRegion("sound");
        soundO = atlas.findRegion("soundoff");
        achiv = atlas.findRegion("achiv");
        store = atlas.findRegion("store");
        achievm = atlas.findRegion("achiv2");
        share = atlas.findRegion("share");

        // MODE:
        modeArcade = atlas.findRegion("modearcade");
        modeRecruit = atlas.findRegion("moderecruit");
        modeUltra = atlas.findRegion("modeultra");
        modeSingular = atlas.findRegion("modesingul");
        modeSpeed = atlas.findRegion("modespeed");

        // COIN:
        coin = atlas.findRegion("coin");
        particle = atlas.findRegion("particle");
        platform = atlas.findRegion("platform");
        platformFlash = atlas.findRegion("platformFlash");
        paintFlash = atlas.findRegion("paintFlash");
        intro = atlas.findRegion("intro");
        loadingBar = atlas.findRegion("loadingBar");
        colorView = atlas.findRegion("colorView");
        speedParticle = atlas.findRegion("speedParticle");

        rectangle = atlas.findRegion("4");
        circle = atlas.findRegion("0");
        hexagon = atlas.findRegion("6");
        triangle = atlas.findRegion("3");
        pentagon = atlas.findRegion("5");
        rhombus = atlas.findRegion("40");
        rect = atlas.findRegion("rect");
        close = atlas.findRegion("close");
        corner = atlas.findRegion("corner");
        shapes = new TextureRegion[]{rectangle, circle, hexagon, triangle, pentagon, rhombus};

//        pentagram= atlas.findRegion("coin");
    }

    public void dispose() {
        if (atlas != null)
            atlas.dispose();
    }

}
