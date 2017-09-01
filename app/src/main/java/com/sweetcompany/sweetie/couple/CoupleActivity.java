package com.sweetcompany.sweetie.couple;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sweetcompany.sweetie.DashboardActivity;
import com.sweetcompany.sweetie.R;

/**
 * Created by Eduard on 17-Jul-17.
 */
// TODO: extract UI for this activity
public class CoupleActivity extends AppCompatActivity {

    private static final String TAG = "CoupleActivity";

    // key for Intent extras
    public static final String FIRST_MESSAGE_KEY = "messageToShow";
    public static final String IMAGE_PARTNER_KEY = "imagePartner";
    public static final String BREAK_KEY = "breakKey";

    private ImageView mPartnerImage;
    private TextView mCoupleMessage;
    private ImageButton mOkButton;
    private TextView mFirstInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.couple_activity);

        String buttonMessage = "Default message, you are coupled or your couple break";
        String imageUri = null;
        boolean coupleBreak = false;

        // first activity open
        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                buttonMessage = bundle.getString(FIRST_MESSAGE_KEY);;
                imageUri = bundle.getString(IMAGE_PARTNER_KEY);
                coupleBreak = bundle.getBoolean(BREAK_KEY);
            }
        }

        mPartnerImage = (ImageView) findViewById(R.id.couple_partner_image);
        mCoupleMessage = (TextView) findViewById(R.id.couple_first_message);
        mOkButton = (ImageButton) findViewById(R.id.couple_ok);
        mFirstInfo = (TextView) findViewById(R.id.couple_first_info);


        if (coupleBreak) {

            // TODO: set heart broken
            mPartnerImage.setVisibility(View.GONE);
            mOkButton.setImageResource(R.drawable.heart_empty);
            mFirstInfo.setVisibility(View.GONE);

        } else {
            Log.d(TAG, "image uri of partner: " + imageUri);

            Glide.with(this)
                    .load(imageUri)
                    .placeholder(R.drawable.image_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(mPartnerImage);

            mCoupleMessage.setText(buttonMessage);

            mOkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CoupleActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
}
