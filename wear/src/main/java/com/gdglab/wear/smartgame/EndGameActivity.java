package com.gdglab.wear.smartgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class EndGameActivity extends Activity {

    private TextView mTextView;
    private TextView mScoreValueTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        /*final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {


            }
        });*/
        mTextView = (TextView) findViewById(R.id.text);
        mScoreValueTV = (TextView) findViewById(R.id.scoreValue);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setScore();
    }

    public void retry(View v){

        startActivity(new Intent(getApplicationContext(),GameActivity.class));
        finish();
    }

    public void dismiss(View v){
        finish();
    }
    private void setScore() {


        Intent in = getIntent();
        if (in!=null){
            if (in.hasExtra(GameConst.ACTION_END_SCORE)){
                String score = String.valueOf(in.getExtras().getInt(GameConst.ACTION_END_SCORE));

                mScoreValueTV.setText(score);
            }
        }
    }

}
