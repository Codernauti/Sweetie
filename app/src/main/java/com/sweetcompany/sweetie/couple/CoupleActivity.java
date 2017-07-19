package com.sweetcompany.sweetie.couple;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.sweetcompany.sweetie.DashboardActivity;

/**
 * Created by Eduard on 17-Jul-17.
 */

public class CoupleActivity extends AppCompatActivity {

    private Button mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: extract from Bundle partner Name?
        String partnerName = "Unknown";
        mButton = new Button(this);
        mButton.setText("Congratulation! You are couple with " + partnerName);
        setContentView(mButton);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoupleActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }
}
