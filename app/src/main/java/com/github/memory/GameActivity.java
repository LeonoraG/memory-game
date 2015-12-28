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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * The activity containing the actual game play
 */
public class GameActivity extends AppCompatActivity implements GameOverListener {

    private static final String TAG = GameActivity.class.getSimpleName();
    ImageAdapter myImageAdapter;
    //number of pressed cards
    int numPressed = 0;
    boolean done = false;
    Bitmap firstCard;
    Bitmap secondCard;
    HashMap<Integer, Integer> cardAt = new HashMap<Integer,Integer>();

    int maxPairs ;  //=4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//      requesting to turn the title OFF
//      requestWindowFeature(Window.FEATURE_NO_TITLE);
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
            if(numPressed == 0){
                int index = cardAt.get(position);
                int resId = getResources().getIdentifier("sample_" + index, "drawable", getPackageName());
                firstCard = BitmapFactory.decodeResource(getResources(), resId);
                myImageAdapter.updateImage(position, resId);
                numPressed++;
                done = true;
            }

            if((numPressed == 1) && !done){
                int index = cardAt.get(position);
                int resId = getResources().getIdentifier("sample_" + index, "drawable", getPackageName());
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
        if(firstCard.sameAs(secondCard))
            myImageAdapter.cardsMatched();
        else
            myImageAdapter.cardsNotMatched();
        numPressed = 0;
    }

    public void onGameOver(){
        PlayAgainFragment alertFragment = new PlayAgainFragment();
        FragmentManager fm = getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putInt("maxPairs", maxPairs);
        alertFragment.setArguments(args);
        alertFragment.show(fm, "Game over!");
    }

}