package com.example.runner.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.example.runner.main.Animation;
import com.example.runner.main.GameState;
import com.example.runner.main.Map;
import com.example.runner.main.Resources;

public class Player extends GameObject {
    public static final int WIDTH = 22, HEIGHT = 34;
    public static final int CENTER_X = (Map.WIDTH - WIDTH) / 2,
            CENTER_Y = Tile.GROUND_Y - HEIGHT + 2;

    public static final int IDLE_STATE = 0, RUN_STATE = 1, JUMP_STATE = 2, GRAB_STATE = 3;
    private static final float INITIAL_VELY = 20;

    private final GameState GAME;
    private final PlayerEngine ENGINE;

    private final Bitmap[] IDLE_FRAMES, RUN_FRAMES, JUMP_FRAMES, GRAB_FRAMES;
    private final Animation IDLE, RUN, GRAB;

    private int state;
    private float velX, velY;
    private boolean landed, verticalDrop, ledgeJump;
    private int jumps, jumpFrame;

    public Player(GameState game) {
        super(null, WIDTH, HEIGHT, CENTER_X, CENTER_Y);

        IDLE_FRAMES = Resources.PLAYER_IDLE;
        RUN_FRAMES = Resources.PLAYER_RUN;
        JUMP_FRAMES = Resources.PLAYER_JUMP;
        GRAB_FRAMES = Resources.PLAYER_GRAB;

        GAME = game;
        ENGINE = new PlayerEngine(this, game);

        IDLE = new Animation(0, IDLE_FRAMES.length);
        RUN = new Animation(0, RUN_FRAMES.length);
        GRAB = new Animation(0, GRAB_FRAMES.length);
    }

    @Override
    public void update() {
        if (state == IDLE_STATE) {
            IDLE.update();
        } else if (state == RUN_STATE) {
            if (ENGINE.tileCollision() == -1) {
                drop(false);
            }
            RUN.update();
        } else if (state == JUMP_STATE) {
            updateJump();
            if (landed && state != GRAB_STATE) {
                run();
            }
            jumpFrame = (velY >= 0) ? 0 : 1;
        } else if (state == GRAB_STATE) {
            if (!ENGINE.isGrabbing()) {
                drop(false);
            }
            if (GRAB.isActive()) {
                GRAB.update();
            }
        }

        velX = (state == GRAB_STATE || verticalDrop || (ledgeJump && velY > 0))
                ? 0 : GAME.getMap().getSpeed();
    }

    @Override
    public void draw(Canvas canvas) {
        switch(state) {
            case IDLE_STATE:
                canvas.drawBitmap(IDLE_FRAMES[IDLE.getFrame()], x, y, null);
                break;
            case RUN_STATE:
                canvas.drawBitmap(RUN_FRAMES[RUN.getFrame()], x, y, null);
                break;
            case JUMP_STATE:
                canvas.drawBitmap(JUMP_FRAMES[jumpFrame], x, y, null);
                break;
            case GRAB_STATE:
                canvas.drawBitmap(GRAB_FRAMES[GRAB.getFrame()], x, y, null);
        }
    }

    private void updateJump() {
        switch(ENGINE.tileCollision()) {
            case PlayerEngine.BOT_COLLISION:
                land();
                break;
            case PlayerEngine.TOP_COLLISION:
                velY = 0;
                break;
            case PlayerEngine.LEFT_COLLISION:
                drop(true);
                break;
            case PlayerEngine.GRAB_COLLISION:
                grab();
        }

        if (!landed) {
            y -= velY;
            velY -= Map.GRAVITY;
        }
    }

    public void run() {
        state = RUN_STATE;
        ENGINE.reset();
    }

    public void jump() {
        if ((jumps < 2 && velY < 0) || state != JUMP_STATE) {
            ledgeJump = (state == GRAB_STATE);
            velY = INITIAL_VELY;
            landed = false;
            jumps++;
            state = JUMP_STATE;
        }
    }

    public void drop(boolean vertical) {
        state = JUMP_STATE;
        velY = 0;
        landed = false;
        verticalDrop = vertical;
        jumps++;
    }

    private void grab() {
        land();
        state = GRAB_STATE;
        GRAB.start();
    }

    private void land() {
        landed = true;
        ledgeJump = false;
        verticalDrop = false;
        jumps = 0;
        velY = 0;
    }

    float getRightBounds() {
        return super.getRight() + velX;
    }

    float getTopBounds() {
        return y - velY;
    }

    float getBottomBounds() {
        return super.getBottom() - velY;
    }

    public float getVelX() {
        return velX;
    }

    float getVelY() {
        return velY;
    }

    boolean isVertical() {
        return verticalDrop;
    }

    boolean isMovingY() {
        return (verticalDrop || ledgeJump);
    }

    public int getState() {
        return state;
    }
}
