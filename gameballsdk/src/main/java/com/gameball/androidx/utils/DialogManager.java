package com.gameball.androidx.utils;

import android.content.Context;
import android.content.DialogInterface;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gameball.androidx.R;
import com.gameball.androidx.model.response.NotificationBody;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


/**
 * Created by abdelhafiz on 10/07/18.
 */
public class DialogManager
{
    public static void showCustomNotification(final Context context, NotificationBody notificationBody)
    {
        if (context != null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View toastLayout = inflater.inflate(R.layout.gb_activity_small_notification, null);
            toastLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            final Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.FILL_HORIZONTAL, 0, (int)DisplayUtils.convertDpToPixel(18));
            toast.setView(toastLayout);

            TextView text = toastLayout.findViewById(R.id.gb_notification_body);
            TextView title = toastLayout.findViewById(R.id.gb_notification_title);
            ImageView icon = toastLayout.findViewById(R.id.gb_notification_icon);

            text.setText(notificationBody.getBody());
            title.setText(notificationBody.getTitle());

            Picasso.get()
                    .load(notificationBody.getIcon())
                    .into(icon, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {
                            toast.show();
                        }

                        @Override
                        public void onError(Exception e)
                        {

                        }
                    });

        }
    }
}
