package com.example.runner.main;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import androidx.annotation.Nullable;

import com.example.runner.objects.GameObject;
import com.example.runner.objects.Player;
import com.example.runner.objects.Berry;
import com.example.runner.objects.Tile;

import java.util.ArrayList;

public class Map {
    public static final int WIDTH = 384, HEIGHT = 216;
    public static final int MAX_TILES = 2;
    public static int SHIFT_TIME = 5000, GRAVITY = 1;

    private final Bitmap[] LAYERS = Resources.BACKGROUNDS;
    private float mainSpeed, mainShift;

    private float[] shifts = new float[LAYERS.length];
    private float[] speeds = new float[LAYERS.length];

    private final GameState GAME;
    private ArrayList<Tile> tiles = new ArrayList<>();
    private ArrayList<Berry> berries = new ArrayList<>();
    private ArrayList<GameObject> toRemove = new ArrayList<>();

    Map(GameState game) {
        GAME = game;
        for (int i = 0; i < LAYERS.length; i++) {
            speeds[i] = i * 1000f * GameView.WIDTH / SHIFT_TIME / GameThread.FPS
                    / (LAYERS.length - 1);
        }

        for (int i = 0; i < Tile.MAX_GROUND; i++) {
            tiles.add(new Tile(i * Tile.GROUND_WIDTH));
        }

        mainSpeed = speeds[LAYERS.length - 1];
        mainShift = shifts[LAYERS.length - 1];
    }

    void update(Player player) {
        if (player.getVelX() > 0) {
            for (int i = 1; i < LAYERS.length; i++) {
                shifts[i] += speeds[i];
                shifts[i] %= GameView.WIDTH;
            }
            mainSpeed = speeds[LAYERS.length - 1];
            mainShift = shifts[LAYERS.length - 1];
        }
        updateTiles(player);
        updateBerries(player);
        toRemove.clear();
    }

    void draw(Canvas canvas) {
        for (int i = 0; i < LAYERS.length; i++) {
            canvas.drawBitmap(LAYERS[i], -shifts[i], 0, null);
            canvas.drawBitmap(LAYERS[i], LAYERS[i].getWidth() - shifts[i], 0, null);
        }

        for (Tile tile : tiles) {
            tile.draw(canvas);
        }

        for (Berry berry : berries) {
            berry.draw(canvas);
        }
    }

    private void updateTiles(Player player) {
        for (Tile tile : tiles) {
            if (player.getVelX() > 0) {
                tile.shift(mainSpeed);
            }

            tile.update();
            if (tile.getTop() >= GameView.HEIGHT) {
                toRemove.add(tile);
            }
        }
        tiles.removeAll(toRemove);

        float margin = getLastGround().getRight() - mainSpeed - GameView.WIDTH;
        if (margin <= 0) {
//            int chance = (int) (Math.random() * 11);
//            if (chance <= 7 || getGroundTiles() <= Tile.MAX_GROUND / 2) {
//                tiles.add(new Tile(Map.WIDTH + margin));
//            }
            tiles.add(new Tile(Map.WIDTH + margin));
        }

        if (mainShift < mainSpeed) {
            for (int i = 0; i < MAX_TILES; i++) {
                tiles.add(Tile.generateTile(i));
            }
        }
    }

    private void updateBerries(Player player) {
        for (Berry berry : berries) {
            if (player.getVelX() > 0) {
                berry.shift(mainSpeed);
            }

            berry.update();
            if (berry.getRight() <= 0) {
                toRemove.add(berry);
            } else if (collision(player, berry)) {
                toRemove.add(berry);
                GAME.collectBerry();
            }
        }

        if (berries.size() == 0) {
            berries.add(Berry.spawnBerry());
        }
        berries.removeAll(toRemove);
    }

    private boolean collision(Player player, Berry berry) {
        return (player.getRight() >= berry.getLeft() && player.getLeft() <= berry.getRight()
                && player.getTop() <= berry.getBottom() && player.getBottom() >= berry.getTop());
    }

    @Nullable
    private Tile getLastGround() {
        for (int i = tiles.size() - 1; i >= 0; i--) {
            if (tiles.get(i).isGround()) {
                return tiles.get(i);
            }
        }
        return null;
    }

    boolean isStable() {
        for (Tile tile : tiles) {
            if (tile.isFalling()) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public float getSpeed() {
        return mainSpeed;
    }
}
