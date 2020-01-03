package com.example.runner.objects;

import com.example.runner.main.GameState;
import com.example.runner.main.GameView;

import java.util.ArrayList;

class PlayerEngine {
    static final int BOT_COLLISION = 0, TOP_COLLISION = 1,
            LEFT_COLLISION = 2, GRAB_COLLISION = 3;
    private ArrayList<Tile> tiles;
    private Tile grabTile;
    private Player player;

    PlayerEngine(Player player, GameState game) {
        this.player = player;
        tiles = game.getMap().getTiles();
    }

    int tileCollision() {
        for (Tile tile : tiles) {
            if ((!tile.isFalling()) && player.getLeft() <= tile.getRight()
                    && player.getRightBounds() >= tile.getLeft()
                    && player.getTopBounds() <= tile.getBottom()
                    && player.getBottomBounds() >= tile.getTop()) {
                if (bottomCollision(tile)) {
                    return BOT_COLLISION;
                } else if (topCollision(tile)) {
                    return TOP_COLLISION;
                } else if (leftCollision(tile)) {
                    return LEFT_COLLISION;
                } else if (grabCollision(tile)) {
                    grabTile = tile;
                    return GRAB_COLLISION;
                }
            }
        }
        return -1;
    }

    boolean isGrabbing() {
        return (!grabTile.isFalling() && leftCollision(grabTile));
    }

    private boolean leftCollision(Tile tile) {
        if (!player.isMovingY() && player.getRight() <= tile.getLeft()) {
            player.x = tile.getLeft() - player.width;
            return true;
        }
        return false;
    }

    private boolean bottomCollision(Tile tile) {
        if (player.getVelY() <= 0 && player.getBottom() <= tile.getTop()) {
            player.y = tile.getTop() - player.height;
            return true;
        }
        return false;
    }

    private boolean topCollision(Tile tile) {
        if (player.getVelY() >= 0 && player.getTop() >= tile.getBottom()) {
            player.y = tile.getBottom();
            return true;
        }
        return false;
    }

    private boolean grabCollision(Tile tile) {
        if (player.isVertical() && grabTile == null) {
            float margin = -7 * GameView.SCALE_Y;
            float dy = player.getTop() - tile.getTop();
            return (dy >= margin && dy <= Tile.HEIGHT / 2f * GameView.SCALE_Y + margin);
        }
        return false;
    }

    void reset() {
        grabTile = null;
    }
}
