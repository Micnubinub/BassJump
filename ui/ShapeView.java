package tbs.jumpsnew.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import tbs.jumpsnew.objects.Player;


/**
 * Created by root on 3/01/15.
 */
public class ShapeView extends View {
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int scoreBackground = 0xff404040;
    private static final int color = 0xffe5e475;
    private static int[] points;
    private Player.PlayerShape playerShape;
    private int w, h, thickness = 12, cx, cy, l, angleOffSet, initRotation, rotationStep;


    public ShapeView(Context context) {
        super(context);
        init();
    }

    public ShapeView(Context context, Player.PlayerShape playerShape) {
        super(context);
        this.playerShape = playerShape;
        init();
    }

    public ShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        playerShape = Player.PlayerShape.HEXAGON;
        init();
    }

    public static void drawRectangle(Canvas canvas, int w, int h) {
        paint.setColor(color);
        paint.setAlpha(255);
        canvas.drawRoundRect(new RectF(0, 0, w, w), 12, 12, paint);
        paint.setColor(scoreBackground);
        canvas.drawRoundRect(new RectF(12, 12, w - 12, w - 12), 12, 12, paint);
        // canvas.drawRoundRect(new RectF(GameValues.PAINT_THICKNESS, GameValues.PAINT_THICKNESS, w - GameValues.PAINT_THICKNESS, w - GameValues.PAINT_THICKNESS), 12, 12, paint);


    }

    public static void drawTriangle(Canvas canvas, int w, int h) {
        paint.setStrokeWidth(12);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(color);
        for (int i = 0; i < points.length; i += 2) {
            canvas.drawLine(points[i], points[i + 1], points[(i + 2) % points.length], points[(i + 3) % points.length], paint);
        }

    }

    public static void drawCircle(Canvas canvas, int w, int h) {
        //Todo
        paint.setColor(color);
        canvas.drawCircle(w / 2, h / 2, Math.min(w, h) / 2, paint);
        paint.setColor(scoreBackground);
        canvas.drawCircle(w / 2, h / 2, (Math.min(w, h) / 2) - 12, paint);
    }

    public static void drawPentagon(Canvas canvas, int w, int h) {
        //Todo
        paint.setColor(color);
        paint.setStrokeWidth(12);
        paint.setStrokeCap(Paint.Cap.ROUND);

        for (int i = 0; i < points.length; i += 2) {
            canvas.drawLine(points[i], points[i + 1], points[(i + 2) % points.length], points[(i + 3) % points.length], paint);
        }
    }

    public static void drawHexagon(Canvas canvas, int w, int h) {
        //Todo
        paint.setColor(color);
        paint.setStrokeWidth(12);
        paint.setStrokeCap(Paint.Cap.ROUND);

        for (int i = 0; i < points.length; i += 2) {
            canvas.drawLine(points[i], points[i + 1], points[(i + 2) % points.length], points[(i + 3) % points.length], paint);
        }

    }

    public void setPlayerShape(Player.PlayerShape playerShape) {
        this.playerShape = playerShape;
    }

    private void init() {
        cx = w / 2;
        cy = w / 2;
        l = Math.min(w, h) / 2;
        switch (playerShape) {
            case TRIANGLE:
                initTriangle();
                break;
            case PENTAGON:
                initPentagon();
                break;
            case HEXAGON:
                initHexagon();
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setShapeRotation(0);

        switch (playerShape) {
            case RECT:
                drawRectangle(canvas, w, h);
                break;
            case CIRCLE:
                drawCircle(canvas, w, h);
                break;
            case TRIANGLE:
                drawTriangle(canvas, w, h);
                break;
            case PENTAGON:
                drawPentagon(canvas, w, h);
                break;
            case HEXAGON:
                drawHexagon(canvas, w, h);
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        init();
    }

    private void initTriangle() {
        points = new int[6];
        initRotation = 90;
        rotationStep = 120;
        angleOffSet = 30;
    }

    private void initPentagon() {
        points = new int[10];
        initRotation = 0;
        rotationStep = 72;
        angleOffSet = 72;
    }

    private void initHexagon() {
        points = new int[12];
        initRotation = 30;
        rotationStep = 60;
        angleOffSet = 0;
    }

    public void setShapeRotation(double rotation) {
        rotation = (rotation % 180) + angleOffSet;
        for (int i = 0; i < points.length; i += 2) {
            points[i] = cx + (int) (l * Math.cos(Math.toRadians(initRotation + (rotationStep * i / 2) + rotation)));
            points[i + 1] = cy + (int) (l * Math.sin(Math.toRadians(initRotation + (rotationStep * i / 2) + rotation)));
        }
    }
}
