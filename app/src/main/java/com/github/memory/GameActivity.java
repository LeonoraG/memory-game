package com.github.memory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * The activity containing the actual game play
 */
public class GameActivity extends AppCompatActivity implements GameOverListener {

    private static final String TAG = GameActivity.class.getSimpleName();
    private Handler mHandler = new Handler();
    ImageAdapter myImageAdapter;
    //number of pressed cards
    int numPressed = 0;
    boolean done = false;
    Bitmap firstCard;
    Bitmap secondCard;
    HashMap<Integer, Integer> cardAt = new HashMap<Integer,Integer>();

    int numTries = 0;
    int maxPairs;
    int cardsMatched = 0;
    long mStartTime;
    String finalSeconds = "";
    String finalMinutes = "";
    TextView mTimeLabel;
    TextView mCardLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        maxPairs = getIntent().getExtras().getInt("maxPairs", 3);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        myImageAdapter = new ImageAdapter(this, maxPairs);
        myImageAdapter.notifyDataSetChanged();
        myImageAdapter.setGameOverListener(this);
        gridview.setAdapter(myImageAdapter);
        gridview.setOnItemClickListener(listener);
        initialShuffle();
        mCardLabel = (TextView) findViewById(R.id.tries);
        mCardLabel.setText(numTries+"");
        //For the timer
        mTimeLabel = (TextView) findViewById(R.id.timeElapsed);
        mStartTime = System.currentTimeMillis();
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, 100);

    }

    private void initialShuffle(){

        List<Integer> solution = new ArrayList<>();
        for (int j = 0; j < maxPairs; j++){
            solution.add(j);
            solution.add(j);
        }

        int i = 0;
        Collections.shuffle(solution);

        for(int j : solution){
            cardAt.put(i, j);
            i++;
        }
    }
    OnItemClickListener listener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
             done = false;
            if(numPressed == 0 && (myImageAdapter.getResIdAt(position)!= R.drawable.transparent)){
                int index = cardAt.get(position);
                int resId = getResources().getIdentifier("card_" + index, "drawable", getPackageName());
                firstCard = BitmapFactory.decodeResource(getResources(), resId);
                myImageAdapter.updateImage(position, resId);
                numPressed++;
                done = true;
            }

            if((numPressed == 1) && !done && (myImageAdapter.getResIdAt(position)!= R.drawable.transparent)){
                int index = cardAt.get(position);
                int resId = getResources().getIdentifier("card_" + index, "drawable", getPackageName());
                secondCard = BitmapFactory.decodeResource(getResources(), resId);
                myImageAdapter.updateImage(position, resId);
                Handler handler = new Handler();
                numPressed++;
                handler.postDelayed(new Runnable() {
                    public void run() {
                        checkCards();
                    }
                }, 1000);
            }
        }
    };

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

    protected void checkCards(){
        Log.d(TAG, "Checking...");
        numTries++;
        mCardLabel.setText(numTries+"");
        if(firstCard.sameAs(secondCard))
        {
            myImageAdapter.cardsMatched();
            cardsMatched++;
        }
        else
            myImageAdapter.cardsNotMatched();
        numPressed = 0;
    }

    public void onGameOver(){
        PlayAgainFragment alertFragment = new PlayAgainFragment();
        FragmentManager fm = getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putInt("maxPairs", maxPairs);
        args.putInt("numTries", numTries);
        args.putString("finalMinutes", finalMinutes);
        args.putString("finalSeconds", finalSeconds);
        alertFragment.setArguments(args);
        mHandler.removeCallbacks(mUpdateTimeTask); //stop the time
        alertFragment.show(fm, "Game over!");
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            final long start = mStartTime;
            long millis = System.currentTimeMillis() - start;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds     = seconds % 60;
            Log.d(TAG, "Koliko ");
            if (seconds < 10) {
                mTimeLabel.setText("" + minutes + ":0" + seconds);
                finalSeconds = "0"+seconds;
            } else {
                mTimeLabel.setText("" + minutes + ":" + seconds);
                finalSeconds = ""+seconds;
            }
            finalMinutes = minutes+"";

           // mTimeLabel.setText("iiiiiiiiiiie");

            mHandler.postDelayed(mUpdateTimeTask, 100);
        }
    };

}