package com.sweetcompany.sweetie.Registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sweetcompany.sweetie.DashboardActivity;
import com.sweetcompany.sweetie.DashboardPagerAdapter;
import com.sweetcompany.sweetie.LoginActivity;
import com.sweetcompany.sweetie.MainActivity;
import com.sweetcompany.sweetie.R;


public class RegisterActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    ViewPager mViewPager;
    RegisterPagerAdapter mAdapter;
    Context mContext;

    @Override
    //TODO usare setcustom animation e gestirseli da sè senza viewpager
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        mContext = getApplicationContext();
        mAdapter = new RegisterPagerAdapter(getSupportFragmentManager(), mContext);

        mViewPager = (ViewPager) findViewById(R.id.register_pager);
        mViewPager.setAdapter(mAdapter);
        //disable swiping // TODO checcazzè? esiste altro modo per bloccarlo?
        //mViewPager.beginFakeDrag();
        //mViewPager.setOffscreenPageLimit(0);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case RegisterPagerAdapter.STEP_ONE:
                mViewPager.setCurrentItem(RegisterPagerAdapter.STEP_ONE);
            case RegisterPagerAdapter.STEP_TWO:
                mViewPager.setCurrentItem(RegisterPagerAdapter.STEP_TWO);
            case RegisterPagerAdapter.STEP_THREE:
                mViewPager.setCurrentItem(RegisterPagerAdapter.STEP_THREE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void registrationCompleted(){
        startActivity(new Intent(this, DashboardActivity.class));

    }

}