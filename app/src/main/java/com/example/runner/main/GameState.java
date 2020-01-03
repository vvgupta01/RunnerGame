package com.example.runner.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;

import com.example.runner.objects.Button;
import com.example.runner.objects.Player;
import com.example.runner.objects.Berry;

public class GameState {
    private static final int GAME_STATE = 0, END_STATE = 1;
    private final Button STATE_BUTTON;
    private final Button HELP_BUTTON;

    private SharedPreferences prefs;
    private Map map;
    private Player player;

    private int state;
    private int berries;
    private float highScore, score;
    private boolean paused;

    private float textShift, textVel;

    GameState(Context context) {
        STATE_BUTTON = new Button(Map.WIDTH - Button.WIDTH - 10, 10);
        HELP_BUTTON = new Button(Map.WIDTH - 2 * (Button.WIDTH + 10), 10);

        prefs = context.getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE);
        highScore = prefs.getInt("HIGH_SCORE", 0);
        reset();
    }

    private void togglePause() {
        paused = !paused;
        if (paused) {
            STATE_BUTTON.setState(Button.PLAY);
            AudioPlayer.pause();
        } else {
            STATE_BUTTON.setState(Button.PAUSE);
            AudioPlayer.start();
        }
    }

    void pause() {
        if (!paused && state != END_STATE) {
            togglePause();
        }
    }

    void update() {
        if (state != END_STATE && !paused) {
            if (player.getState() != Player.IDLE_STATE) {
                score += GameThread.TPF / 10;
                map.update(player);

                if (player.getTop() > GameView.HEIGHT && map.isStable()) {
                    end();
                }
            } else {
                if (Math.abs(textShift) >= Berry.SCALED_MAX_SHIFT) {
                    textVel *= -1;
                }
                textShift += textVel;
            }
            player.update();
        }
    }

    void draw(Canvas canvas) {
        map.draw(canvas);
        player.draw(canvas);
        drawUI(canvas);
    }

    void tap(float tx, float ty) {
        if (STATE_BUTTON.click(tx, ty)) {
            if (state == GAME_STATE && player.getState() != Player.IDLE_STATE) {
                togglePause();
            } else if (state == END_STATE) {
                reset();
            }
        } else if (player.getState() == Player.IDLE_STATE) {
            player.run();
            AudioPlayer.start();
        } else if (!paused) {
            player.jump();
        }
    }

    void swipe(float tx1, float ty1, float tx2, float ty2, float velX, float velY) {
        if (player.getState() == Player.GRAB_STATE
                && Math.abs(tx1 - tx2) <= 100 && velY > 0) {
            player.drop(true);
        }
    }

    private void drawUI(Canvas canvas) {
        drawText(canvas, (int) score + "", GameView.WIDTH / 2,
                20 * GameView.SCALE_Y, true);

        canvas.drawBitmap(Resources.ITEMS[0], 10 * GameView.SCALE_X,
                10 * GameView.SCALE_Y, null);
        drawText(canvas, berries + "", (Berry.WIDTH + 20) * GameView.SCALE_X,
                20 * GameView.SCALE_Y, false);

        if (player.getState() == Player.IDLE_STATE || state == END_STATE) {
            drawText(canvas, "BEST: " + (int) highScore, 10 * GameView.SCALE_X,
                    GameView.HEIGHT - 10 * GameView.SCALE_Y, false);
        }

        if (player.getState() == Player.IDLE_STATE) {
            drawText(canvas, "TAP TO START", GameView.WIDTH / 2,
                    GameView.HEIGHT / 2f + textShift, true);
        }

        if (paused) {
            drawText(canvas, "PAUSED", GameView.WIDTH / 2, GameView.HEIGHT / 2,
                    true);
        }

        if (state == END_STATE) {
            drawText(canvas, "GAME OVER", GameView.WIDTH / 2, GameView.HEIGHT / 2,
                    true);
        }
        STATE_BUTTON.draw(canvas);
        HELP_BUTTON.draw(canvas);
    }

    private void drawText(Canvas canvas, String text, float x, float y, boolean center) {
        if (center) {
            canvas.drawText(text, x, y, Resources.FILL_CENTER_PAINT);
            canvas.drawText(text, x, y, Resources.STROKE_CENTER_PAINT);
        } else {
            canvas.drawText(text, x, y, Resources.FILL_PAINT);
            canvas.drawText(text, x, y, Resources.STROKE_PAINT);
        }
    }

    private void end() {
        state = END_STATE;
        STATE_BUTTON.setState(Button.RESET);
        AudioPlayer.reset();
        save();
    }

    private void reset() {
        state = GAME_STATE;
        STATE_BUTTON.setState(Button.PAUSE);
        HELP_BUTTON.setState(Button.HELP);

        map = new Map(this);
        player = new Player(this);

        score = 0;
        berries = 0;

        textShift = 0;
        textVel = Berry.VELY;
    }

    private void save() {
        if (score > highScore) {
            highScore = score;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("HIGH_SCORE", (int) highScore);
            editor.apply();
        }
    }

    void collectBerry() {
        berries++;
        score += 100;
        AudioPlayer.playSound(AudioPlayer.BERRY, 0.05f);
    }

    public Map getMap() {
        return map;
    }
}
