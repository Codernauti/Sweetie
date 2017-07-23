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
    public static final String GALLERY_DATABASE_KEY = "GALLERYDatabaseKey";
    public static final String GALLERY_TITLE = "GALLERYTitle";    // For offline user
    public static final String ACTION_DATABASE_KEY = "ActionDatabaseKey";

    private GalleryPresenter mPresenter;
    private FirebaseGalleryController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);

        String galleryKey = null;
        String actionKey = null;
        if (savedInstanceState == null) { // first Activity open
            Bundle galleryBundle = getIntent().getExtras();
            if (galleryBundle != null) {
                galleryKey = galleryBundle.getString(GALLERY_DATABASE_KEY);
                actionKey = galleryBundle.getString(ACTION_DATABASE_KEY);

                Log.d(TAG, "from Intent GALLERY_TITLE: " +
                        galleryBundle.getString(GALLERY_TITLE));
                Log.d(TAG, "from Intent GALLERY_DATABASE_KEY: " +
                        galleryBundle.getString(GALLERY_DATABASE_KEY));
                Log.d(TAG, "from Intent GALLERY_ACTION_KEY: " +
                        galleryBundle.getString(ACTION_DATABASE_KEY));
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

        if (galleryKey != null) {
            //mController = new FirebaseGalleryController(coupleUid, galleryKey, actionKey);
            //mPresenter = new GalleryPresenter(view, mController, userMail);
        }
        else {
            Log.w(TAG, "Impossible to create GalleryController and GalleryPresenter because galleryKey is NULL");
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

