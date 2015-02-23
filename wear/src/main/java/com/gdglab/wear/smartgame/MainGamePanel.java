package com.gdglab.wear.smartgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;

public class MainGamePanel extends SurfaceView implements
        SurfaceHolder.Callback {

    private static final String TAG = "MainGamePanel";
    private GameThread thread;
    private Rect rect;
    private Context ctx;
    int mInteractiveBackgroundColor = Color.parseColor("green");

    private Paint mBackgroundPaint;
    private Paint loosePaint;

    int mDisplayWidth = 280;
    int mDisplayHeight = 280;

    // Touch index, use 0 everytime (single touch)
    int touchIndex = 0;

    // to avoid a square in the same place as previous
    int oldRandValue = 5;

    RelativeLayout mFrame = null;


    Handler mHandler = new Handler();

    //updated tap x,y coordinates
    int tapX = 0;
    int tapY = 0;

    private enum TapType {
        INITIAL_STATE,
        TAP_OK,
        TAP_WRONG
    }

    TapType touchSentinel = TapType.INITIAL_STATE;

    int sleepTime =1000;
    int sleepDelta=-20;

    public MainGamePanel(final Context context) {
        super(context);
        getHolder().addCallback(this);


        this.ctx = context;
        rect = new Rect();
        mFrame = (RelativeLayout) findViewById(R.id.frame);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(mInteractiveBackgroundColor);
        mBackgroundPaint.setAntiAlias(true);

        loosePaint = new Paint();
        loosePaint.setAntiAlias(true);


        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mDisplayWidth = size.x;
        mDisplayHeight = size.y;


        ScoreCls.getScoreCls().reset();
        thread = new GameThread(getHolder(), MainGamePanel.this);

        new CountDownTimer(4000, 1000) {
            Toast toast = Toast.makeText(context, "message", Toast.LENGTH_SHORT);

            public void onTick(long millisUntilFinished) {
                int secUntilFinish = (int) millisUntilFinished / 1000;
                if (secUntilFinish >= 1)
                    toast.setText("" + (secUntilFinish));
                toast.show();
//                  Toast.makeText(getContext(),""+(secUntilFinish),Toast.LENGTH_SHORT).show();

            }

            public void onFinish() {
                // create the game loop thread
                toast.cancel();
                if (thread != null)
                    thread.start();


            }
        }.start();

        setFocusable(true);

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (thread != null)
            thread.setRunning(true);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                if (thread != null) {
                    thread.join();
                    thread.setRunning(false);
                    thread = null;
                    retry = false;
                }
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (thread != null && thread.isRunning()) {
//            Log.i(TAG, "touch");
            tapX = (int) event.getX(touchIndex);
            tapY = (int) event.getY(touchIndex);
//            Log.i(TAG, "X: " + String.valueOf(x));
//            Log.i(TAG, "Y: " + String.valueOf(y));

            if (rect.contains(tapX, tapY)) {
                touchSentinel = TapType.INITIAL_STATE.TAP_OK;
                ScoreCls.getScoreCls().increaseScore();
            } else {
//                Log.i(TAG, "false");
//                Toast.makeText(getContext(), "You loose!!!!", Toast.LENGTH_SHORT).show();
                touchSentinel = TapType.TAP_WRONG;
                thread.setRunning(false);

            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }

    public void updateCanvas() {
        // updateCanvas game state
        // render state to the screen
        Canvas canvas = null;

        canvas = getHolder().lockCanvas();

        if (null != canvas) {

            try {

                Thread.sleep(sleepTime);

                if (touchSentinel == TapType.TAP_WRONG) {

//                  Toast.makeText(getContext(),"You loose!!!!",Toast.LENGTH_SHORT).show();

                    mBackgroundPaint.setColor(getResources().getColor(R.color.light_grey));
                    mBackgroundPaint.setAlpha(100);

                    /*
                    * TODO: set Red color on the wrong tapped rect
                     */
                   /* Paint wrongTapArea = new Paint();
                    wrongTapArea.setColor(getResources().getColor(R.color.red));
                    canvas.drawRect(tapX-10,tapY-10,tapX+10,tapY+10,wrongTapArea);*/

                    canvas.drawPaint(mBackgroundPaint);
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.x);
                    //TODO: center on the screen correctly!
                    canvas.drawBitmap(bitmap, mDisplayWidth / 4, mDisplayHeight / 4, loosePaint);
                    if (thread != null)
                        thread.setRunning(false);

                    getHolder().unlockCanvasAndPost(canvas);

                    mHandler.postDelayed(new Runnable() {
                      @Override
                       public void run () {
                                ((Activity) ctx).finish();
                                startEndGameActivity();
                      }
                    },2000);

                    return;
                }
                if (sleepTime >=20)
                    sleepTime -=20;

                Random quadrant = new Random();
                int quad = quadrant.nextInt(4);

                //I don't want the same value as previous
                while (quad == oldRandValue) {
                    quad = quadrant.nextInt(4);
                }

                oldRandValue = quad;
                int left = 0;
                int top = 0;
                int right = 0;
                int bottom = 0;
                switch (quad) {
                    case 0:

                        left = 0;
                        top = 0;
                        right = left + mDisplayWidth / 2;
                        bottom = top + mDisplayHeight / 2;

                        break;
                    case 1:
                        left = mDisplayWidth / 2;
                        top = 0;
                        right = mDisplayWidth;
                        bottom = mDisplayHeight / 2;
                        break;
                    case 2:
                        left = 0;
                        top = mDisplayHeight / 2;
                        right = left + mDisplayWidth / 2;
                        bottom = mDisplayHeight;
                        break;
                    case 3:
                        left = mDisplayWidth / 2;
                        top = mDisplayHeight / 2;
                        right = mDisplayWidth;
                        bottom = mDisplayHeight;
                        break;
                }

                rect.set(left, top, right, bottom);


                canvas.drawColor(Color.WHITE);
                canvas.drawRect(rect, mBackgroundPaint);
                touchSentinel = TapType.TAP_WRONG;
                getHolder().unlockCanvasAndPost(canvas);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void startEndGameActivity() {
        Intent in = new Intent(ctx, EndGameActivity.class);
        in.putExtra(GameConst.ACTION_END_SCORE, ScoreCls.getScoreCls().getScore());
        ctx.startActivity(in);
    }


}
