package tbs.bassjump.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import tbs.bassjump.Game;
import tbs.bassjump.GameValues;
import tbs.bassjump.utility.Utility;

/**
 * Created by Michael on 2/9/2015.
 */

public class TextView extends View {
    protected static final Color textColor = new Color();
    //Todo make it so that the text view cuts off the drawing once the text drawing position exceeds the height.. ellipsize
    public int padding, maxNumLines;
    protected int intTextColor = 0xffffffff;
    protected String text;
    protected float textScale = 0.2f, startingPoint;
    protected float textHeight, textWidth;
    protected Gravity gravity = Gravity.CENTER;

    public TextView() {
    }

    public TextView(String text) {
        setText(text);
    }

    public TextView(String text, Gravity gravity) {
        setText(text);
        this.gravity = gravity;
    }

    public TextView(Gravity gravity) {
        this.gravity = gravity;
    }

    @Override
    public void setWidth(float w) {
        this.w = w;
        getTextStrings();
    }

    @Override
    public void setHeight(float h) {
        super.setHeight(h);
    }

    @Override
    public void setSize(float w, float h) {
        super.setSize(w, h);
    }

    @Override
    public void setX(int x) {
        this.x = x;
        getTextStrings();
    }

    @Override
    public void setY(int y) {
        this.y = y;
        getTextStrings();
    }

    public void setMaxNumLines(int maxNumLines) {
        this.maxNumLines = maxNumLines;
    }

    public void setTextScale(float textScale) {
        this.textScale = textScale;
        Utility.getFont().getData().setScale(textScale);
        final float[] dimen = Utility.measureText(text);
        textWidth = dimen[0];
        textHeight = dimen[1];
        h = textHeight;
    }

    @Override
    public boolean click(int xPos, int yPos) {
        //Todo
        rect.set(x, y, w, GameValues.TITLE_HEIGHT);
        return rect.contains(xPos, Game.h - yPos - GameValues.TITLE_HEIGHT);
    }

    @Override
    public boolean fling(float vx, float vy) {
        return false;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (this.text != text) {
            Utility.getFont().getData().setScale(textScale);
            final float[] dimen = Utility.measureText(text);
            w = dimen[0];
        }
        this.text = text;
    }

    public void getTextStrings() {
//        if (text == null || text.length() < 1)
//            return;
//        Utility.getFont().getData().setScale(textScale);
//        final float[] dimen = Utility.measureText(text);
//        int numLines = (int) Math.ceil(dimen[0] / (w));
//        numLines = numLines > 25 ? 25 : numLines;
//
//        final int numLettersInOneLine = (int) Math.ceil(text.length() / numLines);
//        textHeight = dimen[1];
//        h = Math.round((textHeight * numLines) + (padding * (numLines + 1)));
//        startingPoint = (y + h - padding) - (textHeight / 2);
//        if (textStrings == null || numLines != textStrings.length)
//            textStrings = new String[numLines];
//        for (int i = 0; i < numLines; i++) {
//            String out = "";
//            if (!(numLines - 1 == i)) {
//                int index = i * numLettersInOneLine;
//                if (!(index + numLettersInOneLine > text.length()))
//                    out = text.substring(index, index + numLettersInOneLine);
//            } else {
//                out = text.substring(i * numLettersInOneLine, text.length());
//            }
//            textStrings[i] = out;
//        }
    }

    public void measure() {
        //Todo
    }

    protected void drawText(final SpriteBatch batch, final float relX, final float relY) {
//        for (int i = 0; i < textStrings.length; i++) {
//            if (gravity == null)
//                gravity = Gravity.LEFT;
        switch (gravity) {
            case CENTER:
                textColor.set(intTextColor);
                startingPoint = (y + h - padding) - (textHeight / 2);
                Utility.drawCenteredText(batch, textColor, text, relX + x + (w / 2), relY + startingPoint, textScale);
                break;
            case LEFT:
                textColor.set(intTextColor);
                Utility.drawLeftText(batch, textColor, text, relX + x, relY + y - textHeight, textScale);
                break;
            case RIGHT:

                break;
            default:
        }
        w = (Math.max(w, Utility.glyphLayout.width));
        h = (Math.max(h, Utility.glyphLayout.height));
//            Utility.drawCenteredText(spriteBatch, textColor, textStrings[i], textScale, relX + x + (w / 2), relY + startingPoint - (i * textHeight));
//        }

    }

    @Override
    public void draw(float relX, float relY, float parentRight, float parentTop) {
        drawText(Game.spriteBatch, relX, relY);
    }

    public void setGravity(Gravity gravity) {
        this.gravity = gravity;
        getTextStrings();
    }

    public void setTextColor(int textColor) {
        intTextColor = textColor;
    }

    @Override
    public void dispose() {
//        textStrings = null;
    }

    public enum Gravity {
        CENTER, LEFT, RIGHT
    }
}
