package com.github.memory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class CardNumberActivity extends AppCompatActivity {
    int maxPairs;
    int copyOfMaxPairs;
    String name;
    String copyOfName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_number);

        maxPairs = getIntent().getExtras().getInt("maxPairs", 3);
        copyOfMaxPairs = maxPairs;
        name = getIntent().getExtras().getString("name");
        copyOfName = name;

        int resId = getResources().getIdentifier("RadioButtonID" + maxPairs*2, "id", getPackageName());
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupID);
        radioGroup.check(resId);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup rGroup, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton)rGroup.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked)
                    maxPairs = Integer.parseInt(checkedRadioButton.getTag().toString())/2;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_card_number, menu);
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
     * On apply we set the new value to maxPairs, and return to the main menu
     * @param view
     */
    public void applyClicked(View view) {

        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra("maxPairs", maxPairs);
        EditText et = (EditText)findViewById(R.id.nameInput);
        name = et.getText().toString();
        intent.putExtra("name", name);
        System.out.println("prije: " + maxPairs);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * When cancel is clicked just return to the main menu
     * @param view
     */
    public void cancelClicked(View view) {
        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra("maxPairs", copyOfMaxPairs);
        intent.putExtra("name", copyOfName);
        setResult(RESULT_OK, intent);
        finish();
    }
}
