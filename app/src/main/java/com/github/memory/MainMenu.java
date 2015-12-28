package com.github.memory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * First menu you see when the app is run
 */

public class MainMenu extends AppCompatActivity {

    //Max number of card pairs in game
    int maxPairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        maxPairs = 3;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Open a new game
     * @param view
     */
    public void newGameClicked(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("maxPairs", maxPairs);
        startActivity(intent);
    }
    /**
     * Opens the settings for number of cards
     * @param view
     */
    public void configureCardNumberClicked(View view) {
        Intent intent = new Intent(this, CardNumberActivity.class);
        intent.putExtra("maxPairs", maxPairs);
        startActivityForResult(intent, 1);

    }

    /**
     * Exit the application
     * @param view
     */
    public void exitApplicationClicked(View view) {

        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(homeIntent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("REQUEST CODE:" + requestCode);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    maxPairs = data.getIntExtra("maxPairs",3);
                }
                break;
            default:
                break;
        }
    }
}
