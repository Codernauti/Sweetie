package com.sweetcompany.sweetie.chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;

import com.sweetcompany.sweetie.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconGridView;
import io.github.rockerhieu.emojicon.EmojiconPage;
import io.github.rockerhieu.emojicon.EmojiconsView;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

/**
 * Created by Eduard on 26-Jul-17.
 */

class EmoticonsPagerAdapter extends PagerAdapter implements
        EmojiconGridFragment.OnEmojiconClickedListener{

    private static final String TAG = "EmoticonsPagerAdapter";
    private static final int TOT_NUM_PAGES = 5;

    private Context mContext;
    private ArrayList<EmojiconGridView> mEmoticonsPages = new ArrayList<>();

    private EmoticonsPagerAdapterListener mListener;

    interface EmoticonsPagerAdapterListener {
        void onEmoticonsClicked(Emojicon emojicon);
    }


    EmoticonsPagerAdapter(Context context) {
        mContext = context;

        for (int i = 0; i < TOT_NUM_PAGES; i++) {
            EmojiconGridView page = new EmojiconGridView(mContext);
            page.setOnEmojiconClickedListener(this);
            setEmoticonsData(i, page);
            page.setNumColumns(8);
            mEmoticonsPages.add(page);
        }
    }

    private void setEmoticonsData(int position, EmojiconGridView page) {
        switch (position) {
            case 0:
                page.setEmojiData(Emojicon.TYPE_PEOPLE, null, false);
                break;
            case 1:
                page.setEmojiData(Emojicon.TYPE_NATURE, null, false);
                break;
            case 2:
                page.setEmojiData(Emojicon.TYPE_PLACES, null, false);
                break;
            case 3:
                page.setEmojiData(Emojicon.TYPE_OBJECTS, null, false);
                break;
            case 4:
                page.setEmojiData(Emojicon.TYPE_SYMBOLS, null, false);
                break;
            default:
                break;    // user press CANC
        }
    }

    void setListener(EmoticonsPagerAdapterListener listener) {
        mListener = listener;
    }

    void removeListener() {
        mListener = null;
    }

    @Override
    public int getCount() {
        return TOT_NUM_PAGES + 1; // 5° page is a cancel button
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (position == TOT_NUM_PAGES) { // Backspace Tab
            return null;
        }
        View view = mEmoticonsPages.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position == TOT_NUM_PAGES) {    // Backspace Tab
            return;
        }
        container.removeView(mEmoticonsPages.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        // TODO: callback to ChatFragment
        Log.d(TAG, "emoticons arrived" + emojicon.getEmoji());
        mListener.onEmoticonsClicked(emojicon);
    }
}
