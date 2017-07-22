package com.sweetcompany.sweetie.gallery;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.sweetcompany.sweetie.R;
import android.app.ProgressDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sweetcompany.sweetie.firebase.FirebaseGalleryController;
import com.sweetcompany.sweetie.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private String TAG = GalleryActivity.class.getSimpleName();

    // key for Intent extras
    public static final String GALLERY_DATABASE_KEY = "gALLERYDatabaseKey";
    public static final String GALLERY_TITLE = "gALLERYTitle";    // For offline user
    public static final String ACTION_DATABASE_KEY = "ActionDatabaseKey";

    private GalleryPresenter mPresenter;
    private FirebaseGalleryController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);

        String chatKey = null;
        String actionKey = null;
        if (savedInstanceState == null) { // first Activity open
            Bundle chatBundle = getIntent().getExtras();
            if (chatBundle != null) {
                chatKey = chatBundle.getString(GALLERY_DATABASE_KEY);
                actionKey = chatBundle.getString(ACTION_DATABASE_KEY);

                Log.d(TAG, "from Intent CHAT_TITLE: " +
                        chatBundle.getString(GALLERY_TITLE));
                Log.d(TAG, "from Intent CHAT_DATABASE_KEY: " +
                        chatBundle.getString(GALLERY_DATABASE_KEY));
                Log.d(TAG, "from Intent CHAT_ACTION_KEY: " +
                        chatBundle.getString(ACTION_DATABASE_KEY));
            }
            else {
                Log.w(TAG, "getIntent FAILED!");
            }
        } else {
            // TODO: restore data from savedInstanceState
        }

        GalleryFragment view = (GalleryFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gallery_fragment_container);

        if (view == null) {
            view = GalleryFragment.newInstance(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.gallery_fragment_container, view);
            transaction.commit();
        }

        String userMail = Utility.getStringPreference(this, Utility.MAIL);
        String coupleUid = Utility.getStringPreference(this, Utility.COUPLE_UID);

        if (chatKey != null) {
            //mController = new FirebaseChatController(coupleUid, chatKey, actionKey);
            //mPresenter = new ChatPresenter(view, mController, userMail);
        }
        else {
            Log.w(TAG, "Impossible to create ChatController and ChatPresenter because chatKey is NULL");
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        //mController.attachListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mController.detachListeners();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

