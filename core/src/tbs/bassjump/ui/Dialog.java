package tbs.bassjump.ui;

import com.badlogic.gdx.graphics.g2d.Sprite;

import tbs.bassjump.Game;
import tbs.bassjump.GameValues;
import tbs.bassjump.managers.BitmapLoader;
import tbs.bassjump.utility.Utility;

/**
 * Created by Michael on 3/8/2015.
 */
public class Dialog extends View {
    public static Sprite corner;
    protected static Pages pages;
    private static Button close = new Button();
    //Todo somehow make it draw over other objects
    private static TextView title = new TextView("STORE");
    private static TextView colorTitle = new TextView("COLORS");
    private static TextView shapeTitle = new TextView("SHAPES");
    private static boolean showDialog = false;
    private static int pad;

    public Dialog() {
        Dialog.pages = new Pages();
        setWidth(Game.w - GameValues.DIALOG_PADDING - GameValues.DIALOG_PADDING);
        setHeight(Game.h - GameValues.DIALOG_PADDING - GameValues.DIALOG_PADDING);
    }

    public static void setCornerVals(float x, float y, float rotation) {
        corner.setOriginCenter();
        corner.setRotation(rotation);
        corner.setPosition(x, y);
    }

    @Override
    public void dispose() {
        corner = null;
        pages.dispose();
    }

    @Override
    public boolean drag(float startX, float startY, float dx, float dy) {
        return pages.drag(startX, startY, dx, dy);
    }

    @Override
    public boolean fling(float vx, float vy) {
        return pages.fling(vx, vy);
    }

    @Override
    public void draw(float relX, float relY, float relW, float relH) {
        if (!showDialog)
            return;
        drawBackground();

        if (!showDialog) {
            lastRelX = relX;
            lastRelY = relY;
        } else {
            //Todo make sure its drawn in the center, with padding(add to the values you are meant to create in utility >utility.dialog_padding = 1...
            if (pages != null) {
                pages.onDraw(relX + x, relY + y + pad, w, h - GameValues.TITLE_HEIGHT - GameValues.TITLE_HEIGHT);
            }
        }

        title.draw(relX + x, relY + y, w, h);
        colorTitle.draw(relX + x, relY + y, w, h);
        shapeTitle.draw(relX + x, relY + y, w, h);

        Game.spriteBatch.draw(BitmapLoader.close, close.xPos, close.yPos, GameValues.DIALOG_CLOSE_BUTTON_SCALE, GameValues.DIALOG_CLOSE_BUTTON_SCALE);
    }

    @Override
    public void setHeight(float h) {
        h = h < Values.DIALOG_PADDING ? Values.DIALOG_PADDING : h;
        final int diff = Game.h - Values.DIALOG_PADDING - Values.DIALOG_PADDING;
        this.h = h > diff ? diff : h;
        x = (int) ((Game.w - pages.w) / 2);
        y = (int) ((Game.h - pages.h) / 2);
    }

    @Override
    public boolean click(int xPos, int yPos) {
        rect.set(x, y, w, h);
        if (close.click(xPos, Game.h - yPos - close.height) || !rect.contains(xPos, yPos)) {
            dismiss();
            return true;
        } else if (colorTitle.click(xPos, yPos)) {
            pages.setCurrentPage(0);
        } else if (shapeTitle.click(xPos, yPos)) {
            pages.setCurrentPage(1);
        }
        return pages.click(xPos, yPos);
    }

    @Override
    public void setWidth(float w) {
        w = w < Values.DIALOG_PADDING ? Values.DIALOG_PADDING : w;
        final int diff = Game.w - Values.DIALOG_PADDING - Values.DIALOG_PADDING;
        this.w = w > diff ? diff : w;
        x = (int) ((Game.w - pages.w) / 2);
        y = (int) ((Game.h - pages.h) / 2);
    }

    public void dismiss() {
        showDialog = false;
        Utility.print("dismiss");
    }

    public void show() {
        if (showDialog == true)
            return;

        pages.init();
        showDialog = true;
        close.width = GameValues.DIALOG_CLOSE_BUTTON_SCALE;
        close.height = GameValues.DIALOG_CLOSE_BUTTON_SCALE;
        if (corner == null) {
            corner = new Sprite(BitmapLoader.corner);
        }
        corner.setSize(GameValues.CORNER_SCALE, GameValues.CORNER_SCALE);

        pad = GameValues.DIALOG_PADDING;
        final int scale = GameValues.CORNER_SCALE;

        final int top = Game.h - pad - scale - pad;
        x = pad;
        y = Game.coinTextTop + (pad / 8);
        w = Game.w - pad - pad;
        h = top - pad;

        ShapesAdapter.refreshRegions();
        ColorAdapter.refreshRegions();

        ColorAdapter.getItemBought();
        ShapesAdapter.getItemBought();

        title.setTextColor(0xffffffff);

        title.setPosition(pad, top - GameValues.TITLE_HEIGHT - GameValues.TITLE_HEIGHT);
        colorTitle.setPosition(pad, top - GameValues.TITLE_HEIGHT - GameValues.TITLE_HEIGHT - GameValues.TITLE_HEIGHT);
        shapeTitle.setPosition(pad + (int) (w / 2), top - GameValues.TITLE_HEIGHT - GameValues.TITLE_HEIGHT - GameValues.TITLE_HEIGHT);

        title.setSize((int) w, GameValues.TITLE_HEIGHT);
        colorTitle.setSize((int) w / 2, GameValues.TITLE_HEIGHT);
        shapeTitle.setSize((int) w / 2, GameValues.TITLE_HEIGHT);

        title.setTextScale(Utility.getScale(GameValues.TITLE_HEIGHT * 0.95f));
        colorTitle.setTextScale(Utility.getScale(GameValues.TITLE_HEIGHT * 0.75f));
        shapeTitle.setTextScale(Utility.getScale(GameValues.TITLE_HEIGHT * 0.75f));

        final int padding = (int) (GameValues.SHAPE_WIDTH * 0.08f);
        pages.setSize(w, h - GameValues.TITLE_HEIGHT - GameValues.TITLE_HEIGHT - (3 * padding));
        pages.setPosition(pad, pad);

        //set pad for inner dialog
        pad = (int) (GameValues.SHAPE_WIDTH * 0.08f);
    }

    private void drawBackground() {
        final int pad = GameValues.DIALOG_PADDING;
        final int scale = GameValues.CORNER_SCALE;

        final int top = Game.h - pad - scale - pad;

        colorTitle.setTextColor((pages.getCurrentPage() == 0) ? 0xffffffff : 0xaaaaaaff);
        shapeTitle.setTextColor((pages.getCurrentPage() == 1) ? 0xffffffff : 0xaaaaaaff);

        setCornerVals(pad, top, 0);
        corner.draw(Game.spriteBatch);

        setCornerVals(pad, y, 90);
        corner.draw(Game.spriteBatch);

        setCornerVals(Game.w - pad - scale, top, 270);
        corner.draw(Game.spriteBatch);

        setCornerVals(Game.w - pad - scale, y, 180);
        corner.draw(Game.spriteBatch);

        Game.spriteBatch.draw(BitmapLoader.rect, pad, y + scale, Game.w - pad - pad, top - y - scale);
        Game.spriteBatch.draw(BitmapLoader.rect, pad + scale, y, Game.w - pad - scale - pad - scale, top - y + scale);
        close.xPos = Game.w - pad - (GameValues.DIALOG_CLOSE_BUTTON_SCALE / 2);
        close.yPos = top - (scale / 2) - pad + (GameValues.DIALOG_CLOSE_BUTTON_SCALE / 2);
    }

    public boolean isShowing() {
        return showDialog;
    }

    private void getShaderPrograms() {
        dispose();
        pages.dispose();
    }


}
