package com.example.runner.states;

import android.graphics.Canvas;

import com.example.runner.main.GameView;
import com.example.runner.main.Map;
import com.example.runner.main.Resources;
import com.example.runner.objects.Button;

public class EndState extends State {
    private Button retry;

    EndState() {
        retry = new Button(2, Map.WIDTH - 26, 10);
    }

    public void update() {

    }

    public void draw(Canvas canvas) {
        canvas.drawText("GAME OVER", GameView.WIDTH / 2, GameView.HEIGHT / 2,
                Resources.getPaint(10, true));
        retry.draw(canvas);
    }

    public void input(float tx, float ty) {
        if (retry.click(tx, ty)) {
            Manager.setState(Manager.GAME_STATE);
        }
    }
}
