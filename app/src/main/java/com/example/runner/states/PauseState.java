package com.example.runner.states;

import android.graphics.Canvas;

import com.example.runner.main.GameView;
import com.example.runner.main.Map;
import com.example.runner.main.Resources;
import com.example.runner.objects.Button;

public class PauseState extends State {
    private Button unpause;

    PauseState() {
        unpause = new Button(1, Map.WIDTH - 26, 10);
    }

    public void update() {

    }

    public void draw(Canvas canvas) {
        canvas.drawText("PAUSED", GameView.WIDTH / 2, GameView.HEIGHT / 2,
                Resources.getPaint(10, true));
        unpause.draw(canvas);
    }

    public void input(float tx, float ty) {
        if (unpause.click(tx, ty)) {
            Manager.pause();
        }
    }
}
