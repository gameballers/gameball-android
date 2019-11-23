package com.gameball.androidx.views.smallNotification;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gameball.androidx.R;
import com.gameball.androidx.model.response.NotificationBody;
import com.gameball.androidx.utils.Constants;
import com.squareup.picasso.Picasso;

public class SmallNotificationActivity extends AppCompatActivity {

    private TextView notificationTitle;
    private TextView notificationBody;
    private ImageView notificationIcon;
    private ImageButton closeBtn;

    private NotificationBody notificationBodyObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gb_activity_small_notification);
        init();

        fillView();
    }

    private void init()
    {
        if(getIntent() != null)
            notificationBodyObj = (NotificationBody) getIntent().getExtras().getSerializable(Constants.NOTIFICATION_BODY);

        notificationBody = findViewById(R.id.gb_notification_body);
        notificationIcon = findViewById(R.id.gb_notification_icon);
        notificationTitle = findViewById(R.id.gb_notification_title);
        closeBtn = findViewById(R.id.gb_close_btn);
    }

    private void fillView()
    {
        notificationBody.setText(notificationBodyObj.getBody());
        notificationTitle.setText(notificationBodyObj.getTitle());

        Picasso.get()
                .load(notificationBodyObj.getIcon())
                .into(notificationIcon);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
