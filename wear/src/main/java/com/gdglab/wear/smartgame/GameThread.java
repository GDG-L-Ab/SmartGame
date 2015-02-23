package com.gdglab.wear.smartgame;


import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    private static final String TAG = GameThread.class.getSimpleName();

    private SurfaceHolder surfaceHolder;
    private MainGamePanel gamePanel;
    private boolean running;

    private final Context cont;


    public void setRunning(boolean running) {
        this.running = running;
    }
    public boolean isRunning(){
        return this.running;
    }

    public GameThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
        super();
        //TODO to delete, only for debug purpose
        setName(TAG);
        this.cont = gamePanel.getContext();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;



    }

    @Override
    public void run() {


        long tickCount = 0L;
        Log.d(TAG, "Starting game loop");
        while (running ) {
            //tickCount++;
            gamePanel.updateCanvas();

        }
        Log.d(TAG, "Game loop executed " + tickCount + " times");



    }

}