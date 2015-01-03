package tbs.jumpsnew.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by root on 3/01/15.
 */
public class ColorView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int r, cx, cy, color;

    public ColorView(Context context, int color) {
        super(context);
        this.color = color;
    }

    public ColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(0xffffffff);
        canvas.drawCircle(cx, cy, r, paint);

        paint.setColor(color);
        canvas.drawCircle(cx, cy, Math.round(r * 0.95f), paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cx = w / 2;
        cy = h / 2;
        r = Math.min(cx, cy);
        invalidate();
    }
}
