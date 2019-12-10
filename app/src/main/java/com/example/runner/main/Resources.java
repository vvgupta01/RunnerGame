package com.example.runner.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.example.runner.R;
import com.example.runner.objects.Button;

public class Resources {
    public static Bitmap[] PLAYER_IDLE, PLAYER_RUN, PLAYER_JUMP, PLAYER_GRAB;
    public static Bitmap[] BACKGROUNDS, TILES;
    public static Bitmap[] ICONS;

    private static Typeface FONT;
    private static Paint BASE_PAINT;
    private Context context;

    Resources(Context context) {
        this.context = context;
        load();
    }

    private void load() {
        PLAYER_IDLE = loadSheet(R.drawable.player_idle, 21, 35);
        PLAYER_RUN = loadSheet(R.drawable.player_run, 23, 34);
        PLAYER_JUMP = loadSheet(R.drawable.player_jump, 22, 37);
        PLAYER_GRAB = loadSheet(R.drawable.player_grab, 22, 42);

        BACKGROUNDS = loadSheet(R.drawable.background, Map.WIDTH, Map.HEIGHT);
        TILES = loadSheet(R.drawable.tiles, 96, 32);
        ICONS = loadSheet(R.drawable.icons, Button.WIDTH, Button.HEIGHT);

        FONT = loadFont();
        BASE_PAINT = loadPaint();
    }

    public static Paint getPaint(int size, boolean center) {
        Paint paint = new Paint(BASE_PAINT);
        paint.setTextSize(size * GameView.SCALE_X);
        if (center) {
            paint.setTextAlign(Paint.Align.CENTER);
        }
        return paint;
    }

    private static Paint loadPaint() {
        Paint paint = new Paint();
        paint.setTypeface(FONT);
        paint.setColor(Color.WHITE);
        return paint;
    }

    private Typeface loadFont() {
        return Typeface.createFromAsset(context.getAssets(), "font/font.ttf");
    }

    private Bitmap[] loadSheet(int res, int width, int height) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;

        Bitmap sheet = BitmapFactory.decodeResource(context.getResources(), res, opts);
        int images = sheet.getWidth() / width;
        Bitmap[] bitmaps = new Bitmap[images];

        for (int i = 0; i < images; i++) {
            bitmaps[i] = Bitmap.createBitmap(sheet, i * width, 0, width, height,
                    GameView.SCALE_MATRIX, false);
        }
        return bitmaps;
    }
}
