package com.sweetcompany.sweetie.Registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.sweetcompany.sweetie.DashboardActivity;
import com.sweetcompany.sweetie.R;


public class RegisterActivity extends AppCompatActivity{

    Context mContext;
    Fragment mFragment;
    FragmentTransaction mTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        mContext = getApplicationContext();
        mFragment = new StepOne();
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.add(R.id.register_fragment_container,mFragment);
        mTransaction.commit();

    }
    public void registrationCompleted(){
        startActivity(new Intent(this, DashboardActivity.class));
    }

}