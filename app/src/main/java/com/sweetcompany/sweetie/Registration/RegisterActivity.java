package com.sweetcompany.sweetie.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.sweetcompany.sweetie.DashboardActivity;
import com.sweetcompany.sweetie.R;


public class RegisterActivity extends AppCompatActivity {

    private StepOne mView;
    private FragmentTransaction mTransaction;

    private RegisterPresenter mPresenter;
    private PairingPresenter mPairingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        mView = new StepOne();
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.add(R.id.register_fragment_container, mView);
        mTransaction.commit();

        mPresenter = new RegisterPresenter(mView);
    }

    public void setPresenter(RegisterContract.View view){
        mPresenter = mPresenter.setView(view);
    }

    public void registrationCompleted(){
        startActivity(new Intent(this, DashboardActivity.class));
    }

    public void initAndOpenPairingFragment() {
        Intent intent = new Intent(this, PairingActivity.class);
        startActivity(intent);
    }

}