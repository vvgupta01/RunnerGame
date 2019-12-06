package com.example.runner.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.example.runner.main.Animation;
import com.example.runner.main.GameSurface;
import com.example.runner.main.Resources;


public class Player extends GameObject {
    public static final int WIDTH = 22, HEIGHT = 34;
    public static final int IDLE_STATE = 0, RUN_STATE = 1, JUMP_STATE = 2, GRAB_STATE = 3;
    static final float CENTER_X = 181 * GameSurface.SCALE_X,
        CENTER_Y = 152 * GameSurface.SCALE_Y;


    private PlayerEngine ENGINE;
    private final Bitmap[] IDLE_FRAMES, RUN_FRAMES, JUMP_FRAMES, GRAB_FRAMES;
    private final Animation IDLE, RUN, GRAB;

    private int state;
    private int jumpFrame;

    public Player() {
        super(null, WIDTH, HEIGHT, CENTER_X, CENTER_Y);

        IDLE_FRAMES = Resources.PLAYER_IDLE;
        RUN_FRAMES = Resources.PLAYER_RUN;
        JUMP_FRAMES = Resources.PLAYER_JUMP;
        GRAB_FRAMES = Resources.PLAYER_GRAB;

        RUN = new Animation(0, RUN_FRAMES.length);
        IDLE = new Animation(0, IDLE_FRAMES.length);
        GRAB = new Animation(0, GRAB_FRAMES.length);

        ENGINE = new PlayerEngine(this);
    }

    @Override
    public void update() {
        x += ENGINE.getShift();
        if (state == IDLE_STATE) {
            IDLE.update();
        } else if (state == RUN_STATE) {
            ENGINE.checkRun();
            RUN.update();
        } else if (state == JUMP_STATE) {
            ENGINE.updateJump();
            if (ENGINE.isLanded() && state != GRAB_STATE) {
                state = RUN_STATE;
            }
            jumpFrame = ENGINE.getFrame();
        } else if (state == GRAB_STATE) {
            ENGINE.checkGrab();
            if (GRAB.isActive()) {
                GRAB.update();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (state == IDLE_STATE) {
            canvas.drawBitmap(IDLE_FRAMES[IDLE.getFrame()], x, y, null);
        } else if (state == RUN_STATE) {
            canvas.drawBitmap(RUN_FRAMES[RUN.getFrame()], x, y, null);
        } else if (state == JUMP_STATE) {
            canvas.drawBitmap(JUMP_FRAMES[jumpFrame], x, y, null);
        } else if (state == GRAB_STATE) {
            canvas.drawBitmap(GRAB_FRAMES[GRAB.getFrame()], x, y, null);
        }
    }

    public void run() {
        state = RUN_STATE;
    }

    public void jump() {
        if (state == GRAB_STATE) {
            ENGINE.ledge_jump();
        } else {
            ENGINE.jump();
        }
        state = JUMP_STATE;
    }

    void drop(boolean verticalDrop) {
        state = JUMP_STATE;
        ENGINE.drop(verticalDrop);
    }

    void grab() {
        state = GRAB_STATE;
        GRAB.start();
    }

    public int getState() {
        return state;
    }
}