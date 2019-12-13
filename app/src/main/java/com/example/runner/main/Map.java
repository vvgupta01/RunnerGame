package com.example.runner.main;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.runner.objects.Player;
import com.example.runner.objects.Tile;

import java.util.ArrayList;

public class Map {
    public static final int WIDTH = 384, HEIGHT = 216;

    private final Bitmap[] LAYERS = Resources.BACKGROUNDS;
    private float[] shifts = new float[LAYERS.length];
    private float[] speeds = new float[LAYERS.length];

    private float mainShift, mainSpeed;
    private ArrayList<Tile> tiles = new ArrayList<>();
    private ArrayList<Tile> grounds = new ArrayList<>();
    private ArrayList<Tile> toRemove = new ArrayList<>();

    Map() {
        for (int i = 0; i < LAYERS.length; i++) {
            speeds[i] = (i + 1) * GameView.WIDTH / 500f;
        }

        for (int i = 0; i < Tile.MAX_GROUND + 1; i++) {
            grounds.add(new Tile(i * Tile.GROUND_WIDTH));
        }
    }

    void update() {
        for (int i = 0; i < LAYERS.length; i++) {
            shifts[i] += speeds[i] * GameThread.DELTA;
            shifts[i] %= GameView.WIDTH;
        }
        mainShift = shifts[LAYERS.length - 1];
        mainSpeed = speeds[LAYERS.length - 1] * GameThread.DELTA;

        updateTiles();
        updateGround();
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

        for (Tile ground : grounds) {
            ground.draw(canvas);
        }
    }

    private void updateTiles() {
        for (Tile tile : tiles) {
            tile.shift(mainSpeed);
            tile.update();
            if (tile.getRight() <= 0) {
                toRemove.add(tile);
            }
        }

        tiles.removeAll(toRemove);
        toRemove.clear();

        if (mainShift < mainSpeed) {
            generateTiles();
        }
    }

    private void generateTiles() {
        for (int i = 0; i < Tile.MAX_TILES; i++) {
            float anchorX = Map.WIDTH + i * Map.WIDTH / Tile.MAX_TILES;
            int marginX = (int) (Math.random() * Map.WIDTH / (2 * Tile.MAX_TILES));
            float x = anchorX + marginX;

            float y = Tile.GROUND_Y - ((int) (Math.random() * 2) + 1)
                    * (Player.HEIGHT + Tile.HEIGHT);
            int id = (int) (Math.random() * 3);
            tiles.add(new Tile(id, x, y));
        }
    }

    private void updateGround() {
        float margin = (grounds.get(0).getRight() - mainSpeed) / GameView.SCALE_X;
        int toRemove = (margin <= 0) ? 0 : -1;

        for (Tile ground : grounds) {
            ground.shift(mainSpeed);
            ground.update();
        }

        if (toRemove != -1) {
            grounds.remove(0);

            int chance = (int) (Math.random() * 11);
            if (chance <= 7 || grounds.size() == Tile.MAX_GROUND - 2) {
                grounds.add(new Tile(Map.WIDTH + margin));
            }
        }
    }

    public ArrayList<Tile> getAllTiles() {
        ArrayList<Tile> allTiles = new ArrayList<>(tiles);
        allTiles.addAll(grounds);
        return allTiles;
    }

    public float getSpeed() {
        return mainSpeed;
    }
}
