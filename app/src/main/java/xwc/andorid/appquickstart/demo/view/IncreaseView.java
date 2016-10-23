package xwc.andorid.appquickstart.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import xwc.andorid.appquickstart.demo.R;

/**
 *
 */
public class IncreaseView extends View {

    private int max = 100;
    private int progress = 60;
    private Paint paint;
    public  int MAX_LEVEL = 40;
    private float vgap = 2;
    private float sw = 1;
    private boolean land = true;

    public IncreaseView(Context context) {
        super(context);
        init(null, 0);
    }

    public IncreaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public IncreaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (land){
            float w = right - left;
            sw = w / MAX_LEVEL - vgap;
            paint.setStrokeWidth( sw );
            sw /= 2;
        }else{
            float h = bottom - top;
            sw = h / MAX_LEVEL - vgap;
            paint.setStrokeWidth( sw );
            sw /= 2;
        }
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int lines = progress * MAX_LEVEL / max;
        if (lines < 1) lines = 1;
        float[] point = new float[lines * 4];
        float w = getWidth(), h = getHeight();
        if (land){
            float vw = w / (float) MAX_LEVEL;
            for (int i = 0; i < lines; i++){
                point[4 * i] = vw * i + sw;
                point[4 * i + 1] = h - sw;
                point[4 * i + 2] = point[4 * i];
                point[4 * i + 3] = sw;
            }
        }else{
            float vh = h / (float) MAX_LEVEL;
            for (int i = 0; i < lines; i++){
                point[4 * i] = sw;
                point[4 * i + 1] = h - vh * i + sw;
                point[4 * i + 2] = w - sw;
                point[4 * i + 3] = point[4 * i + 1];
            }
        }
        canvas.drawLines(point, paint);
    }

}
