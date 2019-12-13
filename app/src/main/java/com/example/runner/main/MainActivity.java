package com.example.runner.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static int WIDTH, HEIGHT;
    public static boolean PAUSED;

    public static int MAX_TIME;
    private static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        );
        prefs = this.getPreferences(Context.MODE_PRIVATE);
        MAX_TIME = prefs.getInt("HIGH_SCORE", 0);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        WIDTH = metrics.widthPixels;
        HEIGHT = metrics.heightPixels;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(new GameView(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("STATE", "PAUSED");
        PAUSED = true;
    }

    public static void save(double time) {
        if (time > MAX_TIME) {
            MAX_TIME = (int) time;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("HIGH_SCORE", (int) time);
            editor.apply();
        }
    }
}
