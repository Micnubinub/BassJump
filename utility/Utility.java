package tbs.jumpsnew.utility;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import tbs.jumpsnew.Game;
import tbs.jumpsnew.MainActivity;
import tbs.jumpsnew.managers.FileManager;
import tbs.jumpsnew.objects.Player;
import tbs.jumpsnew.ui.ColorView;
import tbs.jumpsnew.ui.ShapeView;

public class Utility {
    public static final String EQUIPPED_SONG = "EQUIPPED_SONG";
    public static final String EQUIPPED_SHAPE = "EQUIPPED_SHAPE";
    public static final String BOUGHT_SONGS = "BOUGHT_SONGS";
    public static final String BOUGHT_COLORS = "BOUGHT_COLORS";
    public static final String BOUGHT_SHAPES = "BOUGHT_SHAPES";
    public static final String COINS = "COINS";
    public static final int SONG_PRICE = 1000;
    public static final int COLOR_PRICE = 500;
    public static final String SEP = "//,/,//";
    public static final String COLOR_RED = "COLOR_RED";
    public static final String COLOR_BLUE = "COLOR_BLUE";
    public static final String COLOR_GREEN = "COLOR_GREEN";
    public static final String COLOR_YELLOW = "COLOR_YELLOW";
    public static final String COLOR_ORANGE = "COLOR_ORANGE";
    public static final String COLOR_PURPLE = "COLOR_PURPLE";
    public static final String COLOR_WHITE = "COLOR_WHITE";

    public static final String SHAPE_RECTANGLE = "SHAPE_RECTANGLE";
    public static final String SHAPE_TRIANGLE = "SHAPE_TRIANGLE";
    public static final String SHAPE_CIRCLE = "SHAPE_CIRCLE";
    public static final String SHAPE_PENTAGON = "SHAPE_PENTAGON";
    public static final String SHAPE_HEXAGON = "SHAPE_HEXAGON";

    public static final String SONG = "SONG";
    private static ArrayList<StoreItem> songs;
    private static final Runnable songRefresher = new Runnable() {
        @Override
        public void run() {
            songs = new ArrayList<>();
            songs.add(new StoreItem(StoreItem.Type.SONG, Uri.parse(
                    "android.resource://"
                            + Game.context.getApplicationInfo().packageName
                            + "/raw/song1").toString(), "Fall Back", "SmaXa",
                    0, true));
            final String boughtSongs = getBoughtSongs(Game.context);
            final ArrayList<File> songFiles = FileManager.scanForMusic();
            for (int i = 0; i < songFiles.size(); i++) {
                songs.add(getSongStoreItem(boughtSongs, songFiles.get(i)
                        .getAbsolutePath()));
            }
        }
    };

    public static void showToast(String a, Context context) {
        Toast.makeText(context, a, Toast.LENGTH_SHORT).show();
    }

    public static void refreshSongs() {
        try {
            new Thread(songRefresher).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Log Message
    public static void log(String message) {
        Log.e(MainActivity.TAG, message);
    }

    // Random in Range
    public static int randInt(int min, int max) {
        // Fix
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static float randFloat(int minX, int maxX) {
        return new Random().nextFloat() * (maxX - minX) + minX;
    }

    public static void addGameColors() {
        final ArrayList<StoreItem> colors = Utility
                .getColorStoreItems(Game.context);
        final ArrayList<StoreItem> tmp = new ArrayList<>();

        for (int i = 0; i < colors.size(); i++) {
            if (colors.get(i).bought)
                tmp.add(colors.get(i));
        }

        Game.colors = new int[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            Game.colors[i] = Utility.getColor(tmp.get(i).tag);
        }

    }

    public static int generateRange(int num) {
        return Utility.randInt(-num / 3, num / 3);
    }

    // Stop Thread
    public static void StopThread(Thread thread) {
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    // Resize Bitmap
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        final Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    // Load Bitmap from Location
    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
        }
        return bitmap;
    }

    public static void saveCoins(Context context, int coins) {
        getPrefs(context).put(COINS, String.valueOf(coins));
    }

    public static int getCoins(Context context) {
        int coins = 0;
        try {
            coins = Integer.parseInt(getPrefs(context).getString(COINS));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coins;
    }

    public static ArrayList<StoreItem> getColorStoreItems(Context context) {
        final ArrayList<StoreItem> items = new ArrayList<>();

        final String boughtColors = getBoughtColors(context);
        final String[] colors = new String[]{COLOR_BLUE, COLOR_RED,
                COLOR_ORANGE, COLOR_YELLOW, COLOR_WHITE, COLOR_GREEN,
                COLOR_PURPLE};
        for (String color : colors) {
            items.add(getColorStoreItem(boughtColors, color));
        }

        return items;
    }

    public static ArrayList<StoreItem> getShapeStoreItems(Context context) {
        final ArrayList<StoreItem> items = new ArrayList<>();

        final String boughtShapes = getBoughtShapes(context);
        final String[] shapes = new String[]{SHAPE_RECTANGLE, SHAPE_TRIANGLE,
                SHAPE_CIRCLE, SHAPE_PENTAGON, SHAPE_HEXAGON};
        for (String shape : shapes) {
            items.add(getShapeStoreItem(boughtShapes, shape));
        }

        return items;
    }

    public static ArrayList<StoreItem> getSongStoreItems(Context context) {

        return songs;
    }

    public static View getColor(Context context, String tag) {
        View view = new ColorView(context, getColor(tag));
        view.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        return view;
    }

    private static int getColor(String tag) {
        // Todo sidney tweak colors
        int i = 0xffffffff;
        if (tag.contains(COLOR_BLUE))
            i = 0xff327ae5;
        else if (tag.contains(COLOR_GREEN))
            i = 0xff32e532;
        else if (tag.contains(COLOR_ORANGE))
            i = 0xffe57e32;
        else if (tag.contains(COLOR_PURPLE))
            i = 0xffe532cd;
        else if (tag.contains(COLOR_RED))
            i = 0xffd54040;
        else if (tag.contains(COLOR_YELLOW))
            i = 0xffffeb3b;
        return i;
    }

    public static View getShape(Context context, String tag) {
        final View view = new ShapeView(context, getShapeType(tag));
        view.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        return view;
    }

    public static Player.PlayerShape getShapeType(String tag) {
        Player.PlayerShape shape = Player.PlayerShape.RECT;

        if (tag.contains(SHAPE_CIRCLE))
            shape = Player.PlayerShape.CIRCLE;
        else if (tag.contains(SHAPE_PENTAGON))
            shape = Player.PlayerShape.PENTAGON;
        else if (tag.contains(SHAPE_TRIANGLE))
            shape = Player.PlayerShape.TRIANGLE;
        else if (tag.contains(SHAPE_HEXAGON))
            shape = Player.PlayerShape.HEXAGON;
        return shape;
    }

    public static String getEquippedShape(Context context) {
        String out = getPrefs(context).getString(EQUIPPED_SHAPE);
        out = out == null ? "" : out;
        return out;
    }

    public static String getEquippedSong(Context context) {
        String out = getPrefs(context).getString(EQUIPPED_SONG);
        out = out == null ? "" : out;
        return out;
    }

    public static void equipShape(Context context, String tag) {
        getPrefs(context).put(EQUIPPED_SHAPE, tag);
        Player.setPlayerShape(getShapeType(tag));
    }

    public static void equipSong(Context context, String tag) {
        getPrefs(context).put(EQUIPPED_SONG, tag);
        if (tag == null || tag.length() < 1)
            Game.playDefaultSong();
        else {
            try {
                Game.playSong(tag);
            } catch (Exception e) {
                Game.playDefaultSong();
            }
        }
    }

    public static String getBoughtShapes(Context context) {
        StringBuilder builder = new StringBuilder();
        builder.append(SHAPE_RECTANGLE);

        String out = getPrefs(context).getString(BOUGHT_SHAPES);
        out = out == null ? "" : out;

        if (out.length() < 2)
            return builder.toString();

        builder.append(SEP);
        builder.append(out);
        return builder.toString();
    }

    public static String getBoughtSongs(Context context) {
        final StringBuilder builder = new StringBuilder();
        builder.append("android.resource://"
                + context.getApplicationInfo().packageName + "/raw/song1");

        String out = getPrefs(context).getString(BOUGHT_SONGS);
        out = out == null ? "" : out;

        if (out.length() < 2)
            return builder.toString();

        builder.append(SEP);
        builder.append(out);
        return builder.toString();
    }

    public static String getBoughtColors(Context context) {
        StringBuilder builder = new StringBuilder();
        builder.append(COLOR_BLUE);
        builder.append(SEP);
        builder.append(COLOR_RED);

        String out = getPrefs(context).getString(BOUGHT_COLORS);
        out = out == null ? "" : out;

        if (out.length() < 2)
            return builder.toString();
        builder.append(SEP);
        builder.append(out);
        return builder.toString();
    }

    public static void addBoughtShapes(Context context, String tag) {
        final StringBuilder builder = new StringBuilder();
        builder.append(getBoughtShapes(context));
        if (builder.toString().length() > 1)
            builder.append(SEP);
        builder.append(tag);
        getPrefs(context).put(BOUGHT_SHAPES, builder.toString());
    }

    public static void addBoughtSongs(Context context, String tag) {
        final StringBuilder builder = new StringBuilder();
        builder.append(getBoughtSongs(context));
        if (builder.toString().length() > 1)
            builder.append(SEP);
        builder.append(tag);
        getPrefs(context).put(BOUGHT_SONGS, builder.toString());
    }

    public static void addBoughtColors(Context context, String tag) {
        final StringBuilder builder = new StringBuilder();
        builder.append(getBoughtColors(context));
        if (builder.toString().length() > 1)
            builder.append(SEP);
        builder.append(tag);
        getPrefs(context).put(BOUGHT_COLORS, builder.toString());
        addGameColors();
    }

    public static StoreItem getShapeStoreItem(String boughtShapes, String tag) {
        return new StoreItem(StoreItem.Type.SHAPE, tag, getShapeName(tag),
                "Shape", getShapePrice(tag), boughtShapes.contains(tag));
    }

    private static String getShapeName(String tag) {
        String shape = null;

        if (tag.contains(SHAPE_CIRCLE))
            shape = "Circle";
        else if (tag.contains(SHAPE_HEXAGON))
            shape = "Hexagon";
        else if (tag.contains(SHAPE_TRIANGLE))
            shape = "Triangle";
        else if (tag.contains(SHAPE_PENTAGON))
            shape = "Pentagon";
        else
            shape = "Rectangle";

        return shape;
    }

    private static int getShapePrice(String tag) {
        int price = 0;
        if (tag.contains(SHAPE_TRIANGLE))
            price = 600;
        else if (tag.contains(SHAPE_CIRCLE))
            price = 1500;
        else if (tag.contains(SHAPE_PENTAGON))
            price = 2500;
        else if (tag.contains(SHAPE_HEXAGON))
            price = 10000;

        return price;
    }

    public static StoreItem getColorStoreItem(String boughtColors, String tag) {
        return new StoreItem(StoreItem.Type.COLOR, tag, getColorName(tag),
                "Color", COLOR_PRICE, boughtColors.contains(tag));
    }

    private static String getColorName(String tag) {
        String color = "Blue";
        if (tag.contains(COLOR_YELLOW))
            color = "Yellow";
        else if (tag.contains(COLOR_RED))
            color = "Red";
        else if (tag.contains(COLOR_PURPLE))
            color = "Purple";
        else if (tag.contains(COLOR_ORANGE))
            color = "Orange";
        else if (tag.contains(COLOR_GREEN))
            color = "Green";
        else if (tag.contains(COLOR_WHITE))
            color = "White";
        return color;
    }

    public static StoreItem getSongStoreItem(String boughtSongs, String song) {
        final String[] songDetails = getSongTitle(song).split(SEP);

        if (songDetails[1] == null || songDetails[0].length() < 1
                || songDetails[0].equals("null"))
            songDetails[1] = new File(song).getName();

        if (songDetails[0] == null || songDetails[1].length() < 1
                || songDetails[1].equals("null"))
            songDetails[0] = "Unknown Artist";

        return new StoreItem(StoreItem.Type.SONG, song, songDetails[0],
                songDetails[1], SONG_PRICE, boughtSongs.contains(song));
    }

    public static String getSongTitle(String song) {
        try {
            final MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(song);

            final StringBuilder builder = new StringBuilder();
            builder.append(mmr
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
            builder.append(SEP);
            builder.append(mmr
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));

            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Unknown Song" + SEP + "Unknown Artist";
    }

    public static SecurePreferences getPrefs(Context context) {
        return new SecurePreferences(context, "prefs_tbs_n", "X5TBSSDVSHYGF",
                true);
    }
}
