package com.example.runner.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.example.runner.main.GameSurface;

public abstract class GameObject {
    private Bitmap image;
    float width, height;
    float x, y;

    GameObject(Bitmap image, int width, int height, float x, float y) {
        this.image = image;
        this.width = width * GameSurface.SCALE_X;
        this.height = height * GameSurface.SCALE_Y;
        this.x = x;
        this.y = y;
    }

    public void update() {

    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public RectF getBounds() {
        return new RectF(getLeft(), getTop(), getRight(), getBottom());
    }

    public float getLeft() {
        return x;
    }

    public float getTop() {
        return y;
    }

    public float getRight() {
        return x + width;
    }

    public float getBottom() {
        return y + height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
