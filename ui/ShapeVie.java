package tbs.jumpsnew.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import tbs.jumpsnew.objects.Player;


/**
 * Created by root on 3/01/15.
 */
public class ShapeVie extends View {
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public ShapeVie(Context context, Player.PlayerShape playerShape) {
        super(context);

    }

    public ShapeVie(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(0xffffffff);

        canvas.drawRect(0, 0, 50, 50, paint);

        paint.setStrokeWidth(50);
        canvas.drawLine(0, 120, 50, 120, paint);

    }


}
