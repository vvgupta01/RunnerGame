package com.example.runner.states;

import android.graphics.Canvas;

public class Manager {
    public static final int GAME_STATE = 0, END_STATE = 1;
    private static State[] STATES;
    private static PauseState pauseState;

    private static int state;
    private static boolean paused;

    public Manager() {
        STATES = new State[2];
        pauseState = new PauseState();
        setState(GAME_STATE);
    }

    static void setState(int state) {
        Manager.state = state;
        if (state == GAME_STATE) {
            STATES[state] = new GameState();
        } else if (state == END_STATE) {
            STATES[state] = new EndState();
        }
    }

    public void update() {
        if (!paused) {
            STATES[state].update();
        } else {
            pauseState.update();
        }
    }

    public void draw(Canvas canvas) {
        STATES[GAME_STATE].draw(canvas);
        if (state == END_STATE) {
            STATES[END_STATE].draw(canvas);
        } else if (paused) {
            pauseState.draw(canvas);
        }
    }

    public void input(float tx, float ty) {
        if (!paused) {
            STATES[state].input(tx, ty);
        } else {
            pauseState.input(tx, ty);
        }
    }

    public static int getState() { return state; }

    public static void pause() {
        paused = !paused;
    }

    public static boolean isPaused() {
        return paused;
    }
}
