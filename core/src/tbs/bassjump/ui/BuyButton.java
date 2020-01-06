package tbs.bassjump.ui;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import tbs.bassjump.Game;
import tbs.bassjump.GameValues;
import tbs.bassjump.managers.BitmapLoader;
import tbs.bassjump.utility.Utility;

/**
 * Created by mike on 3/7/16.
 */
public class BuyButton extends TextView {
    public static final int EQUIP = 1, EQUIPPED = 3, BUY = 0, CANNOT_BUY = 2;
    private static ShaderProgram buy, cannotBuy, equip;
    private static boolean isInit;
    private static Sprite corner;
    public final int position;
    public float buttonX, buttonY, buttonW, buttonH;
    private int buttonMode = 0;

    public BuyButton(final int position) {
        this.position = position;
    }

    public static void init() {
        if (isInit)
            return;
        buy = Utility.getBuyButtonShaderProgram(0x4CAF50ff);
        cannotBuy = Utility.getBuyButtonShaderProgram(0xF44336ff);
        equip = Utility.getBuyButtonShaderProgram(0x2196F3ff);

        isInit = true;
    }

    public static void disposeShaders() {
        Utility.dispose(buy);
        Utility.dispose(cannotBuy);
        Utility.dispose(equip);
        isInit = false;
        corner = null;
    }

    private static void initCorner() {
        if (corner != null)
            return;
        corner = new Sprite(BitmapLoader.corner);
        corner.setSize(GameValues.CORNER_SCALE / 3, GameValues.CORNER_SCALE / 3);
    }

    public static void setCornerVals(float x, float y, float rotation) {
        corner.setOriginCenter();
        corner.setRotation(rotation);
        corner.setPosition(x, y);
    }

    public void setButtonMode(int buttonMode) {
        this.buttonMode = buttonMode;
    }

    public boolean click(int xPos, int yPos) {
        yPos = Game.h - yPos;
        rect.set(buttonX, buttonY, buttonW, buttonH);
        return rect.contains(xPos, yPos);
    }

    private void drawBackground(float relX, float relY) {
        initCorner();
        final int scale = GameValues.CORNER_SCALE / 3;
        final float left = relX + x - scale;
        final float bottom = relY + y + (h / 2) - scale;
        buttonX = left;
        buttonY = bottom;
        buttonW = w + scale + scale;
        buttonH = h + (4 * scale);

        setCornerVals(left, bottom + h + (3 * scale), 0);
        corner.draw(Game.spriteBatch);

        setCornerVals(left, bottom, 90);
        corner.draw(Game.spriteBatch);

        setCornerVals(left + w + scale, bottom + h + (3 * scale), 270);
        corner.draw(Game.spriteBatch);

        setCornerVals(left + w + scale, bottom, 180);
        corner.draw(Game.spriteBatch);

        Game.spriteBatch.draw(BitmapLoader.rect, left + scale, bottom, w, h + (4 * scale));
        Game.spriteBatch.draw(BitmapLoader.rect, left, bottom + scale, w + scale + scale, h + (2 * scale));
    }

    @Override
    public void draw(float relX, float relY, float parentRight, float parentTop) {
        switch (buttonMode) {
            case EQUIP:
                Game.spriteBatch.setShader(equip);
                break;
            case CANNOT_BUY:
                Game.spriteBatch.setShader(cannotBuy);
                break;
            case EQUIPPED:
                Game.spriteBatch.setShader(null);
                break;
            default:
                Game.spriteBatch.setShader(buy);
                break;
        }
        drawBackground(relX, relY);
        Game.spriteBatch.setShader(null);
        super.draw(relX, relY, parentRight, parentTop);
    }
}
