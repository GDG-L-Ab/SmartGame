package com.gdglab.wear.smartgame;

import android.util.Log;

/**
 * Created by antonino.orlando on 18/02/2015.
 */
public class ScoreCls {
    private static final String TAG ="ScoreCls" ;
    private static ScoreCls istanza = null;

    private static int score =0;
    private ScoreCls() {}

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        ScoreCls.score = score;
    }
    public static void reset() {
        ScoreCls.score = 0;
    }
    public static void increaseScore() {
        ScoreCls.score +=1;
        Log.i(TAG,""+score);
    }

    public static synchronized ScoreCls getScoreCls() {
        if (istanza == null)
            istanza = new ScoreCls();
        return istanza;
    }
}
