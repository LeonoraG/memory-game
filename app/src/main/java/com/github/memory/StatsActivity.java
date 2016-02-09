package com.github.memory;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {
    int maxPairs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        maxPairs = getIntent().getExtras().getInt("maxPairs", 3);
        LinearLayout linearLayout =  (LinearLayout)findViewById(R.id.statsView);

        final DBAdapter db = new DBAdapter(this);

        int i = 1;

        TextView entry00 = new TextView(this);
        linearLayout.addView(entry00);

//        TextView entry = new TextView(this);
//        entry.setTextColor(Color.parseColor("#FFFFFF"));
//        entry.setText(" name time number of tries number of cards");
//        linearLayout.addView(entry);

        db.open();
        Cursor c = db.getSortedByNumberOfTries(maxPairs * 2);
        if (c.moveToFirst())
        {
            do {
                TextView entry = new TextView(this);
                entry.setTextColor(Color.parseColor("#FFFFFF"));
                entry.setText(i + ". name: " + c.getString(1)
                        + ", time: "+ c.getInt(2)
                        + ", number of tries: " + c.getInt(3)
                        + ", number of cards: "+c.getInt(4));
                linearLayout.addView(entry);
                i++;
            } while (c.moveToNext() && i < 11);
        }
        db.close();

        TextView entry11 = new TextView(this);
        linearLayout.addView(entry11);


        Button button = new Button(this);
        button.setText("Reset");
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                db.open();
                db.reset();
                db.close();

            }
        });
        linearLayout.addView(button);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
