package com.sweetcompany.sweetie.geogift;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sweetcompany.sweetie.R;

public class GeogiftTestActivity extends AppCompatActivity {

    private static final String TAG = "GeogiftTestActivity";

    // key for Intent extras
    public static final String GEOGIFT_DATABASE_KEY = "GeogiftDatabaseKey";
    public static final String GEOGIFT_TITLE = "GeogiftTitle";    // For offline user
    public static final String ACTION_DATABASE_KEY = "ActionDatabaseKey";

    private GeogiftTestFragment mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geogift_activity_test);


        mView = (GeogiftTestFragment) getSupportFragmentManager()
                .findFragmentById(R.id.geogift_fragment_container_test);

        if (mView == null) {
            mView = GeogiftTestFragment.newInstance(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.geogift_fragment_container_test, (GeogiftTestFragment)mView);
            transaction.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
