package com.example.runner.main;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.example.runner.objects.GameObject;
import com.example.runner.objects.Ground;
import com.example.runner.objects.Tile;

import java.util.ArrayList;

public class Map {
    public static final int WIDTH = 384, HEIGHT = 216;
    private static final int MAX_TILES = 2;

    private final Bitmap[] LAYERS = Resources.BACKGROUNDS;
    private float[] shifts = new float[LAYERS.length];
    private float[] speeds = new float[LAYERS.length];

    private float mainSpeed;
    private ArrayList<Tile> tiles = new ArrayList<>();
    private ArrayList<Ground> grounds = new ArrayList<>();
    private ArrayList<Tile> toRemove = new ArrayList<>();

    Map() {
        for (int i = 0; i < LAYERS.length; i++) {
            speeds[i] = (i + 1) * MainActivity.WIDTH / 1000f;
        }

        for (int i = 0; i < Ground.MAX_TILES + 1; i++) {
            grounds.add(new Ground(i * Ground.WIDTH * GameSurface.SCALE_X));
        }
    }

    void update() {
        for (int i = 0; i < LAYERS.length; i++) {
            shifts[i] += speeds[i];
            shifts[i] %= MainActivity.WIDTH;
        }
        mainSpeed = speeds[LAYERS.length - 1];

        updateTiles();
        updateGround();
    }

    private void updateTiles() {
        for (Tile tile : tiles) {
            tile.shift(mainSpeed);
            tile.update();
            if (tile.isOffScreen()) {
                toRemove.add(tile);
            }
        }

        tiles.removeAll(toRemove);
        toRemove.clear();

        for (int i = 0; i < MAX_TILES - tiles.size(); i++) {
            float x = MainActivity.WIDTH + MainActivity.WIDTH / (MAX_TILES + 1) * (i + 1);
            float y = Ground.Y - 50 * GameSurface.SCALE_Y * (i + 1);
            int id = (int) (Math.random() * 3);
            tiles.add(new Tile(2, x, y));
        }
    }

    private void updateGround() {
        float margin = grounds.get(0).getBounds().right - mainSpeed;
        Ground toRemove = (margin <= 0) ? grounds.get(0) : null;

        for (Ground ground : grounds) {
            ground.shift(mainSpeed);
            ground.update();
        }

        if (toRemove != null) {
            grounds.remove(toRemove);
            grounds.add(new Ground(MainActivity.WIDTH + margin));
        }
    }

    void draw(Canvas canvas) {
        for (int i = 0; i < LAYERS.length; i++) {
            Bitmap layer = LAYERS[i];
            canvas.drawBitmap(layer, -shifts[i], 0, null);
            canvas.drawBitmap(layer, layer.getWidth() - shifts[i], 0, null);
        }

        for (Tile tile : tiles) {
            tile.draw(canvas);
        }

        for (Ground ground : grounds) {
            ground.draw(canvas);
        }
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public ArrayList<Ground> getGrounds() {
        return grounds;
    }

    public float getSpeed() {
        return mainSpeed;
    }
}
