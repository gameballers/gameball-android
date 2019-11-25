package com.gameball.androidx.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gameball.androidx.R;
import com.gameball.androidx.model.response.NotificationBody;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;


/**
 * Created by abdelhafiz on 10/07/18.
 */
public class DialogManager
{

    private static DialogManager dialogManagerInstance;

    public LinkedList<NotificationBody> notificationBodyQueue = new LinkedList<>();

    public Toast smallNotificationToast;

    public NotificationBody notificationBody;

    public static DialogManager getInstance() {
        if(dialogManagerInstance == null)
            dialogManagerInstance = new DialogManager();
        return dialogManagerInstance;
    }


    public static void addNotificationToQue(final Context context, NotificationBody notificationBody) {
        DialogManager.getInstance().notificationBodyQueue.add(notificationBody);

        if(DialogManager.getInstance().notificationBodyQueue.size() == 1)
            showCustomNotification(context);
    }

    public static void showCustomNotification(final Context context) {
        if(DialogManager.getInstance().notificationBodyQueue.isEmpty())
            return;

        NotificationBody notificationBody = DialogManager.getInstance().notificationBodyQueue.getFirst();
        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View toastLayout = inflater.inflate(R.layout.gb_activity_small_notification, null);
            toastLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            final Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, (int) DisplayUtils.convertDpToPixel(18));
            toast.setView(toastLayout);

            TextView text = toastLayout.findViewById(R.id.gb_notification_body);
            TextView title = toastLayout.findViewById(R.id.gb_notification_title);
            ImageView icon = toastLayout.findViewById(R.id.gb_notification_icon);

            text.setText(notificationBody.getBody());
            title.setText(notificationBody.getTitle());


            CountDownTimer toastCountDown = null;

            if(DialogManager.getInstance().notificationBody == null) {
                DialogManager.getInstance().notificationBody = notificationBody;

                toastCountDown = new CountDownTimer(5000, 2500 /*Tick duration*/) {
                    public void onTick(long millisUntilFinished) {
                        showCustomNotification(context);
                    }

                    public void onFinish() {
                        toast.cancel();
                        DialogManager.getInstance().notificationBodyQueue.removeFirst();
                        DialogManager.getInstance().notificationBody = null;
                        showCustomNotification(context);
                    }
                };
            }

            final CountDownTimer finalToastCountDown = toastCountDown;
            Picasso.get()
                    .load(notificationBody.getIcon())
                    .into(icon, new Callback() {
                        @Override
                        public void onSuccess() {
                            toast.show();
                            if(finalToastCountDown != null)
                                finalToastCountDown.start();
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        }
    }
}
