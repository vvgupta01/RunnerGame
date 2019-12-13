package com.example.runner.main;

import android.graphics.Canvas;

import com.example.runner.objects.Button;
import com.example.runner.objects.Player;

public class GameState {
    public static final int GAME_STATE = 0, END_STATE = 1;
    private final Button STATE_BUTTON;
    private final Button HELP_BUTTON;

    private static Map map;
    private static Player player;

    private int state;
    private double time;

    GameState() {
        STATE_BUTTON = new Button(Map.WIDTH - Button.WIDTH - 10, 10);
        HELP_BUTTON = new Button(Map.WIDTH - 2 * (Button.WIDTH + 10), 10);
        reset();
    }

    void update() {
        if (player.getState() != Player.IDLE_STATE) {
            time += GameThread.TPF;
            map.update();
        }
        player.update();

        if (player.getRight() < -GameView.WIDTH / 10f
                || player.getTop() > 6 * GameView.HEIGHT / 5f) {
            state = END_STATE;
            STATE_BUTTON.setState(Button.RESET);
            MainActivity.save(time);
        }
    }

    void draw(Canvas canvas) {
        map.draw(canvas);
        player.draw(canvas);
        drawUI(canvas);
    }

    void input(float tx, float ty) {
        if (STATE_BUTTON.click(tx, ty)) {
            if (state == GAME_STATE
                    && player.getState() != Player.IDLE_STATE) {
                MainActivity.PAUSED = !MainActivity.PAUSED;
                if (MainActivity.PAUSED) {
                    STATE_BUTTON.setState(Button.PLAY);
                } else {
                    STATE_BUTTON.setState(Button.PAUSE);
                }
            } else if (state == END_STATE) {
                reset();
            }
        } else if (player.getState() == Player.IDLE_STATE) {
            player.run();
        } else if (!MainActivity.PAUSED) {
            player.jump();
        }
    }

    private void drawUI(Canvas canvas) {
        canvas.drawText(getTime(time), GameView.WIDTH / 2, 20 * GameView.SCALE_Y,
                Resources.getPaint(10, true));

        if (player.getState() == Player.IDLE_STATE) {
            canvas.drawText("TAP TO START", GameView.WIDTH / 2, GameView.HEIGHT / 2,
                    Resources.getPaint(10, true));
        }

        if (player.getState() == Player.IDLE_STATE || state == END_STATE) {
            canvas.drawText("BEST: " + getTime(MainActivity.MAX_TIME), 10 * GameView.SCALE_X,
                    20 * GameView.SCALE_Y, Resources.getPaint(10, false));
        }

        if (MainActivity.PAUSED) {
            canvas.drawText("PAUSED", GameView.WIDTH / 2, GameView.HEIGHT / 2,
                    Resources.getPaint(10, true));
        }

        if (state == END_STATE) {
            canvas.drawText("GAME OVER", GameView.WIDTH / 2, GameView.HEIGHT / 2,
                    Resources.getPaint(10, true));
        }
        STATE_BUTTON.draw(canvas);
        HELP_BUTTON.draw(canvas);
    }

    private void reset() {
        state = GAME_STATE;
        STATE_BUTTON.setState(Button.PAUSE);
        HELP_BUTTON.setState(Button.HELP);

        map = new Map();
        player = new Player();
        time = 0;
    }

    private String getTime(double time) {
        int minutes = (int) (time / 60000);
        int seconds = (int) (time / 1000) % 60;
        String minutesText = minutes + "";
        String secondsText = seconds + "";
        if (seconds < 10) {
            secondsText = "0" + secondsText;
        }
        return minutesText + ":" + secondsText;
    }

    public static Map getMap() {
        return map;
    }
}
