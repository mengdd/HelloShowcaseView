package com.ddmeng.helloshowcaseview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

public class ShowcaseDrawer {
    private final Paint eraserPaint;
    private final Paint basicPaint;
    protected int backgroundColour;

    public ShowcaseDrawer() {
        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY);
        eraserPaint = new Paint();
        eraserPaint.setColor(0xFF0000);
        eraserPaint.setAlpha(0);
        eraserPaint.setXfermode(xfermode);
        eraserPaint.setAntiAlias(true);
        basicPaint = new Paint();

    }

    public void setBackgroundColour(int backgroundColor) {
        this.backgroundColour = backgroundColor;
    }

    public void erase(Bitmap bitmapBuffer) {
        bitmapBuffer.eraseColor(backgroundColour);
    }

    public void drawShowcase(Bitmap buffer, float x, float y, int width, int height) {
        Log.i("ddmeng", "drawShowcase");
        Canvas bufferCanvas = new Canvas(buffer);
        bufferCanvas.drawRect(x - width / 2, y - height / 2, x + width / 2, y + height / 2, eraserPaint);
    }

    public void drawToCanvas(Canvas canvas, Bitmap bitmapBuffer) {
        Log.i("ddmeng", "drawToCanvas");
        canvas.drawBitmap(bitmapBuffer, 0, 0, basicPaint);
    }
}
