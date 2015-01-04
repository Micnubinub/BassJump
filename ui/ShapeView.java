package tbs.jumpsnew.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import tbs.jumpsnew.Game;
import tbs.jumpsnew.objects.Player;


/**
 * Created by root on 3/01/15.
 */
public class ShapeView extends View {
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int scoreBackground = 0xff404040;
    private static final int color = 0xffe5e475;
    private static int thickness = 12;
    private int[] points;
    private Player.PlayerShape playerShape;
    private int w, h, cx, cy, l, angleOffSet, initRotation, rotationStep;

    public ShapeView(Context context, Player.PlayerShape playerShape) {
        super(context);

        init(playerShape);
    }

    public ShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(Player.PlayerShape.HEXAGON);
    }

    public static void drawCircle(Canvas canvas, int w, int h) {
        paint.setColor(Game.color);
        canvas.drawCircle(w / 2, h / 2, (Math.min(w, h) / 2) - thickness, paint);
        paint.setColor(scoreBackground);
        canvas.drawCircle(w / 2, h / 2, (Math.min(w, h) / 2) - (thickness * 2), paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

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
        init(playerShape);
    }

    public void drawRectangle(Canvas canvas, int w, int h) {
        paint.setStrokeWidth(thickness);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Game.color);
        for (int i = 0; i < points.length; i += 2) {
            canvas.drawLine(points[i], points[i + 1], points[(i + 2) % points.length], points[(i + 3) % points.length], paint);
        }
    }

    public void drawTriangle(Canvas canvas, int w, int h) {
        paint.setStrokeWidth(thickness);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Game.color);
        for (int i = 0; i < points.length; i += 2) {
            canvas.drawLine(points[i], points[i + 1], points[(i + 2) % points.length], points[(i + 3) % points.length], paint);
        }
    }

    public void drawPentagon(Canvas canvas, int w, int h) {
        paint.setColor(Game.color);
        paint.setStrokeWidth(thickness);
        paint.setStrokeCap(Paint.Cap.ROUND);

        for (int i = 0; i < points.length; i += 2) {
            canvas.drawLine(points[i], points[i + 1], points[(i + 2) % points.length], points[(i + 3) % points.length], paint);
        }
    }

    public void drawHexagon(Canvas canvas, int w, int h) {
        paint.setColor(Game.color);
        paint.setStrokeWidth(thickness);
        paint.setStrokeCap(Paint.Cap.ROUND);

        for (int i = 0; i < points.length; i += 2) {
            canvas.drawLine(points[i], points[i + 1], points[(i + 2) % points.length], points[(i + 3) % points.length], paint);
        }

    }

    private void init(Player.PlayerShape playerShape) {
        this.playerShape = playerShape;
        cx = w / 2;
        cy = h / 2;
        thickness = Math.min(w, h) / 12;
        l = (Math.min(w, h) / 2) - thickness;
        switch (playerShape) {
            case RECT:
                initRectAngle();
                setShapeRotation(-90);
                break;
            case TRIANGLE:
                initTriangle();
                setShapeRotation(-90);
                break;
            case PENTAGON:
                initPentagon();
                setShapeRotation(-90);
                break;
            case HEXAGON:
                initHexagon();
                setShapeRotation(-90);
                break;
        }
    }


    private void initRectAngle() {
        points = new int[8];
        initRotation = 45;
        rotationStep = 90;
        angleOffSet = 0;
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
