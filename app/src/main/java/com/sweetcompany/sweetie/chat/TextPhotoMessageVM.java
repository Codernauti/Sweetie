package com.sweetcompany.sweetie.chat;

import android.view.View;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.DataMaker;
import com.sweetcompany.sweetie.utils.Utility;

/**
 * Created by ghiro on 03/08/2017.
 */

public class TextPhotoMessageVM extends MessageVM {
    private String mText;
    private String mUriLocal;
    private String mUriStorage;
    private int mPercent;

    TextPhotoMessageVM(String text, boolean mainUser, String date, boolean bookMarked, String key, String uriL, String uriS) {
        super(mainUser, date, bookMarked, key);
        mText = text;
        mUriLocal = uriL;
        mUriStorage = uriS;
    }

    String getText() {
        return mText;
    }

    void setUriLocal(String uriL){
        this.mUriLocal = uriL;
    }
    String getUriLocal() {
        return mUriLocal;
    }

    void setUriStorage(String uriS){
        this.mUriStorage = uriS;
    }
    String getUriStorage() {
        return mUriStorage;
    }

    int getPercent(){
        return mPercent;
    }
    void setPercent(int perc){
        this.mPercent = perc;
    }

    @Override
    void configViewHolder(MessageViewHolder viewHolder) {
        // TODO: This downcast is secure?
        TextPhotoMessageViewHolder view = (TextPhotoMessageViewHolder) viewHolder;

        view.setText(mText);
        view.setTextTime(DataMaker.getHH_ss_Local(super.getTime()));
        view.setBookmark(super.isBookmarked());

        String uriToLoad;
        // is image uploaded by me?
        //verify if is in Local memory and has valid path
        if(super.isTheMainUser()) {
            if(Utility.isImageAvaibleInLocal(getUriLocal())) uriToLoad = getUriLocal();
            else uriToLoad = getUriStorage();
        }
        else // uploaded by partner, take uri storage
        {
            uriToLoad = getUriStorage();
        }

        view.setImage(uriToLoad);
        view.setPercentUploading(getPercent());
    }

    @Override
    int getIdView() {
        if (isTheMainUser()) {
            return R.layout.chat_user_list_item;
        }
        else {  // isThePartner()
            return R.layout.chat_partner_list_item;
        }
    }

    @Override
    TextPhotoMessageViewHolder newViewHolder(View inflatedView) {
        return new TextPhotoMessageViewHolder(inflatedView, isTheMainUser());
    }
}