package tbs.bassjump.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import tbs.bassjump.Game;
import tbs.bassjump.GameValues;
import tbs.bassjump.managers.BitmapLoader;
import tbs.bassjump.utility.Utility;


/**
 * Created by mike on 3/7/16.
 */
public class ShapesAdapter extends Adapter {
    public static BuyButton[] buyButtons;
    private static ShapeView shapeView = new ShapeView();
    private static TextureRegion[] regions;
    private static boolean[] itemsBought;
    private static int equippedItem;

    public static void getItemBought() {
        equippedItem = Utility.getEquippedShape();

        if (itemsBought == null)
            itemsBought = new boolean[regions.length];

        refreshRegions();
        //Todo convert int int[] later
        final String[] boughtCars = Utility.getBoughtShapes().split(Utility.SEP);
        buyButtons = new BuyButton[regions.length];
        for (int i = 0; i < regions.length; i++) {
            itemsBought[i] = i == 0 || Utility.contains(boughtCars, String.valueOf(i));
            final BuyButton buyButton = new BuyButton(i);
            buyButton.setTextScale(Utility.getScale(GameValues.TITLE_HEIGHT * 0.7f));
            buyButtons[i] = buyButton;
        }
    }

    public static void refreshRegions() {
        regions = BitmapLoader.shapes;
    }

    @Override
    public int getCount() {
        return regions.length;
    }

    @Override
    public boolean click(int x, int y) {
        for (final BuyButton buyButton : buyButtons) {
            if (buyButton.click(x, y)) {
                final int position = buyButton.position;
                if (itemsBought[position]) {
                    Utility.equipShape(position);
                    equippedItem = position;
                } else {
                    if (Game.coins < Utility.shapePrices[position]) {

                    } else {
                        Utility.addBoughtShapes(position);
                        itemsBought[position] = true;

                        Utility.saveCoins(Game.coins - Utility.shapePrices[position]);
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public View getView(int position) {
        ShapeView.text.setText(Utility.shapeNames[position]);
        ShapeView.price.setText(Utility.shapePricesS[position]);
        ShapeView.region = regions[position];
        final BuyButton buyButton = buyButtons[position];
        try {
            if (itemsBought[position]) {
                if (equippedItem == position) {
                    buyButton.setText("EQUIPPED");
                    buyButton.setButtonMode(BuyButton.EQUIPPED);
                } else {
                    buyButton.setText("EQUIP");
                    buyButton.setButtonMode(BuyButton.EQUIP);
                }
            } else {
                buyButton.setText("BUY");
                buyButton.setButtonMode((Game.coins < Utility.shapePrices[position]) ? BuyButton.CANNOT_BUY : BuyButton.BUY);
            }
            ShapeView.buyButton = buyButton;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shapeView;
    }

    public void dispose() {

    }

    private static class ShapeView extends View {
        public static final TextView price = new TextView();
        public static final TextView text = new TextView(TextView.Gravity.LEFT);
        public static BuyButton buyButton;
        public static TextureRegion region;
        private static float pad, scale, shapeScale, shapeXY;

        public ShapeView() {
            text.setTextColor(0xffffffff);
            pad = (int) (GameValues.SHAPE_WIDTH * 0.08f);
            scale = (GameValues.SHAPE_WIDTH);
            shapeScale = scale * 0.72f;
            shapeXY = (scale - shapeScale) / 2;
            h = scale + pad + pad;

            text.setTextScale(Utility.getScale(GameValues.TITLE_HEIGHT * 0.7f));
            price.setTextScale(Utility.getScale(GameValues.TITLE_HEIGHT * 0.7f));
        }

        @Override
        public void draw(float relX, float relY, float parentRight, float parentTop) {
            final float textY = relY + (h / 2);
            text.draw(relX + pad + pad + scale, textY, parentRight, parentTop);

            final float center = relY + (h / 2);
            final float priceY = center + (pad / 2);
            final float priceX = w - price.w;
            price.draw(priceX, priceY, parentRight, parentTop);
            Game.spriteBatch.draw(BitmapLoader.coin, priceX - (GameValues.TITLE_HEIGHT * 0.7f), priceY + (GameValues.TITLE_HEIGHT * 0.26f), GameValues.TITLE_HEIGHT * 0.52f, GameValues.TITLE_HEIGHT * 0.52f);
            buyButton.draw(w - buyButton.w, priceY - (pad) - (buyButton.h / 2) - GameValues.CORNER_SCALE, parentRight, parentTop);
            Game.spriteBatch.setShader(Game.shaderProgram);

            Game.spriteBatch.draw(region, relX + x + pad + shapeXY, relY + y + pad + shapeXY, shapeScale, shapeScale);
            Game.spriteBatch.setShader(null);
        }

        @Override
        public void dispose() {

        }

        @Override
        public boolean fling(float vx, float vy) {
            return false;
        }

        @Override
        public String toString() {
            return text.text;
        }
    }
}
