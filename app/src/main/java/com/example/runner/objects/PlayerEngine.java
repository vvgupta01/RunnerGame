package com.example.runner.objects;

import android.util.Log;

import com.example.runner.main.GameView;
import com.example.runner.main.GameThread;
import com.example.runner.main.GameState;

import java.util.ArrayList;

class PlayerEngine {
    private static final int LEFT_COLLISION = 1, BOT_COLLISION = 2, TOP_COLLISION = 3;
    private static final double VEL_Y0 = 600, GRAVITY = 1200;
    private static final double ERROR = VEL_Y0 / GameThread.FPS;

    private Player player;
    private ArrayList<Tile> tiles;

    private double velY;
    private boolean landed, ledgeJump, verticalDrop;

    PlayerEngine(Player player) {
        this.player = player;
        tiles = GameState.getMap().getAllTiles();
    }

    void update() {
        tiles = GameState.getMap().getAllTiles();
        if (player.getState() == Player.GRAB_STATE
                || verticalDrop || (ledgeJump && velY > 0)) {
            player.x -= GameState.getMap().getSpeed();
        } else if (player.getState() == Player.RUN_STATE
                && player.x < Player.CENTER_X * GameView.SCALE_X) {
            player.x += GameState.getMap().getSpeed() / 3f;
        }
    }

    void checkRun() {
        for (Tile tile : tiles) {
            if (tileCollision(tile) == BOT_COLLISION) {
                return;
            }
        }
        player.drop(false);
    }

    void jump() {
        velY = VEL_Y0;
        landed = false;
    }

    void drop(boolean verticalDrop) {
        velY = 0;
        landed = false;
        this.verticalDrop = verticalDrop;
    }

    void ledge_jump() {
        jump();
        ledgeJump = true;
    }

    void updateJump() {
        player.y -= velY / GameThread.FPS * GameThread.DELTA;
        if (velY > -VEL_Y0) {
            velY -= GRAVITY / GameThread.FPS * GameThread.DELTA;
        }

        for (Tile tile : tiles) {
            int collisionType = tileCollision(tile);
            if (collisionType == LEFT_COLLISION) {
                player.drop(true);
            } else if (collisionType == BOT_COLLISION) {
                land();
            } else if (collisionType == TOP_COLLISION) {
                velY *= -1;
            }

            if (collisionType != -1) {
                break;
            }
        }

        if (!ledgeJump) {
            for (Tile tile : tiles) {
                float diffX = player.getRight() - tile.getLeft();
                float diffY = player.getTop() - tile.getTop();
                float lowerY = -7 * GameView.SCALE_Y;
                if (diffX >= 0 && diffX <= ERROR && diffY >= lowerY) {
                    if ((!tile.isGround() && diffY <= tile.height / 2 + lowerY)
                            || (tile.isGround() && diffY <= 0)) {
                        land();
                        player.grab();
                    }
                }
            }
        }
    }

    private void land() {
        landed = true;
        ledgeJump = false;
        verticalDrop = false;
        velY = 0;
    }

    private int tileCollision(Tile tile) {
        if (player.getRight() >= tile.getLeft() && player.getLeft() <= tile.getRight()
                && player.getTop() <= tile.getBottom() && player.getBottom() >= tile.getTop()) {
            if (bottomCollision(tile)) {
                return BOT_COLLISION;
            } else if (topCollision(tile)) {
                return TOP_COLLISION;
            } else if (leftCollision(tile)) {
                return LEFT_COLLISION;
            }
        }
        return -1;
    }

    private boolean leftCollision(Tile tile) {
        if (verticalDrop || ledgeJump) {
            return false;
        }

        float playerRight = player.getRight();
        float tileLeft = tile.getLeft();
        if (playerRight - tileLeft <= ERROR) {
            player.x -= (playerRight - tileLeft);
            return true;
        }
        return false;
    }

    private boolean bottomCollision(Tile tile) {
        if (ledgeJump && velY > 0) {
            return false;
        }

        float playerBottom = player.getBottom();
        float tileTop = tile.getTop();
        if (playerBottom - tileTop <= ERROR) {
            player.y -= (playerBottom - tileTop);
            return true;
        }
        return false;
    }

    private boolean topCollision(Tile tile) {
        float playerTop = player.getTop();
        float tileBottom = tile.getBottom();
        if (tileBottom - playerTop <= ERROR) {
            player.y += (tileBottom - playerTop);
            return true;
        }
        return false;
    }

    int getFrame() {
        if (velY >= 0) {
            return 0;
        }
        return 1;
    }

    boolean canJump() {
        return (!verticalDrop &&  ((player.getJumps() < 2 && velY < 0)
                || player.getState() != Player.JUMP_STATE));
    }

    boolean isLanded() {
        return landed;
    }
}
