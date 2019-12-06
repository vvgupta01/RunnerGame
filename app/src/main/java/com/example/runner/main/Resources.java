package com.example.runner.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.example.runner.R;
import com.example.runner.objects.Ground;
import com.example.runner.objects.Tile;

public class Resources {
    public static Bitmap[] PLAYER_IDLE, PLAYER_RUN, PLAYER_JUMP, PLAYER_GRAB;
    public static Bitmap[] BACKGROUNDS, TILES;
    public static Bitmap[] GROUNDS;
    static Bitmap[] ICONS;

    private static Paint BASE_PAINT;
    private Context context;

    Resources(Context context) {
        this.context = context;
        load();
    }

    private void load() {
        PLAYER_IDLE = loadSheet(R.drawable.player_idle, 21, 35, true);
        PLAYER_RUN = loadSheet(R.drawable.player_run, 23, 34, true);
        PLAYER_JUMP = loadSheet(R.drawable.player_jump, 22, 37, true);
        PLAYER_GRAB = loadSheet(R.drawable.player_grab, 22, 42, true);

        BACKGROUNDS = loadSheet(R.drawable.background, Map.WIDTH, Map.HEIGHT, true);
        TILES = loadTiles();
        GROUNDS = loadSheet(R.drawable.ground, Ground.WIDTH, Ground.HEIGHT, true);
        ICONS = loadSheet(R.drawable.icons, 56, 64, false);

        BASE_PAINT = loadPaint();
    }

    static Paint getPaint(int size, boolean center) {
        Paint paint = new Paint(BASE_PAINT);
        paint.setTextSize(size);
        if (center) {
            paint.setTextAlign(Paint.Align.CENTER);
        }
        return paint;
    }

    private static Paint loadPaint() {
        Paint paint = new Paint();
        paint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        paint.setColor(Color.WHITE);
        return paint;
    }

    private Bitmap[] loadSheet(int res, int width, int height, boolean scale) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;

        Bitmap sheet = BitmapFactory.decodeResource(context.getResources(), res, opts);
        int images = sheet.getWidth() / width;
        Bitmap[] bitmaps = new Bitmap[images];

        Matrix matrix = scale ? GameSurface.SCALE_MATRIX : null;
        for (int i = 0; i < images; i++) {
            bitmaps[i] = Bitmap.createBitmap(sheet, i * width, 0, width, height,
                    matrix, false);
        }
        return bitmaps;
    }

    private Bitmap[] loadTiles() {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;

        Bitmap sheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.tiles, opts);
        Matrix matrix = GameSurface.SCALE_MATRIX;
        Bitmap[] bitmaps = new Bitmap[3];
        for (int i = 0; i < 3; i++) {
            bitmaps[i] = Bitmap.createBitmap(sheet, i * (i + 1) * 16, 0, (i + 1) * 32,
                    Tile.HEIGHT, matrix, false);
        }
        return bitmaps;
    }
}
