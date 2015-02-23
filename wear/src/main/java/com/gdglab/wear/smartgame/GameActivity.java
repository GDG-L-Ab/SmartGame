package com.gdglab.wear.smartgame;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


public class GameActivity extends Activity {
    private static final String TAG = GameActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // requesting to turn the title OFF

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // making it full screen

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


//        setContentView(new MainGamePanel(this));
        setContentView(R.layout.activity_game);


    }

    @Override
    protected void onResume() {
        super.onResume();

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(getWindow().getAttributes().width,getWindow().getAttributes().height);
        // set our MainGamePanel as the View
        addContentView(new MainGamePanel(this),layoutParams);

        Log.d(TAG, "View added");
    }



    @Override
    protected void onDestroy() {
        Log.d(TAG, "Destroying...");



        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopping...");


        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
