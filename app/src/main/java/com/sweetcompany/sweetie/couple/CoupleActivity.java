package com.sweetcompany.sweetie.couple;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sweetcompany.sweetie.DashboardActivity;

/**
 * Created by Eduard on 17-Jul-17.
 */
// TODO: design UI for this activity
public class CoupleActivity extends AppCompatActivity {

    // key for Intent extras
    public static final String MESSAGE_TO_SHOW_KEY = "messageToShow";

    private Button mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String buttonMessage = "Default message, you are coupled or your couple break";

        // first activity open
        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                buttonMessage = bundle.getString(MESSAGE_TO_SHOW_KEY);;
            }
        }

        mButton = new Button(this);
        mButton.setText(buttonMessage);
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
