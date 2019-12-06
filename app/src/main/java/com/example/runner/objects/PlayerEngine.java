package com.example.runner.objects;


import android.util.Log;

import com.example.runner.main.GameSurface;
import com.example.runner.main.GameThread;

import java.util.ArrayList;

class PlayerEngine {
    private static final int LEFT_COLLISION = 1, BOT_COLLISION = 2, TOP_COLLISION = 3;
    private static final double VEL_Y0 = 500, GRAVITY = 500;
    private static final double ERROR = VEL_Y0 / GameThread.FPS;

    private Player player;
    private ArrayList<Tile> tiles;
    private ArrayList<Ground> grounds;

    private double velY;
    private float shift;
    private boolean landed, ledgeJump, verticalDrop;

    PlayerEngine(Player player) {
        this.player = player;
        tiles = GameSurface.getMap().getTiles();
        grounds = GameSurface.getMap().getGrounds();
    }

    void checkRun() {
        if (player.x < Player.CENTER_X) {
            shift = GameSurface.getMap().getSpeed() / 5;
        } else {
            shift = 0;
        }

        if (player.getBounds().bottom >= Ground.Y) {
            for (Ground ground : grounds) {
                if (groundCollision(ground)) {
                    return;
                }
            }
        } else {
            for (Tile tile : tiles) {
                if (tileCollision(tile) == BOT_COLLISION) {
                    return;
                }
            }
        }
        player.drop(false);
    }

    void checkGrab() {
        if (player.getState() == Player.GRAB_STATE) {
            shift = -GameSurface.getMap().getSpeed();
        }
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
        player.y -= velY / GameThread.FPS;
        velY -= GRAVITY / GameThread.FPS;

        if (verticalDrop || (ledgeJump && velY > 0)) {
            shift = -GameSurface.getMap().getSpeed();
        } else {
            shift = 0;
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

        for (Ground ground : grounds) {
            if (groundCollision(ground)) {
                land();
                break;
            }
        }

        if (!ledgeJump) {
            for (Tile tile : tiles) {
                float diffX = player.getRight() - tile.getLeft();
                float diffY = player.getTop() - tile.getTop();
                if (diffX >= 0 && diffX <= ERROR
                        && diffY >= -7 * GameSurface.SCALE_Y && diffY <= tile.height) {
                    land();
                    player.grab();
                }
            }
        }
    }

    private void land() {
        landed = true;
        ledgeJump = false;
        verticalDrop = false;
        velY = 0;
        shift = 0;
    }

    private int tileCollision(Tile tile) {
        if (player.getRight() >= tile.getLeft() && player.getLeft() <= tile.getRight()
                && player.getTop() <= tile.getBottom() && player.getBottom() >= tile.getTop()) {
            if (leftCollision(tile)) {
                return LEFT_COLLISION;
            } else if (bottomCollision(tile)) {
                return BOT_COLLISION;
            } else if (topCollision(tile)) {
                return TOP_COLLISION;
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

    private boolean groundCollision(Ground ground) {
        float playerBottom = player.getBottom();
        float groundTop = ground.getTop();
        if (playerBottom >= groundTop) {
            player.y -= (playerBottom - groundTop);
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

    float getShift() {
        return shift;
    }

    boolean isLanded() {
        return landed;
    }
}
