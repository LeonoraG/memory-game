package com.github.memory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class PlayAgainFragment extends DialogFragment {
    int maxPairs;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                // Set Dialog Icon
                .setIcon(R.mipmap.ic_launcher)
                        // Set Dialog Title
                .setTitle("You won!")
                        // Set Dialog Message
                .setMessage("What to do next?")

                        // Positive button
                .setPositiveButton("Play again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), GameActivity.class);
                        intent.putExtra("maxPairs", maxPairs);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();
                    }
                })
                .setNeutralButton("Quit application", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(homeIntent);
                    }
                })
                        // Negative Button
                .setNegativeButton("Main menu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), MainMenu.class);
                        intent.putExtra("maxPairs", maxPairs);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }).create();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        maxPairs = getArguments().getInt("maxPairs");
    }
}
