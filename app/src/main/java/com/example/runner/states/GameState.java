package com.example.runner.states;

import android.graphics.Canvas;

import com.example.runner.main.GameThread;
import com.example.runner.main.GameView;
import com.example.runner.main.Map;
import com.example.runner.main.Resources;
import com.example.runner.objects.Button;
import com.example.runner.objects.Player;

public class GameState extends State {
    private static Map map;
    private Player player;
    private Button pause;
    private static double time;

    GameState() {
        map = new Map();
        pause = new Button(0, Map.WIDTH - 26, 10);
        player = new Player();
        time = 0;
    }

    public void update() {
        if (player.getState() != Player.IDLE_STATE) {
            time += GameThread.TPF;
            map.update();
        }
        player.update();

        if (player.getRight() < -GameView.WIDTH / 10f
                || player.getTop() > 6 * GameView.HEIGHT / 5f) {
            Manager.setState(Manager.END_STATE);
        }
    }

    public void draw(Canvas canvas) {
        map.draw(canvas);
        player.draw(canvas);
        drawUI(canvas);
    }

    public void input(float tx, float ty) {
        if (player.getState() == Player.IDLE_STATE) {
            player.run();
        } else if (pause.click(tx, ty)) {
            Manager.pause();
        } else {
            player.jump();
        }
    }

    private void drawUI(Canvas canvas) {
        String timeText = getTime();
        canvas.drawText(timeText, GameView.WIDTH / 2, 20 * GameView.SCALE_Y,
                Resources.getPaint(10, true));
        if (player.getState() == Player.IDLE_STATE) {
            canvas.drawText("TAP TO START", GameView.WIDTH / 2, GameView.HEIGHT / 2,
                    Resources.getPaint(10, true));
        }

        if (!Manager.isPaused() && Manager.getState() == Manager.GAME_STATE) {
            pause.draw(canvas);
        }
    }

    public static String getTime() {
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
