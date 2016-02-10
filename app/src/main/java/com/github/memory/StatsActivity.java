package com.github.memory;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
//import android.widget.TableRow.LayoutParams

public class StatsActivity extends AppCompatActivity {
    int maxPairs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        maxPairs = getIntent().getExtras().getInt("maxPairs", 3);
        LinearLayout linearLayout =  (LinearLayout)findViewById(R.id.linearLayout0);
        TableLayout tableLayout = (TableLayout)findViewById(R.id.table);
        TextView tv = (TextView) findViewById(R.id.headline);
        tv.setText("Highscores for "+maxPairs*2+" cards:");
        tv.setTextColor(Color.parseColor("#C60B89"));
        tv.setTypeface(null, Typeface.BOLD);

        final DBAdapter db = new DBAdapter(this);

        int i = 1;

//        TextView entry00 = new TextView(this);
//        linearLayout.addView(entry00);

//        TextView entry = new TextView(this);
//        entry.setTextColor(Color.parseColor("#FFFFFF"));
//        entry.setText(" name time number of tries number of cards");
//        linearLayout.addView(entry);

        db.open();
        Cursor c = db.getSortedByNumberOfTries(maxPairs * 2);
        if (c.moveToFirst())
        {
            do {
                createRowInLayout(tableLayout, c.getString(1), i ,  c.getInt(2), c.getInt(3));
//                TextView entry = new TextView(this);
//                entry.setTextColor(Color.parseColor("#FFFFFF"));
//                entry.setText(i + ". name: " + c.getString(1)
//                        + ", time: "+ c.getInt(2)
//                        + ", number of tries: " + c.getInt(3)
//                        + ", number of cards: "+c.getInt(4));
//                linearLayout.addView(entry);
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

    private void createRowInLayout(TableLayout layout,String name, int rank,  int time, int tries){
        TextView rankEntry  = new TextView(this);
        TextView nameEntry  = new TextView(this);
        TextView timeEntry  = new TextView(this);
        TextView triesEntry = new TextView(this);

        rankEntry.setText(rank + ".");
        nameEntry.setText(name);
        timeEntry.setText(time + "");
        triesEntry.setText(tries + "");

        int color = Color.parseColor("#FFFFFF");
        if(rank%2 != 0)
            color = Color.parseColor("#B3F20E");

        rankEntry.setTextColor(color);
        nameEntry.setTextColor(color);
        timeEntry.setTextColor(color);
        triesEntry.setTextColor(color);


        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams lp4 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        lp1.gravity= Gravity.CENTER;
        lp2.gravity= Gravity.CENTER;
        lp3.gravity= Gravity.CENTER;
        lp4.gravity= Gravity.CENTER;

        rankEntry.setLayoutParams(lp1);
        nameEntry.setLayoutParams(lp2);
        timeEntry.setLayoutParams(lp3);
        triesEntry.setLayoutParams(lp4);


        tableRow.addView(rankEntry);
        tableRow.addView(nameEntry);
        tableRow.addView(timeEntry);
        tableRow.addView(triesEntry);

        layout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
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
