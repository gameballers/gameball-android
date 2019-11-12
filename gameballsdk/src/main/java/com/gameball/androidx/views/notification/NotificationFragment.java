package com.gameball.androidx.views.notification;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gameball.androidx.R;
import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.model.response.Notification;

import java.util.ArrayList;

public class NotificationFragment extends Fragment implements NotificationsContract.View {

    private View rootView;

    private TextView notificationsTitle;
    private RecyclerView notificationsList;
    private RelativeLayout noInternetLayout;

    private ProgressBar loadingIndicator;
    private NotificationsHistoryAdapter adapter;
    private NotificationsContract.Presenter presenter;
    private ClientBotSettings clientBotSettings;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NotificationsHistoryAdapter(getContext(), new ArrayList<Notification>());
        presenter = new NotificationsPresenter(this);
        clientBotSettings = SharedPreferencesUtils.getInstance().getClientBotSettings();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        initView();
        setupBotSettings();
        presenter.getNotificationHistory();
        return rootView;
    }

    private void initView()
    {
        notificationsTitle = rootView.findViewById(R.id.notification_title);
        notificationsList = rootView.findViewById(R.id.notification_list);
        loadingIndicator = rootView.findViewById(R.id.loading_indicator);
        noInternetLayout = rootView.findViewById(R.id.no_internet_layout);

        notificationsList.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationsList.setHasFixedSize(true);
        notificationsList.setAdapter(adapter);
    }

    private void setupBotSettings() {
        loadingIndicator.getIndeterminateDrawable().setColorFilter(Color.parseColor(clientBotSettings.getBotMainColor()),
                PorterDuff.Mode.SRC_IN);
        notificationsTitle.setTextColor(Color.parseColor(clientBotSettings.getBotMainColor()));
    }

    @Override
    public void onNotificationsLoaded(ArrayList<Notification> notifications) {
        adapter.setMdata(notifications);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showNoInternetLayout() {
        noInternetLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingIndicator() {
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        presenter.unsubscribe();
        super.onStop();
    }
}
