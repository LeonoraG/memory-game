package com.github.memory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    int foundPairs = 0;
    int maxPairs;
    private Integer[] mThumbIds;
    GameOverListener gameOverListener;

    public ImageAdapter(Context c, int maxPairs) {
        mContext = c;
        this.maxPairs = maxPairs;
         mThumbIds = new Integer[maxPairs*2];
        for(int i = 0; i < maxPairs*2; ++i){
            mThumbIds[i] = R.drawable.backside;
        }
    }

    public int getCount() { return mThumbIds.length; }

    public Object getItem(int position) { return null;}

    /**
     * Returns resource ID at the given position
     * @param position
     * @return
     */
    public Integer getResIdAt(int position){
        if(position < mThumbIds.length)
            return mThumbIds[position];
        else return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    public void updateImage(int position, int resourceId)
    {
        if(mThumbIds[position] != R.drawable.frontside)
        {
            mThumbIds[position] = resourceId;
            notifyDataSetChanged();
        }
    }

    /**
     * If we don't have a match, turn the cards back down.
     */
    public void cardsNotMatched(){
        for(int i = 0; i < mThumbIds.length; ++i)
            if(mThumbIds[i] != R.drawable.frontside && mThumbIds[i] != R.drawable.backside)
                mThumbIds[i] = R.drawable.backside;
        notifyDataSetChanged();
    }

    /**
     * If we have a match, remove that cards from game
     */
    public void cardsMatched(){
        foundPairs++;
        for(int i = 0; i < mThumbIds.length; ++i)
            if(mThumbIds[i] != R.drawable.backside)
                mThumbIds[i] = R.drawable.transparent;
        notifyDataSetChanged();
        if(foundPairs == maxPairs){
            foundPairs = 0;
            gameOverListener.onGameOver();
        }


    }

    /**
     * Sets the listener for the end of the game
     * @param gameOver
     */
    public void setGameOverListener(GameOverListener gameOver) {
        this.gameOverListener = gameOver;
    }
}
