package com.example.runner.states;

import android.graphics.Canvas;

public abstract class State {

    public State() {

    }

    public abstract void update();

    public abstract void draw(Canvas canvas);

    public abstract void input(float tx, float ty);
}
