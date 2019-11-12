package com.gameball.androidx.views.notification;

import com.gameball.androidx.model.response.Notification;
import com.gameball.androidx.utils.BasePresenter;
import com.gameball.androidx.utils.BaseView;

import java.util.ArrayList;

public interface NotificationsContract {
    interface View extends BaseView {
        void onNotificationsLoaded(ArrayList<Notification> notifications);
        void showNoInternetLayout();
    }

    interface Presenter extends BasePresenter {
        void getNotificationHistory();
    }
}
