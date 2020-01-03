package com.example.runner.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.example.runner.main.GameView;

public abstract class GameObject {
    private Bitmap image;
    float width, height;
    float x, y;

    GameObject(Bitmap image, int width, int height, float x, float y) {
        this.image = image;
        this.width = width * GameView.SCALE_X;
        this.height = height * GameView.SCALE_Y;
        this.x = x * GameView.SCALE_X;
        this.y = y * GameView.SCALE_Y;
    }

    public void update() {

    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public float getLeft() {
        return x;
    }

    public float getRight() {
        return x + width;
    }

    public float getTop() {
        return y;
    }

    public float getBottom() {
        return y + height;
    }
}
