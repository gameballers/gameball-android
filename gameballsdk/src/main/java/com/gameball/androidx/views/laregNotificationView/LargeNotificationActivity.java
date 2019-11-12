package com.gameball.androidx.views.laregNotificationView;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gameball.androidx.R;
import com.gameball.androidx.model.response.NotificationBody;
import com.gameball.androidx.utils.Constants;
import com.squareup.picasso.Picasso;

public class LargeNotificationActivity extends AppCompatActivity {

    private TextView notificationTitle;
    private TextView notificationBody;
    private ImageView notificationIcon;
    private ImageButton closeBtn;

    private NotificationBody notificationBodyObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_notification);
        init();

        fillView();
    }

    private void init()
    {
        if(getIntent() != null)
            notificationBodyObj = (NotificationBody) getIntent().getExtras().getSerializable(Constants.NOTIFICATION_OBJ);

        notificationBody = findViewById(R.id.notification_body);
        notificationIcon = findViewById(R.id.notification_icon);
        notificationTitle = findViewById(R.id.notification_title);
        closeBtn = findViewById(R.id.close_btn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
