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
    public static Bitmap[] BACKGROUNDS, TILES, ITEMS;
    public static Bitmap[] ICONS;

    private static Typeface FONT;
    private Context context;

    static Paint FILL_PAINT, STROKE_PAINT,
            FILL_CENTER_PAINT, STROKE_CENTER_PAINT;

    Resources(Context context) {
        this.context = context;
        load();
    }

    private void load() {
        loadImages();;
        loadFont();
        loadPaints();
    }

    private void loadImages() {
        PLAYER_IDLE = loadSheet(R.drawable.player_idle, 21, 35);
        PLAYER_RUN = loadSheet(R.drawable.player_run, 23, 34);
        PLAYER_JUMP = loadSheet(R.drawable.player_jump, 22, 37);
        PLAYER_GRAB = loadSheet(R.drawable.player_grab, 22, 42);

        BACKGROUNDS = loadSheet(R.drawable.background, Map.WIDTH, Map.HEIGHT);
        TILES = loadSheet(R.drawable.tiles, 96, 32);
        ICONS = loadSheet(R.drawable.icons, Button.WIDTH, Button.HEIGHT);
        ITEMS = loadSheet(R.drawable.items, 14, 15);
    }

    private void loadFont() {
        FONT = Typeface.createFromAsset(context.getAssets(), "font/font.ttf");
    }

    private void loadPaints() {
        Paint BASE_PAINT = new Paint();
        BASE_PAINT.setTypeface(FONT);
        BASE_PAINT.setTextSize(10 * GameView.SCALE_X);

        FILL_PAINT = new Paint(BASE_PAINT);
        FILL_PAINT.setColor(Color.WHITE);

        FILL_CENTER_PAINT = new Paint(FILL_PAINT);
        FILL_CENTER_PAINT.setTextAlign(Paint.Align.CENTER);

        STROKE_PAINT = new Paint(BASE_PAINT);
        STROKE_PAINT.setColor(Color.BLACK);
        STROKE_PAINT.setStyle(Paint.Style.STROKE);

        STROKE_CENTER_PAINT = new Paint(STROKE_PAINT);
        STROKE_CENTER_PAINT.setTextAlign(Paint.Align.CENTER);
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
